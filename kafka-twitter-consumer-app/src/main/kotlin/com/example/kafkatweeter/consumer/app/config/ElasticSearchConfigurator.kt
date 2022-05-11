package com.example.kafkatweeter.consumer.app.config

import org.elasticsearch.client.RestHighLevelClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.client.ClientConfiguration
import org.springframework.data.elasticsearch.client.RestClients

@Configuration
class ElasticSearchConfigurator(
    @Value("\${elasticsearch.host}")
    private val elasticSearchHost: String
) {

    @Bean
    fun elasticSearchClient(): RestHighLevelClient {
        val clientConfiguration = ClientConfiguration.builder()
            .connectedTo(elasticSearchHost)
            .build()
        return RestClients.create(clientConfiguration).rest()
    }

}