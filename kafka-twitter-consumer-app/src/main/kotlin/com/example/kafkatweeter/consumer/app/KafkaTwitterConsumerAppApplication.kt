package com.example.kafkatweeter.consumer.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KafkaTwitterConsumerAppApplication

fun main(args: Array<String>) {
	runApplication<KafkaTwitterConsumerAppApplication>(*args)
}
