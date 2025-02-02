package com.store.store.controller;


import com.store.store.entity.StoreEntity;
import com.store.store.kafka.KafkaProducer;
import com.store.store.mapper.StoreEntityToJason;
import com.store.store.service.ServiceImp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/store_app")
public class StoreController {

    @Autowired
    private ServiceImp serviceImp;

    @Autowired
    private KafkaProducer kafkaProducer;

    public StoreController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("/create")
    public ResponseEntity<StoreEntity> create(@Valid @RequestBody StoreEntity storeEntity){
        String kafkaMessage= StoreEntityToJason.convertNotificationToJason(storeEntity);
        kafkaProducer.sendMessage((kafkaMessage));
        return new ResponseEntity<>(serviceImp.create(storeEntity), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        serviceImp.deleteStore(id);
        return new ResponseEntity<>("seccessfully delete", HttpStatus.OK);
    }
    @DeleteMapping("/all")
    public ResponseEntity<String> deleteAll(){
        serviceImp.deleteAll();
        return new ResponseEntity<>("seccessfully delete", HttpStatus.OK);
    }

}
