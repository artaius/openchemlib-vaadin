# OpenChemLib Vaadin 
[![](https://github.com/artaius/openchemlib-vaadin/actions/workflows/maven.yml/badge.svg?branch=release)](https://github.com/artaius/openchemlib-vaadin/actions)
[![](https://img.shields.io/nexus/r/ch.artaios/openchemlib-vaadin?server=https%3A%2F%2Fs01.oss.sonatype.org)](https://central.sonatype.com/artifact/ch.artaios/openchemlib-vaadin)

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

## Development
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


## Working Notes
### Zakodium
- Query features dialog is missing (fragment mode on, double-click on atom with lasso tool).
- Synchronize attributes to properties.
- Change events (from users) must be reflected in idcode property.

### Idorsia
- Enhance clipboard handling (evtl. also dd_native)
- Coordinates are not yet handled.
- Consolidate API (incl. constructors) of OpenChemLibEditor.java and StructureEditor.
- StructureEditorDialog needs to be adapted.
- Still use StructureView or create new Component using SVGRenderer?

### OCL hacks
Following changes are directly applied to the node module sources in ```node_modules/openchemlib/lib/canvas_editor```!
To make those changes active in dev mode, delete ```src/main/bundles/dev.bundle``` initiate ```mvn clean``` & rerun the project or apply changes directly in ```target/dev-bundle/webapp/VAADIN/build/generated-flow-imports-***.js```.
**This also needs to be done in dependant projects as also there the sources are downloaded directly from npm!!!**

- Comment line 85 (```if (this.#isReadOnly) return;```) in ```node_modules/openchemlib/lib/canvas_editor/init/canvas_editor.js```. 
- Comment line 123 (```if (this.#isReadOnly) return;```) in ```node_modules/openchemlib/lib/canvas_editor/init/canvas_editor.js```. 
