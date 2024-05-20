


kafka setup on linux
--------------------------------------------

download kafka-3.7.0

```bash
wget https://downloads.apache.org/kafka/3.7.0/kafka_2.13-3.7.0.tgz
tar -xvzf kafka_2.13-3.7.0.tgz
cd kafka_2.13-3.7.0
```

zookeeper & kafka server setup
--------------------------------------------


start zookeeper

```bash
bin/zookeeper-server-start.sh config/zookeeper.properties
```

start kafka server

```bash
bin/kafka-server-start.sh config/server.properties
```


start kafka server-1 properties file

```bash
cp config/server.properties config/server-1.properties
vi config/server-1.properties
```

```properties
broker.id=101
listeners=PLAINTEXT://:9092
log.dirs=/tmp/kafka-logs-1
```

```bash
bin/kafka-server-start.sh config/server-1.properties
```

start kafka server-2 properties file

```bash
cp config/server.properties config/server-2.properties
vi config/server-2.properties
```

```properties
broker.id=102
listeners=PLAINTEXT://:9093
log.dirs=/tmp/kafka-logs-2
```

```bash
bin/kafka-server-start.sh config/server-2.properties
```


start kafka server-3 properties file

```bash
cp config/server.properties config/server-3.properties
vi config/server-3.properties
```

```properties
broker.id=103
listeners=PLAINTEXT://:9094
log.dirs=/tmp/kafka-logs-3
```

```bash
bin/kafka-server-start.sh config/server-3.properties
```


kafka-ui
--------------------------------------------
```bash
cd kafka_ui
```
refer README.md file for setup



topic management
--------------------------------------------

```bash
bin/kafka-topics.sh --create --topic test-topic --bootstrap-server localhost:9092 --partition 1
```

list topic

```bash
bin/kafka-topics.sh --list --bootstrap-server localhost:9092
```

describe topic

```bash
bin/kafka-topics.sh --describe --topic test-topic --bootstrap-server localhost:9092
```

delete topic

```bash
bin/kafka-topics.sh --delete --topic test-topic --bootstrap-server localhost:9092
```

console producer & consumer
--------------------------------------------

producer

```bash
bin/kafka-console-producer.sh --topic test-topic --bootstrap-server localhost:9092
```

consumer

```bash
bin/kafka-console-consumer.sh --topic test-topic --bootstrap-server localhost:9092
```

producer with key

```bash 
bin/kafka-console-producer.sh --topic test-topic --bootstrap-server localhost:9092 --property "parse.key=true" --property "key.separator=:"
```

consumer with key

```bash
bin/kafka-console-consumer.sh --topic test-topic --bootstrap-server localhost:9092 --from-beginning --property "print.key=true" --property "key.separator=:"
```



consumer group
--------------------------------------------

```bash
bin/kafka-console-consumer.sh --topic test-topic --bootstrap-server localhost:9092 --group test-group
```

```bash
bin/kafka-console-consumer.sh --topic test-topic --bootstrap-server localhost:9092 --group test-group --from-beginning
```

```bash
bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --list
```

```bash
bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group test-group --reset-offsets --to-earliest --execute --topic test-topic    
```

```bash
bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group test-group --reset-offsets --shift-by -2 --execute --topic test-topic
```

```bash
bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group test-group --reset-offsets --to-latest --execute --topic test-topic
```

```bash
bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group test-group --reset-offsets --to-offset 5 --execute --topic test-topic
```