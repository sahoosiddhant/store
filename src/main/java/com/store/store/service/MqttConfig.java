package com.store.store.service;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

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
    @Qualifier("mqttInputChannel")
    @Primary
    public MessageChannel mqttInputChannel(){
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound(){
        MqttPahoMessageDrivenChannelAdapter adapter= new MqttPahoMessageDrivenChannelAdapter("serverIn",mqttClientFactory());
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(2);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler messageHandler(){
        return message -> {
            String payload=(String) message.getPayload().toString();
            System.out.println("Message sent to Mosquitto ");
        };
    }

    @Bean
    @Qualifier("mqttOutboundChannel")
    public MessageChannel mqttOutboundChannel(){
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MqttPahoMessageHandler mqttOutbound(){
        MqttPahoMessageHandler messageHandler=new MqttPahoMessageHandler("serverOut", mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic("mystore");
        return messageHandler;

    }

}
