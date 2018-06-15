package de.jotschi.test;

import java.io.IOException;

import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;

import de.jotschi.jmh.AbstractJMHRunner;

public class JMHRunner extends AbstractJMHRunner {

	public static void main(String[] args) throws RunnerException, IOException {
		Options options = create(args)
			.include(MyBenchmark.class.getSimpleName())
			.build();
		run(options, true);
	}

}
