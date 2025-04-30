# Simple blockchain implementation

---
Build from sources
```bash
mvn clean install
```

Run node
```bash
java -jar cryprocurrency-0.1.jar --config config.json
```
Config file example
```json
{
  "key" : "your private key",
  "addresses" : [
    "other node address"
  ],
  "storage_path" : "app.db",
  "port" : 8080
}
```