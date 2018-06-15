package de.jotschi.jmh;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.ContainerLaunchException;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.OutputFrame;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.utility.LazyFuture;

public class JMHContainer extends GenericContainer<JMHContainer> {

	private static final Logger log = LoggerFactory.getLogger(JMHContainer.class);

	private MavenBuildLock buildLock = new MavenBuildLock();
	private Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(log);

	public static LazyFuture<String> prepareDockerImage() {
		ImageFromDockerfile dockerImage = new ImageFromDockerfile("jmh-container", true);
		dockerImage.withFileFromClasspath("Dockerfile", "/Dockerfile");
		dockerImage.withFileFromFile("/", new File("."));
		return dockerImage;
	}

	public JMHContainer() {
		super(prepareDockerImage());
		setWaitStrategy(Wait.forLogMessage("SUCCESS", 1));
	}

	@Override
	protected void configure() {
		withFileSystemBind("/opt/.m2", "/root/.m2");
		withFileSystemBind("/opt/results", "/maven/target/results");
		withStartupTimeout(Duration.ofMinutes(15));
		withWorkingDirectory("/maven");
		withCommand("bash", "-c", "mvn -B test-compile exec:exec && sleep 20");
		setStartupAttempts(1);
		waitingFor(new NoWaitStrategy());
		setLogConsumers(Arrays.asList(logConsumer, buildLock));
	}

	@Override
	public void start() {
		super.start();
		try {
			buildLock.await(10, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			throw new ContainerLaunchException("Container build did not not finish on-time", e);
		}
	}

	/**
	 * Download the JMH result to the client.
	 * 
	 * @return
	 * @throws IOException
	 */
	public JMHContainer downloadResult() throws IOException {
		copyFileFromContainer("/maven/target/results/current.json", "test");
		return this;
	}

}

class MavenBuildLock implements Consumer<OutputFrame> {

	private CountDownLatch latch = new CountDownLatch(1);

	private static Logger log = LoggerFactory.getLogger(MavenBuildLock.class);

	@Override
	public void accept(OutputFrame frame) {
		if (frame != null) {
			String utf8String = frame.getUtf8String();
			if (utf8String.contains("SUCCESS")) {
				log.info("Build finished. Releasing lock");
				latch.countDown();
			}
		}
	}

	public void await(int value, TimeUnit unit) throws InterruptedException {
		if (!latch.await(value, unit)) {
			throw new RuntimeException("Build did not finish in time.");
		}
	}
}
