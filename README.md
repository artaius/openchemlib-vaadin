# OpenChemLib Vaadin 
Vaadin Java integration of the [OpenChemLib JS](https://github.com/cheminfo/openchemlib-js) components ([OpenChemLib JS](https://github.com/cheminfo/openchemlib-js) is the JavaScript port of [OpenChemLib](https://github.com/Actelion/openchemlib)).

![StructureView](resources/view.png "StructureView")
![StructureView](resources/editor.png "StructureEditor")

## Usage
Grab the precompiled jar file(s) from [Releases](https://github.com/artaius/openchemlib-vaadin/releases/latest) or
add the following dependency to your project:
```xml
<dependency>
    <groupId>ch.artaios</groupId>
    <artifactId>openchemlib-vaadin</artifactId>
    <version>1.0.1</version>
</dependency>
```

To be able to properly run in development mode, don't forget to add package ```ch.artaios``` to ```src/main/resources/application.properties``` like follows:
```properties
vaadin.whitelisted-packages = com.vaadin,org.vaadin,dev.hilla,ch.artaios
```

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

