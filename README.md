# OpenChemLib Vaadin 
Vaadin Java integration of [OpenChemLib JS](https://github.com/cheminfo/openchemlib-js) components (which in turn is the JavaScript port of [OpenChemLib](https://github.com/Actelion/openchemlib)).

## Development instructions
### Starting the test/demo server
1. Run `mvn jetty:run`.
2. Open http://localhost:8080 in the browser.

### Building the production version 
To build production version run:
```bash
mvn clean
mvn vaadin:clean-frontend
mvn install -Pproduction
```

### Usage
Include dependency in your project:
```xml
<dependency>
    <groupId>ch.artaios</groupId>
    <artifactId>openchemlib-vaadin</artifactId>
    <version>1.0.0</version>
</dependency>
```

To be able to properly run in development mode, don't forget to add package ch.artaios to ```src/main/resources/application.properties``` like follows:
```properties
vaadin.whitelisted-packages = com.vaadin,org.vaadin,dev.hilla,ch.artaios
```
Otherwise, in development mode element would be empty!
