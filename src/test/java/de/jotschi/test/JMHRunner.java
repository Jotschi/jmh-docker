package de.jotschi.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.VerboseMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JMHRunner {

	private static final Logger log = LoggerFactory.getLogger(JMHRunner.class);

	private static File OUTPUT_DIR = new File("target/results");

	public static void main(String[] args) throws RunnerException, IOException {
		if (args.length != 1) {
			throw new RuntimeException("Invalid arguments. Only the name of the benchmark output file can be specified.");
		}
		String name = args[0];
		if (!OUTPUT_DIR.exists()) {
			if (!OUTPUT_DIR.mkdirs()) {
				throw new RuntimeException("Could not create output dir {" + OUTPUT_DIR.getAbsolutePath() + "}");
			}
		}

		Options opt = new OptionsBuilder()
			.include(MyBenchmark.class.getSimpleName())
			.forks(1)
			.warmupIterations(2)
			.measurementIterations(2)
			.resultFormat(ResultFormatType.JSON)
			.result(new File(OUTPUT_DIR, name + ".json").getAbsolutePath())
			.verbosity(VerboseMode.EXTRA)
			.build();
		new Runner(opt).run();

		generateProvidedJson();
	}

	/**
	 * Generate a provided.js file which can be used in conjunction with JMH Visualizer.
	 * 
	 * @throws IOException
	 */
	private static void generateProvidedJson() throws IOException {

		StringBuilder builder = new StringBuilder();

		Set<String> names = new HashSet<>();
		builder.append("var providedBenchmarkStore = {\n");
		Set<String> contents = Files.list(OUTPUT_DIR.toPath())
			.filter(Files::isRegularFile)
			.filter(f -> f.getFileName().toString().endsWith(".json"))
			//.sorted(comparator)
			.map(file -> {
				String name = file.getFileName().toString();
				name = name.replaceAll(".json", "");
				names.add("'" + name + "'");
				log.info("Handling result file {" + file + "}");
				try {
					String content = new String(Files.readAllBytes(file));
					return name + ":" + content;
				} catch (IOException e) {
					throw new RuntimeException("Could not read file {" + file + "}");
				}
			}).collect(Collectors.toSet());

		String joinedJson = String.join(",", contents);
		builder.append(joinedJson);
		builder.append("};\n");

		String joined = String.join(",", names);
		builder.append("var providedBenchmarks = [" + joined + "];\n");

		String fileContent = builder.toString();
		log.debug("Generated provided.json:\n" + fileContent);
		Files.write(new File(OUTPUT_DIR, "provided.js").toPath(), fileContent.getBytes());
	}
}
