# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.6.7/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.6.7/gradle-plugin/reference/html/#build-image)
* [Spring for Apache Kafka](https://docs.spring.io/spring-boot/docs/2.6.7/reference/htmlsingle/#boot-features-kafka)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)
* [Twitter Developer Portal](https://developer.twitter.com/en/portal/dashboard)
* [Config API Key and App in Developer Portal](https://www.youtube.com/watch?v=vlvtqp44xoQ)

## Kafka/Twitter Demo App

The idea of this sample is to present a simple demo for a producer application and a consumer application that:

* Read some tweets from Twitter;
* Post those tweet messages into a kafka topic;
* Consume those kafka messages for those tweets;
* Save those tweets on an ElasticSearh repository.

This sample will have all this structure organized in a docker-compose file with all the necessary dependencies organized
ready to only run the mentioned components. For this application it is important to mention that for security
reasons the credentials used to communicate with the Twitter API are not present in the producer application. This brings
the need to configure a Twitter Developer account and replace those values in the consumer Spring configuration file.

Since the configurations on Twitter Developer Portal are not the focus for this sample, the Additional Links session has
a reference to the portal and a video with instructions to create the app there. One important thing for your sample to work: 
your application need to have elevated privilege to read the tweets.

### The Application Structure

As mentioned right above, we have a structure handled inside a docker-compose configuration. One first point
of this structure are the configuration layers that we have inside the [docker-compose.yml](docker-compose.yml) file.
It was created a temporary image just to connect to the kafka service and create the topic used by all the components
involved. There is a layered image to build out of the box both the producer and the consumer. Besides the mentioned
components we have the services and applications used in this demonstration:

![cached image](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/tnfigueiredo/kafka-twitter-dempo-app/main/kafka-twitter-producer-consumer-demo.puml)

Into this structure the components share a common network configuration. So the consumer and producer apps already have
enough configurations to reach the Kafka topic they interact with. In this collaboration diagram it is possible to 
understand better how the interaction among those components behave:

![cached image](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/tnfigueiredo/kafka-twitter-dempo-app/main/kafka-twitter-demo-collaboration.puml)