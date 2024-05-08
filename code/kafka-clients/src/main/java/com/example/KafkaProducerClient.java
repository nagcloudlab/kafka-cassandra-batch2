package com.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class KafkaProducerClient {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerClient.class);

    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "producer-client");
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,CustomPartitioner.class.getName());

        properties.put(ProducerConfig.ACKS_CONFIG, "0");
        properties.put(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
        properties.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 100);
        properties.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 120000);
        // properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,"true");
        properties.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);

        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 20);
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32 * 1024)); // 32 KB batch size

        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, Integer.toString(33554432)); // 32 MB buffer size
        properties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 60000);
        properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000);
        // properties.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG,CustomProducerInterceptor.class.getName());

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {
            String topic = "greetings";
            List<String> languages = List.of("en", "es", "fr");
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                // random key
                String key = languages.get(i % languages.size());
                String value = "Hey Kafka!".repeat(100); // 1kb message
                ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
                producer.send(record, (recordMetadata, exception) -> {
                    if (exception == null) {
                        logger.info(
                                "Received new metadata \nTopic: {}\nKey: {}\nPartition: {}\nOffset: {}\nTimestamp: {}",
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}