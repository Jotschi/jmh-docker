# JMH Docker Setup

Example project which demonstrates how to run [JMH Benchmark](http://openjdk.java.net/projects/code-tools/jmh/) tests using docker.

The [testcontainers](https://www.testcontainers.org/) Java test library will be used to generate and run a container which contains the [JMH](http://openjdk.java.net/projects/code-tools/jmh/) tests.

## How to run

Just run the `JMHRunnerTest`. This test will build and run the docker container. By default the results will be stored on the docker host in the `/opt/results` folder.

The setup will also generate a combined `provided.js` of all previously found benchmark result files. You can run the JMH Visualizer to display the these combined results.

```bash
docker run -p 8080:80 -v /opt/results/provided.js:/usr/share/nginx/html/provided.js jotschi/jmh-visualizer
```
