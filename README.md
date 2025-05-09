NIDS network intrusion detection system

To run:
First start backend with ./mvnw spring-boot:run
Then run frontend with ./mvnw clean javafx:run

the frontend code rn is setup within src/main/java/com/example/stockthingjava/frontend
and is driven by StockThingJavaFXApp.java 
the repo will be maybe restructured later for cleanliness, readability and make it easier
to build from

rn a working endpoint to test is /api/rulesimulate/attack which stimulates a port scan attack which 
is currently a rule in the db 

## setup guide
first, Maven build and generate the .JAR file:
```bash
./mvnw clean package -DskipTests
```

then, use docker to launch the backend support:
```bash
sudo docker compose up --build
```
once the containers are built once, in the future, the faster launch command is
```bash
sudo docker compose up
```

## Update: Application dockerized.
style follows that of the official sample project, consisting of:
- [the sample repo](https://github.com/spring-projects/spring-petclinic)
- [dockerisation guide](https://docs.docker.com/guides/java/containerize/)
to run the application, now simplly:
```bash
docker compose up --build
```
When future dependency changes, only need to modify the Dockerfile

## Sample DB entry:
```bash
curl -X POST http://localhost:8080/api/rules \
    -H "Content-Type: application/json" \
    -d '{
        "description": "dummy description",
        "enabled": true,
        "name": "Rapid Port Scan",
        "rule_logic": "UNIQUE_DEST_PORTS",
        "threshold": 10,
        "time_window_seconds": 1
    }'

```

## Next step...
- cannot dockerize java GUI (maybe you can but that is kinda weird)
- I suggest migrating the GUI to web served. (That's more modern anyways)
- maybe create a separate frontend repo