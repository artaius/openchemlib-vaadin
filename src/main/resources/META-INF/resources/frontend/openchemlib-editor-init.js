import OCL from 'openchemlib/full.pretty.js';

try {
    console.info("Registering OCL custom tag for Vaadin...");
    const CanvasEditorElement = OCL.registerCustomElement();
    console.info("Registered OCL custom tag for Vaadin.");
    console.info("Configuring OCL custom tag for Vaadin...");
    // const constr = CanvasEditorElement.prototype.constructor;

    CanvasEditorElement.prototype.init = function() {
        this.ondragstart = function(e) {
            if(e.dataTransfer != null) {
                e.dataTransfer.setData("text/plain", this.idcode);
                // e.dataTransfer.setData('text/html', '<h1>Foo bar</h1>');
                // e.dataTransfer.setData('text/uri-list', 'https://example.com');
            }
        }
        this.ondragover = function(e) {
            if(this.editable)
                e.preventDefault();
        }

        this.ondrop = function(e) {
            e.preventDefault();
            if(e.dataTransfer != null) {
                const data = e.dataTransfer.getData("text/plain");
                console.info("Dropping data: " + data);
                if(this.mode ===  CanvasEditorElement.MODE.MOLECULE) {
                    const value = anyMolStringToIdCode(data);
                    console.info("Dropped data: " + value);
                    // this.setAttribute('idcode', value);
                    this.idcode = value;
                } else {
                    const value = anyRxnStringToIdCode(data);
                    console.info("Dropped data: " + value);
                    // this.setAttribute('idcode', value);
                    this.idcode = value;
                }
                this.#handleChange({type: 'molecule', isUserEvent: true});
            }
        }
    }

    CanvasEditorElement.prototype.copy=function() {
        navigator.clipboard.writeText(this.getAttribute('idcode')); //.then(r => console.info('idcode copied'));
    }

    CanvasEditorElement.prototype.paste=function() {
        // TODO handle other content types?
        // for debugging: list available clipboard content
        // navigator.clipboard.read().then(clipboardItems => {
        //     console.info(clipboardItems.length + " clipboardItem");
        //     clipboardItems.forEach(item => {
        //         item.types.forEach(type => {
        //             item.getType(type).then(value => value.text().then(text => console.info("clipboardItem type: " + type + ": " + text)));
        //         })
        //     })
        // });
        navigator.clipboard.readText().then(data => {
            console.info("Pasting data: " + data);
            if(this.mode ===  CanvasEditorElement.MODE.MOLECULE)
            {
                const value = anyMolStringToIdCode(data);
                console.info("Pasted data: " + data);
                // this.setAttribute('idcode', value);
                this.idcode = value;
            } else {
                const value = anyRxnStringToIdCode(data);
                console.info("Pasted data: " + data);
                // this.setAttribute('idcode', value);
                this.idcode = value;
            }
        });
    };

    function anyMolStringToIdCode(string) {
        const [idcode, coordinates] = string.split(' ');

        let molecule = undefined;

        // check if the string contains line breaks -> a molfile
        if(string.match(/[\r\n]+/)) {
            // try molfile
            try{
                molecule = OCL.Molecule.fromMolfile(string)
            } catch (error) {
                console.warn(error);
            }
        } else {
            // try smiles
            try {
                molecule = OCL.Molecule.fromSmiles(string)
            } catch (error) {
                // try idcode
                try {
                    molecule = OCL.Molecule.fromIDCode(idcode, coordinates);
                    // directly return the input string if it is already an IDCode
                    return string;
                } catch (error) {
                    console.warn(error);
                }
            }
        }
        if (molecule!== undefined)
            return molecule.getIDCode() + " " + molecule.getIDCoordinates();
        else
            return (new OCL.Molecule()).getIDCode();
    }

    function anyRxnStringToIdCode(string) {
        try {
            const reaction = OCL.ReactionEncoder.decode(string);
            return string;
        } catch (error) {
            console.error(error);
        }
    }

    console.info("Configured OCL custom tag for Vaadin...");
} catch (error) {
    console.error(error);
}





