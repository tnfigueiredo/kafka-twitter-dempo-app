package com.example.kafkatweeter.consumer.app.kafka

import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.common.xcontent.XContentType
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class KafkaTweetsConsumer(
    private val elasticSearchClient: RestHighLevelClient,
    @Value("\${elasticsearch.index}")
    private val tweeterIndex: String
) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(KafkaTweetsConsumer::class.java)
    }

    @KafkaListener(topics = ["tweets-message-topic"], groupId = "simple-kotlin-tweets-message-consumer")
    fun consume(tweet: String) {
        LOGGER.info("got tweet: {}", tweet)
        val indexRequest = IndexRequest(tweeterIndex).source(tweet, XContentType.JSON)
        val indexResponse = elasticSearchClient.index(indexRequest, RequestOptions.DEFAULT)
        LOGGER.info("ElasticSearch id: {}", indexResponse.id)
    }
}