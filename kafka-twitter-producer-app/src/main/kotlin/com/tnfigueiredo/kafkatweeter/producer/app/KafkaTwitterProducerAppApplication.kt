package com.tnfigueiredo.kafkatweeter.producer.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KafkaTwitterProducerAppApplication

fun main(args: Array<String>) {
	runApplication<KafkaTwitterProducerAppApplication>(*args)
}
