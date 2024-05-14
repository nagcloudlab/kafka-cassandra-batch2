```json
{
  "type": "record",
  "name": "Purchase",
  "namespace": "io.confluent.developer.avro",
  "fields": [
    {
      "name": "item",
      "type": {
        "type": "string",
        "avro.java.string": "String"
      }
    },
    {
      "name": "total_cost",
      "type": "double"
    },
    {
      "name": "customer_id",
      "type": {
        "type": "string",
        "avro.java.string": "String"
      }
    }
  ]
}
```

register above schema in schema registry by curl command

```bash
curl -X POST -H "Content-Type: application/json" \
  --data '{"schema": "{\"type\":\"record\",\"name\":\"Purchase\",\"namespace\":\"io.confluent.developer.avro\",\"fields\":[{\"name\":\"item\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"total_cost\",\"type\":\"double\"},{\"name\":\"customer_id\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}]}"}' \
  http://localhost:8081/subjects/purchases-value/versions
```

evolve the schema by adding a new field `purchase_date` of type `string` to the schema

```json
{
  "type": "record",
  "name": "Purchase",
  "namespace": "io.confluent.developer.avro",
  "fields": [
    {
      "name": "item",
      "type": {
        "type": "string",
        "avro.java.string": "String"
      }
    },
    {
      "name": "total_cost",
      "type": "double"
    },
    {
      "name": "customer_id",
      "type": {
        "type": "string",
        "avro.java.string": "String"
      }
    },
    {
      "name": "purchase_date",
      "type": {
        "type": "string",
        "avro.java.string": "String"
      }
    }
  ]
}
```

register the evolved schema in schema registry by curl command

```bash
curl -X POST -H "Content-Type: application/json" \
  --data '{"schema": "{\"type\":\"record\",\"name\":\"Purchase\",\"namespace\":\"io.confluent.developer.avro\",\"fields\":[{\"name\":\"item\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"total_cost\",\"type\":\"double\"},{\"name\":\"customer_id\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"purchase_date\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}]}"}' \
  http://localhost:8081/subjects/purchases-value/versions
```

evolve the schema by adding a new field `purchase_date` of type `string` to the schema and setting a default value for the field

```json
{
  "type": "record",
  "name": "Purchase",
  "namespace": "io.confluent.developer.avro",
  "fields": [
    {
      "name": "item",
      "type": {
        "type": "string",
        "avro.java.string": "String"
      }
    },
    {
      "name": "total_cost",
      "type": "double"
    },
    {
      "name": "customer_id",
      "type": {
        "type": "string",
        "avro.java.string": "String"
      }
    },
    {
      "name": "purchase_date",
      "type": {
        "type": "string",
        "avro.java.string": "String"
      },
      "default": "2021-01-01"
    }
  ]
}
```

register the evolved schema in schema registry by curl command

```bash
curl -X POST -H "Content-Type: application/json" \
  --data '{"schema": "{\"type\":\"record\",\"name\":\"Purchase\",\"namespace\":\"io.confluent.developer.avro\",\"fields\":[{\"name\":\"item\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"total_cost\",\"type\":\"double\"},{\"name\":\"customer_id\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"purchase_date\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"default\":\"2021-01-01\"}]}"}' \
  http://localhost:8081/subjects/purchases-value/versions
```
