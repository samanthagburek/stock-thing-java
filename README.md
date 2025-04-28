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

## Update: Application dockerized.
style follows that of the official sample project, consisting of:
- [the sample repo](https://github.com/spring-projects/spring-petclinic)
- [dockerisation guide](https://docs.docker.com/guides/java/containerize/)
to run the application, now simplly:
```bash
docker compose up --build
```
When future dependency changes, only need to modify the Dockerfile 