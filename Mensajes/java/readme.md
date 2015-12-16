## Resumen

Este es un ejemplode envío de mensajes utilizando JMS y Java

## Prereqs

- Instalar Java SDK
- Instalar [Maven](http://maven.apache.org/download.html) 

## Building

Ejecutar:

    mvn install

## Ejecutando los ejemplos

En una terminal ejecutar:

    java -cp target/stomp-example-0.1-SNAPSHOT.jar example.Listener

En otra terminal ejecutar:

    java -cp target/stomp-example-0.1-SNAPSHOT.jar example.Publisher

La aplicación Listener estara escuchando los mensjaes enviados por Publisher
Para finalizar el envío de mensajes debes digitar SHUTDOWN

Puedes controlar la conexion al servidor cambiando las siguientes variables: 

* `APOLLO_HOST`
* `APOLLO_PORT`
* `APOLLO_USER`
* `APOLLO_PASSWORD`
