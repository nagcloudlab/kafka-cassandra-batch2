package org.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

public class ProducerKafkaClient {

    private static final Logger logger = LoggerFactory.getLogger(ProducerKafkaClient.class);

    public static void main(String[] args) {

        Properties properties=new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092,localhost:9093,localhost:9094");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        //properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,CustomPartitioner.class.getName());
        properties.put(ProducerConfig.ACKS_CONFIG,"all");
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,"true");

        try(KafkaProducer<String,String> producer=new KafkaProducer<>(properties)){

            String topic="greetings";
            List<String> languages=List.of("en","es","fr");

            for (int i = 0; i < 1; i++) {
                // random key
                String key = languages.get(i % languages.size());
                String value = "Hey Kafka!".repeat(100); // 1kb message
                ProducerRecord<String, String> record = new ProducerRecord<>(topic,1,key, value);
                producer.send(record, (recordMetadata, exception) -> {
                    if (exception == null) {
                        logger.info("Received new metadata \nTopic: {}\nKey: {}\nPartition: {}\nOffset: {}\nTimestamp: {}",
                                recordMetadata.topic(),
                                key,
                                recordMetadata.partition(),
                                recordMetadata.offset(),
                                recordMetadata.timestamp());
                    } else {
                        logger.error("Error while producing: {}", exception.getMessage());
                    }
                });
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}