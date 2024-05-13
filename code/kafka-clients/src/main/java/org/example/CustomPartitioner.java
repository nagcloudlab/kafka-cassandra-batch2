package org.example;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

public class CustomPartitioner implements Partitioner {

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        // Assume the key is not null and is a String
        int numPartitions = cluster.partitionsForTopic(topic).size();

        // Simple hash-based partitioning
        int partition = key.hashCode() % numPartitions;
        if (partition < 0) {
            partition = partition + numPartitions; // Make sure the partition number is non-negative
        }

        return partition;
    }

    @Override
    public void close() {
    }

    @Override
    public void configure(Map<String, ?> configs) {
    }
}
