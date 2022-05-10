package com.tnfigueiredo.kafkatweeter.producer.app.kafka

import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaTweetsProducer(
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(KafkaTweetsProducer::class.java)
    }

    fun send(key: String, tweet: String) {
        LOGGER.info("Tweet message: {}", tweet)
        kafkaTemplate.send("tweets-message-topic", key, tweet)
    }

}