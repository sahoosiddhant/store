package com.store.store.mapper;


import com.store.store.entity.StoreEntity;
import org.springframework.stereotype.Service;

@Service
public class StoreEntityToJason {

    public static String convertNotificationToJason(StoreEntity storeEntity){

        return
                "{ \"key\": \"" +storeEntity.getStore_key()+"\","+
                "\"value\": {" +
            "\" message\": {"+
                "\"title\":\"" +storeEntity.getValue().getMessage().getTitle()+"\","+
                        "\"body\": \""+storeEntity.getValue().getMessage().getBody()+
           "\" },"+
            "\"destination\": {"+
                "\"store\":\"" +storeEntity.getValue().getDestination().getStore()+"\","+
                        "\"department\":\"" +storeEntity.getValue().getDestination().getDepartment()+"\","+
                        "\"jobcode\":\"" +storeEntity.getValue().getDestination().getJobcode()+"\","+
                        "\"role\":\"" +storeEntity.getValue().getDestination().getRole()+"\","+
                        "\"salesId\":\"" +storeEntity.getValue().getDestination().getSalesId()+"\","+
                        "\"sourceSystem\":\"" +storeEntity.getValue().getDestination().getSourceSystem()+
            "\" },"+
            "\"metadata\":{"+
                "\"priority\":\"" +storeEntity.getValue().getMetadata().getPriority()+"\","+
                        "\"timestamp\":\"" +storeEntity.getValue().getMetadata().getTimestamp()+
            "\"}"+

        "}"+
    "}";

    }

}
