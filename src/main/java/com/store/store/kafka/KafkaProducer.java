package com.store.store.kafka;

import com.store.store.entity.StoreEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {


    private final KafkaTemplate<String, StoreEntity> kafkaTemplate;

    @Autowired
    public KafkaProducer(KafkaTemplate<String, StoreEntity> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(StoreEntity storeEntity){

        Message<StoreEntity> message= MessageBuilder
                        .withPayload(storeEntity)
                                .setHeader(KafkaHeaders.TOPIC, "store")
                                        .build();


        kafkaTemplate.send(message);
    }

}
