package org.example;

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

        // Create a Properties object to hold configuration settings
        Properties props = new Properties();

        // Client ID
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "producer-client-1");
        // List of Kafka brokers to connect to
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // Serializer class for key
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // Serializer class for value
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // Acknowledgments for message durability
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        // Enable idempotence to avoid message duplication
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        // Retry settings
        props.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 100);
        // Maximum number of in-flight requests per connection
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);
        // Batch size and buffer memory
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        // How long to wait before sending a batch in milliseconds
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        // Compression type
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        // Max request size
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 10485760);
        // Request timeout settings
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000);
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 120000);
        // Interceptor classes
        props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, CustomProducerInterceptor.class.getName());
        // Partitioner class
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomPartitioner.class.getName());

        // Create a KafkaProducer instance
        try(KafkaProducer<String,String> producer=new KafkaProducer<>(props)){
            String topic="greetings";
            List<String> languages=List.of("en","es","fr","de","it","pt","ru","zh","ja","ko");
            for (int i = 0; i < 1; i++) {
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
                TimeUnit.SECONDS.sleep(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}