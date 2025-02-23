package com.store.store.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.store.entity.*;
import com.store.store.mapper.StoreEntityToJason;
import com.store.store.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ServiceImp {
    
    @Autowired
    private StoreRepo storeRepo;
    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private DestinationRepo destinationRepo;
    @Autowired
    private MetadataRepo metadataRepo;
    @Autowired
    private ValueRepo valueRepo;
    @Autowired
    private MessageChannel messageChannel;


    public StoreEntity create(StoreEntity storeEntity) throws JsonProcessingException {



        //unique store notification ID
        storeEntity.setStore_key("store-notification-"+ UUID.randomUUID().toString());

        Message message= messageRepo.save(storeEntity.getValue().getMessage());
        Destination destination= destinationRepo.save(storeEntity.getValue().getDestination());;
        Metadata metadata= storeEntity.getValue().getMetadata();

        //set meta data timestamp
        metadata.setTimestamp(String.valueOf(System.currentTimeMillis()));
        metadataRepo.save(metadata);

        Value value =valueRepo.save(storeEntity.getValue());
        StoreEntity saveStoreEntity= storeRepo.save(storeEntity);
        sendToMqtt(saveStoreEntity);
        return saveStoreEntity;

    }

    public void sendToMqtt(StoreEntity storeEntity) throws JsonProcessingException {
        try{
            String jasonPayload =new ObjectMapper().writeValueAsString(storeEntity.getValue().getDestination());
            System.out.println("Received "+ jasonPayload);
            org.springframework.messaging.Message<String> message= MessageBuilder
                    .withPayload(new ObjectMapper().writeValueAsString(storeEntity.getValue().getDestination().toString()))
                    .setHeader(MqttHeaders.TOPIC,"my_store")
                    .build();
            messageChannel.send(message);
        }catch (Exception e){
            System.out.println(" Error "+e.getMessage());
        }

    }


    public void deleteStore(Long id) {

        StoreEntity storeEntity= storeRepo.findById(id).orElseThrow(()->new RuntimeException("account not found"));
        if(storeEntity.getValue()!=null){
            if(storeEntity.getValue().getMessage()!=null){
                messageRepo.delete(storeEntity.getValue().getMessage());
            }if(storeEntity.getValue().getDestination()!=null){
                destinationRepo.delete(storeEntity.getValue().getDestination());
            }if(storeEntity.getValue().getMetadata()!=null){
                metadataRepo.delete(storeEntity.getValue().getMetadata());
            }

            valueRepo.delete(storeEntity.getValue());
            storeRepo.delete(storeEntity);

        }

    }

    public void deleteAll() {
        storeRepo.deleteAll();
        valueRepo.deleteAll();
        messageRepo.deleteAll();
        destinationRepo.deleteAll();
        metadataRepo.deleteAll();



    }
}
