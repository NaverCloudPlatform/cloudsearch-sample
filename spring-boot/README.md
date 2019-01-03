# Cloud Search Sample

This sample project demonstrates how to use NCP CloudSearch service with [create-react-app](https://github.com/facebookincubator/create-react-app) and [spring-boot](https://github.com/spring-projects/spring-boot).

### How to run

For build in production environment:

```
./mvnw clean install
java -jar target/sample.cloudsearch-0.0.1-SNAPSHOT.jar
```

For Front-end develop:

```
./mvnw spring-boot:run
npm run start
```

### Usage

Update Keys : Access Key, Primary(Second) Key, Secret Key

[src/main/resources/application.yml](src/main/resources/application.yml)
