package com.store.store.service;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;

@IntegrationComponentScan
@Configuration
public class MqttConfig {

    @Bean
    public MqttPahoClientFactory mqttClientFactory(){

        DefaultMqttPahoClientFactory factory= new DefaultMqttPahoClientFactory();
        MqttConnectOptions options= new MqttConnectOptions();
        options.setServerURIs(new String[] {"tcp://localhost:1883"});
        options.setUserName("admin");
        String pass="12345678";
        options.setPassword(pass.toCharArray());

        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    public MessageChannel mqttOutboundChannel(){
        return new DirectChannel();
    }

    public MqttPahoMessageHandler mqttOutbound(){
        MqttPahoMessageHandler messageHandler=new MqttPahoMessageHandler("mqttClient", mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic("mystore");
        return messageHandler;

    }

}
