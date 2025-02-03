package com.store.store.service;

import com.store.store.entity.*;
import com.store.store.kafka.KafkaProducer;
import com.store.store.mapper.StoreEntityToJason;
import com.store.store.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    @Autowired
    private StoreFailRepo storeFailRepo;




    public StoreEntity create(StoreEntity storeEntity) {



        //unique store notification ID
        storeEntity.setStore_key("store-notification-"+ UUID.randomUUID().toString());

        Message message= messageRepo.save(storeEntity.getValue().getMessage());
        Destination destination= destinationRepo.save(storeEntity.getValue().getDestination());;
        Metadata metadata= storeEntity.getValue().getMetadata();

        //set meta data timestamp
        metadata.setTimestamp(String.valueOf(LocalDateTime.now()));
        metadataRepo.save(metadata);

        Value value =valueRepo.save(storeEntity.getValue());

        return storeRepo.save(storeEntity);

    }

    public StoreFailure sendFailMessage (StoreEntity storeEntity){

        StoreFailure.ValueFail.MessageFail messageFail= new StoreFailure.ValueFail.MessageFail(
                storeEntity.getValue().getMessage().getTitle(),
                storeEntity.getValue().getMessage().getBody()
        );

        StoreFailure.ValueFail.DestinationFail destinationFail= new StoreFailure.ValueFail.DestinationFail(
                storeEntity.getValue().getDestination().getStore(),
                storeEntity.getValue().getDestination().getDepartment(),
                storeEntity.getValue().getDestination().getJobcode(),
                storeEntity.getValue().getDestination().getRole(),
                storeEntity.getValue().getDestination().getSalesId(),
                storeEntity.getValue().getDestination().getSourceSystem()
        );
        
        StoreFailure.ValueFail.MetadataFail metadataFail=new StoreFailure.ValueFail.MetadataFail(
                storeEntity.getValue().getMetadata().getPriority(),
                storeEntity.getValue().getMetadata().getTimestamp()
        );


        StoreFailure.ValueFail valueFail= new StoreFailure.ValueFail(messageFail,destinationFail,metadataFail);

        StoreFailure storeFailEntity= new StoreFailure(
                storeEntity.getId(),
                storeEntity.getStore_key(),
                valueFail
        );
        
        return storeFailRepo.save(storeFailEntity);
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
