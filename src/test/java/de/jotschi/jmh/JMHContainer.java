package de.jotschi.jmh;

import java.io.File;
import java.time.Duration;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.utility.LazyFuture;

public class JMHContainer extends GenericContainer<JMHContainer> {

	private static final Logger log = LoggerFactory.getLogger(JMHContainer.class);

	private Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(log);

	public static LazyFuture<String> prepareDockerImage() {
		ImageFromDockerfile dockerImage = new ImageFromDockerfile("jmh-container", true);
		dockerImage.withFileFromClasspath("Dockerfile", "/Dockerfile");
		dockerImage.withFileFromFile("/", new File("."));
		return dockerImage;
	}

	public JMHContainer() {
		super(prepareDockerImage());
	}

	@Override
	protected void configure() {
		withFileSystemBind("/opt/.m2", "/root/.m2");
		withFileSystemBind("/opt/results", "/maven/target/results");
		withStartupTimeout(Duration.ofMinutes(15));
		withWorkingDirectory("/maven");
		withCommand("mvn", "-B", "test-compile", "exec:exec");
		setLogConsumers(Arrays.asList(logConsumer));
		waitingFor(new TerminationWaitStrategy());
	}
}
