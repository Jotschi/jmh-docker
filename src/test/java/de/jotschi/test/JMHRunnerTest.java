package de.jotschi.test;

import java.io.IOException;

import org.junit.ClassRule;
import org.junit.Test;

import de.jotschi.jmh.JMHContainer;

public class JMHRunnerTest {

	static {
		// Run the benchmarks on a dedicated host.
		System.setProperty("DOCKER_HOST", "tcp://hyperion.cluster.gentics.com:2375");
	}

	@ClassRule
	public static JMHContainer container = new JMHContainer("1.2.0")
		.withFileSystemBind("/opt/.m2", "/root/.m2")
		.withFileSystemBind("/opt/results", "/maven/target/results");

	@Test
	public void testBenchmarks() throws IOException {
		container.downloadResult("target/test.json");
	}
}
