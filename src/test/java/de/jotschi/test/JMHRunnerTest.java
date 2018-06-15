package de.jotschi.test;

import org.junit.ClassRule;
import org.junit.Test;

import de.jotschi.jmh.JMHContainer;

public class JMHRunnerTest {

	@ClassRule
	public static JMHContainer container = new JMHContainer();

	@Test
	public void testBenchmarks() {

	}
}
