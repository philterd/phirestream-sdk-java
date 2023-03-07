# Phirestream SDK for Java

The **Phirestream SDK for Java** is an API client for [Phirestream](https://www.mtnfog.com/products/phirestream/), software to identify and redact sensitive information such as PHI and PII from data streams. Phirestream is built upon the open source PII/PHI detection engine [Phileas](https://github.com/philterd/phileas).

Refer to the [Phirestream API](https://docs.mtnfog.com/phirestream/api-and-sdks/api) documentation for details on the methods available.

## Example Usage

With an available running instance of Phirestream configured with a connection to Apache Kafka brokers, to redact text:

```
PhirestreamClient client = new PhirestreamClient.PhirestreamClientBuilder().withEndpoint("https://127.0.0.1:8080").build();

List<Record> records = Arrays.asList(new Record("1", "George Washington was president."));
 ProduceResponse produceResponse = client.produce(records, "topic");
```

## Dependency

Release dependencies are available in Maven Central.

```
<dependency>
  <groupId>io.philterd</groupId>
  <artifactId>phirestream-sdk-java</artifactId>
  <version>1.0.0</version>
</dependency>
```

Snapshot dependencies are available in the Maven Central Snapshot Repository by adding the repository to your `pom.xml`:

```
<repository>
  <id>snapshots</id>
  <url>https://s01.oss.sonatype.org/content/repositories/snapshots</url>
  <releases><enabled>false</enabled></releases>
  <snapshots><enabled>true</enabled></snapshots>
</repository>
```

## Release History

* 1.0.0:
  * Initial release.

## License

This project is licensed under the Apache License, version 2.0.

Copyright 2021-2023 Mountain Fog, Inc.
Phirestream is a registered trademark of Mountain Fog, Inc.
