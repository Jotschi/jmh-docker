package de.jotschi.jmh;

import org.testcontainers.containers.wait.strategy.AbstractWaitStrategy;

public class NoWaitStrategy extends AbstractWaitStrategy {

	@Override
	protected void waitUntilReady() {

	}

}
