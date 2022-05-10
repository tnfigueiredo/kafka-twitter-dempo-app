package com.tnfigueiredo.kafkatweeter.producer.app.resources

import com.google.gson.JsonParser
import com.tnfigueiredo.kafkatweeter.producer.app.kafka.KafkaTweetsProducer
import com.twitter.hbc.core.Client
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.BlockingQueue
import java.util.concurrent.TimeUnit

@RestController
@RequestMapping("/api/twitter-consumers")
class TwitterConsumersResource(
    private val tweeterClient: Client,
    private val msgQueue: BlockingQueue<String>,
    private val kafkaTweetsProducer: KafkaTweetsProducer,

    ) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(TwitterConsumersResource::class.java)
    }

    @PostMapping
    fun consumeFromTwitter() {
        CoroutineScope(Dispatchers.Default).async {
            tweeterClient.connect()
            while (!tweeterClient.isDone()) {
                val msg = msgQueue.poll(5, TimeUnit.SECONDS)
                val tweet = JsonParser.parseString(msg).asJsonObject
                LOGGER.info(tweet.toString())
                kafkaTweetsProducer.send(tweet.get("id").toString(), msg)
            }
        }
    }

    @DeleteMapping
    fun stopConsumingFromTweeter(){
        tweeterClient.stop()
    }

}