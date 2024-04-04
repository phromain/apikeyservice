Endpoint : /apikey

## Docker
### Pour voir les réseaux

docker network ls


### Pour avoir des détails sur le réseau

docker network inspect nutri-network



### Pour creer Image Docker

Droite Maven -> Lifecycle

    clean

    package

ou

./mvnw clean

./mvnw package



### Pour créer l'image:

docker build -f src/main/docker/Dockerfile.jvm -t romainph/apikey-api .

Vous pouvez recuperer cette commande dans docker/Dockerfile.jvm , il faudra mettre le nom par nom_repo_docker/nom_a_donner_image . (le point c est pour indiquer qu'on est dans le dossier)



### Cette Commande creer un container docker et l ajoute au réseau en y ajoutant un alias

docker run -i --name apikey-api  --network nutri-network --network-alias apikey -p 8081:8080 romainph/apikey-api

docker run -i --name NOM_CONTAINER --network NOM_RESEAU --network-alias ALIAS(dans le reseau du container) -p PORT_DEV:PORT_PROD NOM_IMAGE .


# apikeyservice

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/apikeyservice-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Related Guides

- JDBC Driver - Microsoft SQL Server ([guide](https://quarkus.io/guides/datasource)): Connect to the Microsoft SQL
  Server database via JDBC
- SmallRye OpenAPI ([guide](https://quarkus.io/guides/openapi-swaggerui)): Document your REST APIs with OpenAPI - comes
  with Swagger UI
- RESTEasy Classic ([guide](https://quarkus.io/guides/resteasy)): REST endpoint framework implementing Jakarta REST and
  more

## Provided Code

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)

### RESTEasy JAX-RS

Easily start your RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started#the-jax-rs-resources)
