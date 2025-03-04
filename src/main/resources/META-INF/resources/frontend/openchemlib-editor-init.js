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
            if(e.dataTransfer != null){
                const data = e.dataTransfer.getData("text/plain");
                if(this.mode ===  CanvasEditorElement.MODE.MOLECULE)
                    this.idcode = anyMolStringToIdCode(data);
                else
                    this.idcode = anyRxnStringToIdCode(data);
            }
        }

        this.addEventListener('change', function(e) {
            if (e.detail.type === 'selection') {
                var selAtoms;
                if (this.mode === CanvasEditorElement.MODE.MOLECULE) {
                    // Handle single molecules
                    var mol = this.getMolecule();
                    if (!mol) {
                        return;
                    }
                    selAtoms = [];
                    for (var i = 0, n = mol.getAllAtoms(); i < n; i++) {
                        if (mol.isSelectedAtom(i)) {
                            selAtoms.push(i);
                        }
                    }
                } else if (this.mode === CanvasEditorElement.MODE.REACTION) {
                    // Handle reactions
                    var reaction = this.getReaction();
                    if (!reaction) {
                        return;
                    }
                    selAtoms = { reactants: [], products: [] };
                    var reactants = reaction.getReactants();
                    var products = reaction.getProducts();
                    if (reactants) {
                        for (var r = 0; r < reactants; r++) {
                            var mol = reaction.getReactant(r);
                            var sel = [];
                            for (var i = 0, n = mol.getAllAtoms(); i < n; i++) {
                                if (mol.isSelectedAtom(i)) {
                                    sel.push(i);
                                }
                            }
                            selAtoms.reactants.push(sel);
                        }
                    }
                    if (products) {
                        for (var p = 0; p < products; p++) {
                            var mol = reaction.getProduct(p);
                            var sel = [];
                            for (var i = 0, n = mol.getAllAtoms(); i < n; i++) {
                                if (mol.isSelectedAtom(i)) {
                                    sel.push(i);
                                }
                            }
                            selAtoms.products.push(sel);
                        }
                    }
                }
                this.dispatchEvent(new CustomEvent('ocl-selectionchange', {
                    detail: {
                        type: e.detail.type,
                        isUserEvent: e.detail.isUserEvent,
                        mode: this.mode,
                        selectedAtoms: JSON.stringify(selAtoms)
                    },
                    bubbles: true,
                    composed: true
                }));
            }
        });
    }

    CanvasEditorElement.prototype.copy=function() {
        navigator.clipboard.writeText(this.idcode); //.then(r => console.info('idcode copied'));
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
            if (this.mode === CanvasEditorElement.MODE.MOLECULE)
                this.idcode = anyMolStringToIdCode(data);
            else
                this.idcode = anyRxnStringToIdCode(data);
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





