# JMH Docker Setup

Example project which demonstrates how to run [JMH Benchmark](http://openjdk.java.net/projects/code-tools/jmh/) tests using docker.

The [testcontainers](https://www.testcontainers.org/) Java test library will be used to generate and run a container which contains the [JMH](http://openjdk.java.net/projects/code-tools/jmh/) tests.


## How to run

Just run the `JMHRunnerTest`. This test will build and run the docker container.

You can run the JMH Visualizer to display the benchmark results.

```bash
docker run -p 8080:80 jotschi/jmh-visualizer
```