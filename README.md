# JMH Docker Setup

Example project which demonstrates how to run JMH Benchmark tests using docker.

A docker image will be build using the maven project sources. This image can now be executed on the docker host. A junit test is used to run the JMH runner which will in turn run the benchmark and write the results to the `target/results` directory.

## How to run

```bash
export DOCKER_HOST=tcp://your.server.tld:2375
docker-compose up
```

You can run the JMH Visualizer to display the benchmark results.

```bash
docker run -p 8080:80 jotschi/jmh-visualizer
```