import {html, LitElement, PropertyValues, TemplateResult} from 'lit';
import {property, customElement} from 'lit/decorators.js';
import OCL from 'openchemlib/full.pretty.js';

@customElement('structure-editor')
export class StructureEditor extends LitElement {
    @property({type: String, reflect: true})
    idcode: string | null = "";
    @property({type: Boolean, reflect: true})
    viewonly: boolean = false;
    @property({type: Boolean, reflect: true})
    fragment: boolean = true;
    @property({type: Boolean, reflect: true})
    showidcode: boolean = false;

    private static activeEditor: any;
    private static id: number = 0;

    private structureEditor!: any;

    constructor() {
        super();
        this.log('constructor');
    }

    // Don't create shadow DOM (because OCL initialization won't work)
    createRenderRoot(): HTMLElement | DocumentFragment {
        return this;
    }

    connectedCallback() {
        super.connectedCallback();
        if(!this.getAttribute('id'))
            this.setAttribute("id", this.tagName + "_" + (++StructureEditor.id));
        this.log('connectedCallback');
        this.init();
    }

    // when lazy loading vaadin component div element still is null on connectedCallback, therefore aditional init here
    protected firstUpdated(_changedProperties: PropertyValues) {
        super.firstUpdated(_changedProperties);
        this.log('firstUpdated');
        if(this.structureEditor==null)
            this.init();
    }

    private init() {
        let div = this.renderRoot.querySelector('div');
        if (div) {
            this.log('before create structureEditor');
            this.structureEditor = new OCL.StructureEditor(div, true, 1);
            // this.structureEditor = OCL.StructureEditor.createEditor(div.getAttribute("id"));
            this.log('after create structureEditor ok:' + this.idcode);
            if (this.idcode) {
                this._setActiveEditor();
                let idcodeCorrected = this._handleFragmentMode(this.idcode, this.fragment);
                this.idcode = idcodeCorrected;
                this.structureEditor.setIDCode(this.idcode);
            }
            this.structureEditor.setChangeListenerCallback(this._changeMolecule);
        }
    }

    protected updated(_changedProperties: PropertyValues) {
        super.updated(_changedProperties);
        this.log('updated');
    }

    /** Render the component.
     *
     * Add a template to your component to define what it should render.
     * Templates can include expressions, which are placeholders for dynamic content.
     *
     */
    render(): TemplateResult {
        this.log('render');
        return html`
            <div 
                @mouseenter="${this._setActiveEditor}"
                @dragenter="${this._setActiveEditor}"
                style="min-width: 400px; min-height: 400px; width: 100%; height: 100%;"
                view-only="${this.viewonly}"
                is-fragment="${this.fragment}"
                show-idcode="${this.showidcode}"
            ></div>
        `;
    }

    attributeChangedCallback(name: string, _old: string | null, value: string | null) {
        super.attributeChangedCallback(name, _old, value);
        this.log("attributeChangedCallback: " + name + ':' + _old + '->' + value);

        let idcodeCorrected = this._handleFragmentMode(this.idcode, this.fragment);

        // on idcode change (ignore coords only changes)
        if (this.structureEditor && name == "idcode" && (_old == null || value == null || _old.split(" ")[0] != value.split(" ")[0])) {
            // set activeEditor if writing from attribute change! (Potentially buggy, completely disable idcode setter?)
            this._setActiveEditor();
            this.idcode = idcodeCorrected;
            this.structureEditor.setIDCode(idcodeCorrected);
        }
        // on fragment change (ignore coords only changes)
        else if (this.structureEditor && name == "fragment") {
            this._setActiveEditor();
            this.idcode = idcodeCorrected;
            this.structureEditor.setIDCode(idcodeCorrected);
        }
    }

    _setActiveEditor() {
        this.log('setActiveEditor');
        StructureEditor.activeEditor = this;
    }

    // In the following method "this" is undefined and therefor needs special bookkeeping (see "setActiveEditor()")!
    _changeMolecule(idcode: string, molecule: OCL.Molecule) {
        if(StructureEditor.activeEditor) {
            StructureEditor.activeEditor.log('changeMolecule callback: ' + idcode);
            StructureEditor.activeEditor.idcode = idcode;
            // event name is must be propertyname-changed!!!
            StructureEditor.activeEditor.dispatchEvent(new CustomEvent('idcode-changed', {detail: idcode,}));
        }
    }

    _handleFragmentMode(idcode: string | null, fragment: boolean | null): string | null {
        // fragment handling
        // TODO improve
        this.log("fragment mode: " + fragment);
        let molecule = OCL.Molecule.fromIDCode(idcode);
        if(idcode != null && fragment != null && molecule.isFragment() != fragment) {
            this.log("fragment mode of molecule is being adjusted!");
            molecule.setFragment(fragment);
            let tokens = idcode.split(" ");
            return molecule.getIDCode() + " " + (tokens.length>1?tokens[1]:molecule.getIDCoordinates());
        } else {
            return idcode;
        }
    }

    log(msg: string | null) {
        // console.warn('StructureEditor(' + this.id + '): ' + msg)
    }

}