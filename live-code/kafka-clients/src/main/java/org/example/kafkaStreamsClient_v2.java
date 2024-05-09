package org.example;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;

public class kafkaStreamsClient_v2 {
    public static void main(String[] args) {

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-pipe");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");    // assuming that the Kafka broker this application is talking to runs on local machine with port 9092
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 3); // 3 threads for processing (default is 1 thread

        final StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> source = builder.stream("numbers");

        source.filter((key, value) -> Integer.parseInt(value) % 2 == 0)
                .to("even-numbers");
        source.filter((key, value) -> Integer.parseInt(value) % 2 != 0)
                .to("odd-numbers");

        Topology topology= builder.build();
        final KafkaStreams streams = new KafkaStreams(topology, props);

        streams.start();

    }
}
