package de.jotschi.test;

import java.io.IOException;

import org.junit.ClassRule;
import org.junit.Test;

import de.jotschi.jmh.JMHContainer;

public class JMHRunnerTest {

	static {
		System.setProperty("DOCKER_HOST", "tcp://hyperion.cluster.gentics.com:2375");
	}

	@ClassRule
	public static JMHContainer container = new JMHContainer();

	@Test
	public void testBenchmarks() throws IOException {
		container.downloadResult();
	}
}
