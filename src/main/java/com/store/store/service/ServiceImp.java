package com.store.store.service;

import com.store.store.entity.*;
import com.store.store.kafka.KafkaProducer;
import com.store.store.mapper.StoreEntityToJason;
import com.store.store.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
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
    private KafkaProducer kafkaProducer;




    public StoreEntity create(StoreEntity storeEntity) {



        //unique store notification ID
        storeEntity.setStore_key("store-notification-"+ UUID.randomUUID().toString());

        Message message= messageRepo.save(storeEntity.getValue().getMessage());
        Destination destination= destinationRepo.save(storeEntity.getValue().getDestination());;
        Metadata metadata= storeEntity.getValue().getMetadata();

        //set meta data timestamp
        metadata.setTimestamp(String.valueOf(System.currentTimeMillis()));
        metadataRepo.save(metadata);

        Value value =valueRepo.save(storeEntity.getValue());

        return storeRepo.save(storeEntity);

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
