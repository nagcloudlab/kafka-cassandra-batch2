deploy file source connector

```bash
curl -X POST http://localhost:8083/connectors -H "Content-Type: application/json" -d '{
    "name": "file-source-connector",
    "config": {
        "connector.class": "org.apache.kafka.connect.file.FileStreamSourceConnector",
        "tasks.max": "1",
        "file": "my-file.txt",
        "topic": "my-topic"
    }
}'
```

deploy file sink connector

```bash
curl -X POST http://localhost:8083/connectors -H "Content-Type: application/json" -d '{
    "name": "file-sink-connector",
    "config": {
        "connector.class": "org.apache.kafka.connect.file.FileStreamSinkConnector",
        "tasks.max": "1",
        "file": "your-file.txt",
        "topics": "my-topic"
    }
}'
```
