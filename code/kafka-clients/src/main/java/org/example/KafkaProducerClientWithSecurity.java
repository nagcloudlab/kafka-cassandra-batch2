package org.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class KafkaProducerClientWithSecurity {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerClient.class);

    public static void main(String[] args) {

        Properties props = new Properties();

        // Client ID
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "producer-client-1");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9093");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        props.put("security.protocol", "SSL");
        props.put("ssl.protocol", "TLSv1.2");
        props.put("ssl.truststore.location",
                "/Users/nag/kafka-cassandra-batch2/security/kafka.producer.truststore.jks");
        props.put("ssl.truststore.password", "kafka1");
        props.put("ssl.keystore.location",
                "/Users/nag/kafka-cassandra-batch2/security/kafka.producer.keystore.jks");
        props.put("ssl.keystore.password", "kafka1");
        props.put("ssl.key.password", "kafka1");


        // Create a KafkaProducer instance
        try(KafkaProducer<String,String> producer=new KafkaProducer<>(props)){
            String topic="finance";
            for (int i = 0; i < 1; i++) {
                String value = "Hello Kafka!";
                ProducerRecord<String, String> record = new ProducerRecord<>(topic, value);
                producer.send(record, (recordMetadata, exception) -> {
                    if (exception == null) {
                        logger.info("Received new metadata \nTopic: {}\nKey: {}\nPartition: {}\nOffset: {}\nTimestamp: {}",
                                recordMetadata.topic(),
                                recordMetadata.partition(),
                                recordMetadata.offset(),
                                recordMetadata.timestamp());
                    } else {
                        logger.error("Error while producing: {}", exception.getMessage());
                    }
                });
                TimeUnit.SECONDS.sleep(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}