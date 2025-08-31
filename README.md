# Date Field Router - SMT

## English

This project implements a Single Message Transform (SMT) for Kafka Connect that routes events based on the value of a 
date field present in each record's value.

### Parameters

- `transforms.fieldRouter.field.name`: Name of the field. It's value must be parsable as a date.
- `transforms.fieldRouter.source.date.format`: Date pattern used to parse the source field value.
- `transforms.fieldRouter.dest.date.format`: Date pattern used to format the parsed date into the target string.
- `transforms.fieldRouter.dest.topic.format`: Template used to build the destination topic name. It may include the 
`topic` and `field` variables, which will be replaced with the original topic name and the formatted date string, 
respectively.

### Usage

Package the project with

`mvn package`

Copy the resulting `with-dependencies` jar in the `plugin.path` directory of your Kafka Connect worker.

Configure your connector to use the SMT.

### Use example

```json
"transforms": "fieldRouter",
"transforms.fieldRouter.type": "org.lautaropastorino.poc.FieldRouter",
"transforms.fieldRouter.field.name": "authorizationDate",
"transforms.fieldRouter.source.date.format": "yyyy-MM-dd'T'HH:mm:ss.S",
"transforms.fieldRouter.dest.date.format": "yyyy-MM",
"transforms.fieldRouter.dest.topic.format": "${topic}-${field}"
```

## Español

Este proyecto implementa un Single Message Transform (SMT) para ser utilizado en un conector de Kafka Connect. El 
objetivo de este SMT es modificar el tópico destino según el valor de un campo de tipo `date` del `value` de 
cada evento.

### Parámetros

- `transforms.fieldRouter.field.name`: Nombre del campo a utilizar. Debe existir en el `value` del evento y debe poder 
ser parseado como una fecha.
- `transforms.fieldRouter.source.date.format`: Patrón a utilizar para parsear el campo de tipo fecha.
- `transforms.fieldRouter.dest.date.format`: Patrón con el cual formatear la fecha en el destino.
- `transforms.fieldRouter.dest.topic.format`: Patrón para construir el tópico destino. Puede incluir las variables 
`topic` y `field` que serán reemplazadas por los valores obtenidos.

### Modo de uso

Ejecutar el comando 

`mvn package`

Incluir el jar `with-dependencies` generado en el `plugin.path` de Kafka Connect.

Incluir la config del SMT en la creación del conector.

### Ejemplo de uso

```json
"transforms": "fieldRouter",
"transforms.fieldRouter.type": "org.lautaropastorino.poc.FieldRouter",
"transforms.fieldRouter.field.name": "authorizationDate",
"transforms.fieldRouter.source.date.format": "yyyy-MM-dd'T'HH:mm:ss.S",
"transforms.fieldRouter.dest.date.format": "yyyy-MM",
"transforms.fieldRouter.dest.topic.format": "${topic}-${field}"
```