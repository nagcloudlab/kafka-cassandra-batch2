package org.example;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class KafkaConsumerClient {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerClient.class);

    public static void main(String[] args) {

        Properties properties = new Properties();

        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, "consumer-client");

        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-group-3");
        properties.put(ConsumerConfig.GROUP_INSTANCE_ID_CONFIG, "consumer-instance-1");

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        properties.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, CooperativeStickyAssignor.class.getName());



        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        consumer.subscribe(List.of("greetings"));

        // get a reference to the current thread
        final Thread mainThread = Thread.currentThread();
        // adding the shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Detected a shutdown, let's exit by calling consumer.wakeup()...");
            consumer.wakeup();
            // join the main thread to allow the execution of the code in the main thread
            try {
                mainThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));

        try {
            while (true) {
//                 System.out.println("Polling");
                ConsumerRecords<String, String> consumerRecords = consumer.poll(1000); // 100
                System.out.println("Received " + consumerRecords.count() + " records");
                Map<TopicPartition, OffsetAndMetadata> currentProcessedOffsets = new HashMap<>();
                for (var record : consumerRecords) {
//                    logger.info("Received new record\nTopic: {}\nKey: {}\nValue: {}\nPartition: {}\nOffset: {}\nTimestamp: {}",
//                            record.topic(),
//                            record.key(),
//                            record.value(),
//                            record.partition(),
//                            record.offset(),
//                            record.timestamp());
                    currentProcessedOffsets.put(new TopicPartition(record.topic(), record.partition()), new OffsetAndMetadata(record.offset() + 1));
                }
                consumer.commitSync(currentProcessedOffsets);
            }

        } catch (WakeupException e) {
            System.out.println("Wake up exception! " + e);
        } catch (Exception e) {
            System.out.println("Unexpected exception " + e);
        } finally {
            consumer.close(); // Leaving Request to Group Coordinator
            System.out.println("The consumer is now gracefully closed");
        }
    }

}
