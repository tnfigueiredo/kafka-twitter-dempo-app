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

### Running the sample

Due to the fact of using a docker-compose structure, running the sample is simple. When the docker-compose services are started
the whole stack is up and running. The dependence relationship between the services allow the stack to have everything ready when
it finishes to start up:

```shell
sudo docker-compose up --build
Creating network "kafka-twitter-demo-app_default" with the default driver
Creating volume "kafka-twitter-demo-app_elasticsearch-data" with local driver
Building kafka-twitter-producer-app
Step 1/10 : FROM gradle:7-jdk11-alpine AS build
 ---> 2ed4ab3bd3fb
Step 2/10 : COPY --chown=gradle:gradle . /home/gradle/src
 ---> Using cache
 ---> 0ccdc306e13c
Step 3/10 : WORKDIR /home/gradle/src
 ---> Using cache
 ---> 176bef65415d
Step 4/10 : RUN gradle build --no-daemon
 ---> Running in 1282010f596b

Welcome to Gradle 7.4.2!

Here are the highlights of this release:
 - Aggregated test and JaCoCo reports
 - Marking additional test source directories as tests in IntelliJ
 - Support for Adoptium JDKs in Java toolchains

For more details see https://docs.gradle.org/7.4.2/release-notes.html

To honour the JVM settings for this build a single-use Daemon process will be forked. See https://docs.gradle.org/7.4.2/userguide/gradle_daemon.html#sec:disabling_the_daemon.
Daemon will be stopped at the end of the build 
> Task :processResources
> Task :processTestResources
> Task :compileKotlin
> Task :compileJava NO-SOURCE
> Task :classes
> Task :bootJarMainClassName
> Task :bootJar
> Task :inspectClassesForKotlinIC
> Task :jar
> Task :assemble
> Task :compileTestKotlin
> Task :compileTestJava NO-SOURCE
> Task :testClasses
> Task :test
> Task :check
> Task :build

BUILD SUCCESSFUL in 3m 30s
9 actionable tasks: 9 executed
Removing intermediate container 1282010f596b
 ---> 920eb9de0e1a
Step 5/10 : FROM openjdk:11.0.14-jre-slim-buster
 ---> 4021b7ee9099
Step 6/10 : RUN mkdir /app
 ---> Using cache
 ---> 30360b99b00a
Step 7/10 : COPY --from=build /home/gradle/src/build/libs/*-SNAPSHOT.jar /app/kafka-twitter-producer-app.jar
 ---> ba28e5723982
Step 8/10 : WORKDIR /app
 ---> Running in 5ae1717449db
Removing intermediate container 5ae1717449db
 ---> ead41ca642ec
Step 9/10 : EXPOSE 8080
 ---> Running in e8b819d5e446
Removing intermediate container e8b819d5e446
 ---> c23523526049
Step 10/10 : CMD ["java", "-jar", "kafka-twitter-producer-app.jar"]
 ---> Running in bbca114c7ae1
Removing intermediate container bbca114c7ae1
 ---> e1c961ea8254
Successfully built e1c961ea8254
Successfully tagged kafka-twitter-demo-app_kafka-twitter-producer-app:latest
Building kafka-twitter-consumer-app
Step 1/10 : FROM gradle:7-jdk11-alpine AS build
 ---> 2ed4ab3bd3fb
Step 2/10 : COPY --chown=gradle:gradle . /home/gradle/src
 ---> Using cache
 ---> 20917b392a6e
Step 3/10 : WORKDIR /home/gradle/src
 ---> Using cache
 ---> a331f55d2d50
Step 4/10 : RUN gradle build --no-daemon
 ---> Using cache
 ---> ea03246c477e
Step 5/10 : FROM openjdk:11.0.14-jre-slim-buster
 ---> 4021b7ee9099
Step 6/10 : RUN mkdir /app
 ---> Using cache
 ---> 30360b99b00a
Step 7/10 : COPY --from=build /home/gradle/src/build/libs/*-SNAPSHOT.jar /app/kafka-twitter-consumer-app.jar
 ---> Using cache
 ---> ba486e943a55
Step 8/10 : WORKDIR /app
 ---> Using cache
 ---> 46a0e35f5158
Step 9/10 : EXPOSE 8080
 ---> Using cache
 ---> 2b5b8b9a7e23
Step 10/10 : CMD ["java", "-jar", "kafka-twitter-consumer-app.jar"]
 ---> Using cache
 ---> c7d0d2df298b
Successfully built c7d0d2df298b
Successfully tagged kafka-twitter-demo-app_kafka-twitter-consumer-app:latest
Starting kafka-twitter-demo-app_zookeeper_1 ... done
Starting elasticsearch                      ... done
Starting kibana                             ... done
Starting kafka-twitter-demo-app_kafka_1     ... done
Starting kafka-twitter-demo-app_init-kafka_1 ... done
Recreating kafka-twitter-demo-app_kafka-twitter-producer-app_1 ... done
Starting kafka-twitter-demo-app_kafka-twitter-consumer-app_1   ... done
```

The log of the shell execution gives us an idea that the whole stack is available. If any problem happens during this process
it will be necessary to verify where the build failed and correct the failures that may rise during the process. And eve after
the starting of the services stack, it is possible to check if everything is up and running:

```shell
sudo docker-compose ps
                       Name                                      Command               State                            Ports                         
------------------------------------------------------------------------------------------------------------------------------------------------------
elasticsearch                                         /usr/local/bin/docker-entr ...   Up       0.0.0.0:9200->9200/tcp,:::9200->9200/tcp,             
                                                                                                0.0.0.0:9300->9300/tcp,:::9300->9300/tcp              
kafka-twitter-demo-app_init-kafka_1                   /bin/sh -c                       Exit 1                                                         
                                                      # blocks until ...                                                                              
kafka-twitter-demo-app_kafka-twitter-consumer-app_1   java -jar kafka-twitter-co ...   Up       0.0.0.0:8081->8080/tcp,:::8081->8080/tcp              
kafka-twitter-demo-app_kafka-twitter-producer-app_1   java -jar kafka-twitter-pr ...   Up       0.0.0.0:8080->8080/tcp,:::8080->8080/tcp              
kafka-twitter-demo-app_kafka_1                        /etc/confluent/docker/run        Exit 1                                                         
kafka-twitter-demo-app_zookeeper_1                    /etc/confluent/docker/run        Up       0.0.0.0:2181->2181/tcp,:::2181->2181/tcp, 2888/tcp,   
                                                                                                3888/tcp                                              
kibana                                                /usr/local/bin/dumb-init - ...   Up       0.0.0.0:5601->5601/tcp,:::5601->5601/tcp
```
Once all the services are running it is possible to access the kibana service through the address [http://localhost:5601/app/kibana](http://localhost:5601/app/kibana).
The Kibana will be the interface used to check the information that our consumer will be indexing from the tweeters that our producer will
read and publish based on our searching criteria implemented.

<p align="center">
  <img src="https://raw.githubusercontent.com/tnfigueiredo/kafka-twitter-dempo-app/main/kibana-home.png" title="Kibana Home">
</p>

Before trigger the tweets' consumption it will be necessary to create an index in the ElasticSearch repository. To reach this we created a few request in the
[Postman collection](kafka-twitter-demo-app.postman_collection.json) attached to the project. But they can be created also through curl
commands on the terminal also. To crate the index the collection has the request create-elasticsearch-twitter-index. The corresponding
curl command for this request is the following one:

```shell
curl --location --request PUT 'localhost:9200/twitter'
```

The result is possible to see accessing the Dashboard left panel option, Index Management menu item:

<p align="center">
  <img src="https://raw.githubusercontent.com/tnfigueiredo/kafka-twitter-dempo-app/main/kibana-index-management.png" title="Kibana Index Management">
</p>

Besides, the index creation used for saving the tweets it is necessary to modify a configuration related to the content to be stored for
the tweets that are going to be sent to our Kafka consumer. Some tweets had a heavy content and without this configuration some of them
will fail the saving operation. The request on the Postman collection is the create-elasticsearch-twitter-index-config. Here follows the
corresponding curl command:

```shell
curl --location --request PUT 'localhost:9200/twitter/_settings' \
--header 'Content-Type: application/json' \
--data-raw '{
  "index.mapping.total_fields.limit": 10000
}'
```

After checking that the index is created it is necessary to create and index pattern configuration to use it on the data search
for the tweets that we ar going to read buy our applications. This configuration is necessary to allow us to check the data consumed
by our application after we trigger the mechanisms we have on our components. It is necessary to access the menu item index patterns
on the Kibana menu session and select our created menu index pattern:

<p align="center">
  <img src="https://raw.githubusercontent.com/tnfigueiredo/kafka-twitter-dempo-app/main/kibana-index-created.png" title="Kibana Index For Search">
</p>

The whole lifecycle from our sample happens since the building of the application structure, passing
by its execution, working on the data consumption, and stopping the tweets' consumption. After that, to shut down the whole environment
stack it is just to run the following command:

```shell
sudo docker-compose down
```

### Producing and consuming data in the sample

Once there is the configurations needed on the sample all set up, it is possible to start the application that will consume
the tweets' information and see all of them indexed on the ElasticSearch repository. After the step of creating the index necessary to be managed on kibana interface, it is possible to start our producer application tweets' consumption. 
The main responsibility of this component at the solution is to read the tweets based on some parameters and produce Kafka events to allow our
consumer application to consume and store their content. To make it easier to handle the start/stop mechanism for our solution
the tweets' consumption is encapsulated in an asynchronous process that is triggered and stopped through a RESTful
endpoint. The endpoint that triggers the consumption is Postman collection request trigger-kafka-twitter-consumer.
Its corresponding curl command is the following one:

```shell
curl --location --request POST 'localhost:8080/api/twitter-consumers'
```

Once we trigger the tweets' consumption, it will be possible to check on the Kibana interface the tweet messages
stored on the ElasticSearch repository. To check the application activity it is possible to access the container and
check the application logs:

```shell
sudo docker exec -it kafka-twitter-demo-app_kafka-twitter-producer-app_1 bash
root@ff25f7ff4b53:/app# ls
kafka-twitter-producer-app.jar	spring.log
less -f spring.log
```

And the application interface configuration to check the information handled by the sample application can be accessed on the left panel
option called "Kibana >> Saved Objects". It is possible to trigger this option and select our index pattern option it is possible to check the information
on raw filtering options, or even on filtering options defined into the kibana interface:

<p align="center">
  <img src="https://raw.githubusercontent.com/tnfigueiredo/kafka-twitter-dempo-app/main/kibana-index-saved-objects.png" title="Kibana Index Saved Objects">
</p>

To search the information stored on the ElasticSearch repository it is necessary to access the menu option "Discovery" on the left panel 
of the Kibana interface. This option will bring an interface that will allow to search the fields of the indexed information, making possible to
filter data based on any criteria from the saved objects structure and content.

<p align="center">
  <img src="https://raw.githubusercontent.com/tnfigueiredo/kafka-twitter-dempo-app/main/kibana-criteria-search.png" title="Kibana Search Interface">
</p>

To stop the tweets' consumption is just to trigger the Postman request stop-kafka-twitter-consumer. Its
corresponding curl command is the following one:

```shell
curl --location --request DELETE 'localhost:8080/api/twitter-consumers'
```
