package com.store.store.kafka;

import com.store.store.entity.StoreEntity;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.network.Send;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class KafkaProducer {

    private static final Logger LOGGER=LoggerFactory.getLogger(KafkaProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String storeEntity){

        Long startTime= System.currentTimeMillis();

        try{
        SendResult<String,String> result= kafkaTemplate.send("Store",storeEntity).get();
        Long duration= System.currentTimeMillis()-startTime;
        RecordMetadata metadata =result.getRecordMetadata();
            LOGGER.info("message sent {}", storeEntity);
        LOGGER.info("Message sent to partiton {} with offset {} time take {}",
                metadata.partition(),metadata.offset(),duration);

        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("Error sending",e);
        }

    }

}
