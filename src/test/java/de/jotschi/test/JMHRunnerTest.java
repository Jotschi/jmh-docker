package de.jotschi.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.VerboseMode;

public class JMHRunnerTest {

	@Test
	public void invokeJMH() throws RunnerException, IOException {
		File outputDir = new File("target/results");
		if (!outputDir.exists()) {
			assertTrue("Could not create output dir", outputDir.mkdirs());
		}

		Options opt = new OptionsBuilder()
			.include(MyBenchmark.class.getSimpleName())
			.forks(1)
			.warmupIterations(2)
			.measurementIterations(2)
			.resultFormat(ResultFormatType.JSON)
			.result(new File(outputDir, "current.json").getAbsolutePath())
			.verbosity(VerboseMode.EXTRA)
			.build();
		new Runner(opt).run();
	}
}
