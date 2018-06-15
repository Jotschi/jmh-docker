package de.jotschi.test;

import java.io.File;

import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.VerboseMode;

public class JMHRunner {

	public static void main(String[] args) throws RunnerException {
		File outputDir = new File("target/results");
		if (!outputDir.exists()) {
			if (!outputDir.mkdirs()) {
				throw new RuntimeException("Could not create output dir {" + outputDir.getAbsolutePath() + "}");
			}
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
