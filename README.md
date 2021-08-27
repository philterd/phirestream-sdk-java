# Phirestream SDK for Java

The **Phirestream SDK for Java** enables Java developers to easily work with Phirestream. [Phirestream](https://www.mtnfog.com/products/phirestream/) redacts PHI and PII from data stream. Refer to [Phirestream API](https://phirestream.mtnfog.com/api/) documentation for details on the methods available.

[![Build Status](https://travis-ci.org/mtnfog/phirestream-sdk-java.svg?branch=master)](https://travis-ci.org/mtnfog/phirestream-sdk-java)
![Maven Central](https://img.shields.io/maven-central/v/com.mtnfog/phirestream-java-sdk)
[![javadoc](https://javadoc.io/badge2/com.mtnfog/phirestream-java-sdk/javadoc.svg)](https://javadoc.io/doc/com.mtnfog/phirestream-java-sdk)

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
  <groupId>com.mtnfog</groupId>
  <artifactId>phirestream-sdk-java</artifactId>
  <version>1.0.0</version>
</dependency>
```

Snapshot dependencies are available in the Maven Central Snapshot Repository by adding the repository to your `pom.xml`:

```
<repository>
  <id>snapshots</id>
  <url>https://oss.sonatype.org/content/repositories/snapshots</url>
  <releases><enabled>false</enabled></releases>
  <snapshots><enabled>true</enabled></snapshots>
</repository>
```

## Release History

* 1.0.0:
  * Initial release.

## License

This project is licensed under the Apache License, version 2.0.

Copyright 2021 Mountain Fog, Inc.
Phirestream is a registered trademark of Mountain Fog, Inc.
