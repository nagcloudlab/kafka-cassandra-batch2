package com.example;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.kafka.common.serialization.StringSerializer;

public class ProducerClientApp {

    private static final Logger logger = LoggerFactory.getLogger(ProducerClientApp.class);

    public static void main(String[] args) {

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
            String topic = "greetings";
            String value = "Hey Kafka!".repeat(100);
            for (int i = 0; i < 32; i++) {
                ProducerRecord<String, String> record1 = new ProducerRecord<>(topic, value);
                logger.info("Sending message-{}{}", i, ">".repeat(20));
                producer.send(record1, (recordMetadata, exception) -> {
                    if (exception == null) {
                        logger.info("<".repeat(20));
                        logger.info("Received new metadata \nTopic: {}\nPartition: {}\nOffset: {}\nTimestamp: {}",
                                recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(),
                                recordMetadata.timestamp());
                    } else {
                        System.out.println("Error while producing: " + exception);
                    }
                });
            }
        } catch (Exception e) {

        }
    }
}