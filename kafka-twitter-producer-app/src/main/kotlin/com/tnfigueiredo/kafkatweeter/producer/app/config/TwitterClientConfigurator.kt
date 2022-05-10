package com.tnfigueiredo.kafkatweeter.producer.app.config

import com.twitter.hbc.ClientBuilder
import com.twitter.hbc.core.Client
import com.twitter.hbc.core.Constants
import com.twitter.hbc.core.HttpHosts
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint
import com.twitter.hbc.core.processor.StringDelimitedProcessor
import com.twitter.hbc.httpclient.auth.OAuth1
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue


@Configuration
class TwitterClientConfigurator(
    @Value("\${hbc-client.api-key}")
    private val apiKey: String,
    @Value("\${hbc-client.api-secret}")
    private val apiSecret: String,
    @Value("\${hbc-client.access-token}")
    private val accessToken: String,
    @Value("\${hbc-client.access-token-secret}")
    private val accessTokenSecret: String
) {

    @Bean
    fun msgQueue(): BlockingQueue<String> {
        return LinkedBlockingQueue(100000)
    }

    @Bean
    fun tweeterClient(msgQueue: BlockingQueue<String>): Client{
        val hosebirdEndpoint = StatusesFilterEndpoint()
        hosebirdEndpoint.trackTerms(listOf("bitcoin", "forabolsonaro"))

        return ClientBuilder()
            .name("Hosebird-Client-01") // optional: mainly for the logs
            .hosts(HttpHosts(Constants.STREAM_HOST))
            .authentication(OAuth1(apiKey, apiSecret, accessToken, accessTokenSecret))
            .endpoint(hosebirdEndpoint)
            .processor(StringDelimitedProcessor(msgQueue))
            .build()
    }

}