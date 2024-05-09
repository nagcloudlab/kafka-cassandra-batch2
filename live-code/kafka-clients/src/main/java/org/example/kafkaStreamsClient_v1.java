package org.example;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.List;
import java.util.Properties;

public class kafkaStreamsClient_v1 {
    public static void main(String[] args) {


        Properties consumerProperties=new Properties();
        consumerProperties.put("bootstrap.servers","localhost:9092");
        consumerProperties.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        consumerProperties.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        consumerProperties.put("group.id","test-group");

        Properties producerProperties=new Properties();
        producerProperties.put("bootstrap.servers","localhost:9092");
        producerProperties.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        producerProperties.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");


        KafkaConsumer<String,String> consumer=new KafkaConsumer<String, String>(consumerProperties);
        consumer.subscribe(List.of("numbers"));

        KafkaProducer<String,String> producer=new KafkaProducer<String, String>(producerProperties);


        while(true){
            var records=consumer.poll(100);
            records.forEach(record->{
             String value=record.value();
                int number=Integer.parseInt(value);
                if(number%2==0) {
                    producer.send(new org.apache.kafka.clients.producer.ProducerRecord<String, String>("even-numbers", value));
                }
                else{
                    producer.send(new org.apache.kafka.clients.producer.ProducerRecord<String, String>("odd-numbers", value));
                }
            });
        }


    }
}
