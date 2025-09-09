import OCL from 'openchemlib';

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

        this.canonicalizeAtomIndices = function(mol, indices) {
            if (mol !== null && mol !== undefined) {
                console.log(OCL);
                const canonizer = new OCL.Canonizer(mol);
                const mapping = canonizer.getGraphIndexes();
                var mappedIndices = [];
                for (var i = 0; i < indices.length; i++) {
                    mappedIndices.push(mapping[indices[i]]);
                }
                return mappedIndices;
            } else {
                return indices;
            }
        }

        this.uncanonicalizeAtomIndices = function(mol, indices) {
            if (mol !== null && mol !== undefined) {
                const canonizer = new OCL.Canonizer(mol);
                const mapping = canonizer.getGraphIndexes();
                var mappedIndices = [];
                for (var i = 0; i < indices.length; i++) {
                    mappedIndices.push(mapping.indexOf(indices[i]));
                }
                return mappedIndices;
            } else {
                return indices;
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
                    selAtoms = this.canonicalizeAtomIndices(mol, selAtoms);
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
                            sel = this.canonicalizeAtomIndices(mol, sel);
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
                            sel = this.canonicalizeAtomIndices(mol, sel);
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

        this.isEmptyMolecule = function() {
            const mol = this.getMolecule();
            return mol === null || mol.getIDCode() === "d@";
        }

        this.getCorrectMolecule = function(reactantMolId=null, productMolId=null) {
            if (this.mode === CanvasEditorElement.MODE.MOLECULE) {
                return this.getMolecule();
            } else {
                if (reactantMolId !== null) {
                    return this.getReaction().getReactant(reactantMolId);
                } else if (productMolId !== null) {
                    return this.getReaction().getProduct(productMolId);
                } else {
                    return null;
                }
            }
        }

        this.setAtomColor = function(atom, color, reactantMolId=null, productMolId=null, canonicalOrdering=true) {
            const allowedColors = {
                0x000000: "None",
                0x000040: "Blue",
                0x000080: "Red",
                0x0000C0: "Green",
                0x000100: "Magenta",
                0x000140: "Orange",
                0x000180: "DarkGreen",
                0x0001C0: "DarkRed"
            };
            if (allowedColors[color] === undefined) {
                console.error("Color must be one of", allowedColors, ". Received", color, ".");
            } else {
                const molecule = this.getCorrectMolecule(reactantMolId, productMolId);
                if (canonicalOrdering) {
                    atom = this.uncanonicalizeAtomIndices(molecule, [atom])[0];
                }
                if (molecule !== undefined && molecule !== null && !this.isEmptyMolecule() && atom < molecule.getAtoms()) {
                    molecule.setAtomColor(atom, color);
                    this.moleculeChanged();
                }
            }
        }

        this.removeAtomColors = function(reactantMolId=null, productMolId=null) {
            const molecule = this.getCorrectMolecule(reactantMolId, productMolId);
            if (molecule !== undefined && molecule !== null && !this.isEmptyMolecule()) {
                molecule.removeAtomColors();
                this.moleculeChanged();
            }
        }

        this.highlightBondsBackground = function(bondIndices, reactantMolId=null, productMolId=null) {
            const mol = this.getCorrectMolecule(reactantMolId, productMolId);
            if (mol !== undefined && mol !== null && !this.isEmptyMolecule()) {
                for (let i = 0; i < bondIndices.length; i++) {
                    mol.setBondBackgroundHiliting(bondIndices[i], true);
                }
                this.moleculeChanged();
            }
        };

        this.highlightBondsForeground = function(bondIndices, reactantMolId=null, productMolId=null) {
            const mol = this.getCorrectMolecule(reactantMolId, productMolId);
            if (mol !== undefined && mol !== null && !this.isEmptyMolecule()) {
                for (let i = 0; i < bondIndices.length; i++) {
                    mol.setBondForegroundHiliting(bondIndices[i], true);
                }
                this.moleculeChanged();
            }
        };

        this.clearHighlights = function(reactantMolId=null, productMolId=null) {
            const mol = this.getCorrectMolecule(reactantMolId, productMolId);
            if (mol !== undefined && mol !== null && !this.isEmptyMolecule()) {
                mol.removeBondHiliting();
                this.moleculeChanged();
            }
        };

        this.setAtomCustomLabel = function(atom, label, canonicalOrdering=true) {
            if (atom !== null) {
                const mol = this.getMolecule();
                if (canonicalOrdering) {
                    atom = this.uncanonicalizeAtomIndices(mol, [atom])[0];
                }
                if (mol !== undefined && mol !== null && !this.isEmptyMolecule()) {
                    mol.setAtomCustomLabel(atom, label);
                    this.moleculeChanged();
                }
            }
        };
    }

    CanvasEditorElement.prototype.copy=function() {
        navigator.clipboard.writeText(this.idcode); //.then(r => console.info('idcode copied'));
    }

    CanvasEditorElement.prototype.copySmiles=function() {
        if (this.mode === CanvasEditorElement.MODE.MOLECULE)
            navigator.clipboard.writeText(this.getMolecule().toSmiles());
        else
            navigator.clipboard.writeText(this.getReaction().toSmiles());
    }

    CanvasEditorElement.prototype.copyMolfile=function() {
        if (this.mode === CanvasEditorElement.MODE.MOLECULE)
            navigator.clipboard.writeText(this.getMolecule().toMolfile());
    }

    CanvasEditorElement.prototype.copyMolfileV3=function() {
        if (this.mode === CanvasEditorElement.MODE.MOLECULE)
            navigator.clipboard.writeText(this.getMolecule().toMolfileV3());
    }

    CanvasEditorElement.prototype.copyRxn=function() {
        if (this.mode === CanvasEditorElement.MODE.REACTION)
            navigator.clipboard.writeText(this.getReaction().toRxn());
    }

    CanvasEditorElement.prototype.copyRxnV3=function() {
        if (this.mode === CanvasEditorElement.MODE.REACTION)
            navigator.clipboard.writeText(this.getReaction().toRxnV3());
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





