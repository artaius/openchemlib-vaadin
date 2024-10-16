import OCL from 'openchemlib/full.pretty.js';
import {el} from "date-fns/locale";

try {
    console.info("Registering OCL custom tag for Vaadin...");
    const CanvasEditorElement = OCL.registerCustomElement();
    console.info("Registered OCL custom tag for Vaadin.");
    console.info("Configuring OCL custom tag for Vaadin...");
    // const constr = CanvasEditorElement.prototype.constructor;

    CanvasEditorElement.prototype.init = function() {
        console.warn('constructor');
        console.info(arguments);

        // constr.apply(this, arguments);

        console.info('constructor');
        this.draggable = true;
        this.ondragstart = function(e) {
            if(e.dataTransfer != null) {
                e.dataTransfer.setData("text/plain", this.idcode);
                // e.dataTransfer.setData('text/html', '<h1>Foo bar</h1>');
                // e.dataTransfer.setData('text/uri-list', 'https://example.com');
            }
        }
        this.ondragover = function(e) {
            if(!this.readonly)
                e.preventDefault();
        }

        this.ondrop = function(e) {
            e.preventDefault();
            if(e.dataTransfer != null)
                {
                    var data = e.dataTransfer.getData("text/plain");
                    this.idcode = molStringToIdCode(data);
                }
        }
    }

    CanvasEditorElement.prototype.paste=function() {
        // TODO handle other content types?
        // for debugging: list available clipboard content
        // navigator.clipboard.read().then(clipboardItems => {
        //     console.warn(clipboardItems.length + " clipboardItem");
        //     clipboardItems.forEach(item => {
        //         item.types.forEach(type => {
        //             item.getType(type).then(value => value.text().then(text => console.warn("clipboardItem type: " + type + ": " + text)));
        //         })
        //     })
        // });
        navigator.clipboard.readText().then(data => {
            this.setAttribute('idcode', molStringToIdCode(data));
            console.warn('idcode pasted');
        });
    };

    function molStringToIdCode(string) {
        const [idcode, coordinates] = this.idcode.split(' ');

        let molecule = OCL.Molecule.fromIDCode(idcode, coordinates);
        if(molecule!==undefined)
            return string;
        else
            molecule = OCL.Molecule.fromSmiles(string)

        if(molecule===undefined)
            molecule = OCL.Molecule.fromMolfile(string)

        if (molecule!== undefined)
            return molecule.getIDCode + " " + molecule.getIDCoordinates();
        else
            return (new OCL.Molecule()).getIDCode
    }

    console.info("Configured OCL custom tag for Vaadin...");
} catch (error) {
    console.error(error);
}





