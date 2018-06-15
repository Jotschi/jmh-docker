package de.jotschi.jmh;

import org.testcontainers.containers.wait.strategy.AbstractWaitStrategy;

public class TerminationWaitStrategy extends AbstractWaitStrategy {

	@Override
	protected void waitUntilReady() {
		while (waitStrategyTarget.isRunning()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}

}
