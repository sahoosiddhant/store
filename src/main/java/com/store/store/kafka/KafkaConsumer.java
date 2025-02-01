package com.store.store.kafka;

import com.store.store.entity.StoreEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "store", groupId = "my-consumer-group")
  public void listen(StoreEntity message){
      System.out.println("Received message"+message.toString());
  }
}
