package com.example.kafkatweeter.consumer.app

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = [ "listeners=PLAINTEXT://localhost:9092", "port=9092" ])
@ActiveProfiles("test")
class KafkaTwitterConsumerAppApplicationTests {

	@Test
	fun contextLoads() {
	}

}
