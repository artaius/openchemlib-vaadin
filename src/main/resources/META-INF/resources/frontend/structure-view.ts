import {html, LitElement, PropertyValues, TemplateResult} from 'lit';
import {property, customElement} from 'lit/decorators.js';
import OCL from 'openchemlib/full.pretty.js';

@customElement('structure-view')
export class StructureView extends LitElement {
    @property({type: String, reflect: true})
    idcode: string = "";
    @property({type: Boolean, reflect: true})
    editable: boolean = false;

    private static id: number = 0;
    private canvas!: HTMLCanvasElement;

    constructor() {
        super();
        this.log('constructor');
    }

    /** Invoked when a component is added to the document's DOM.
     *
     */
    connectedCallback(): void {
        super.connectedCallback();
        if(!this.getAttribute('id'))
            this.setAttribute("id", this.tagName + "_" + (++StructureView.id));
        this.log('connectedCallback');
        this.init();
    }

    // when lazy loading vaadin component canvas element still is null on connectedCallback, therefore additional init here
    protected firstUpdated(_changedProperties: PropertyValues) {
        super.firstUpdated(_changedProperties);
        this.log('firstUpdated');
        if(this.canvas == null)
            this.init();
    }

    private init() {
        let canvas = this.renderRoot.querySelector('canvas');
        if (canvas) {
            this.canvas = canvas;
        }
    }

    protected updated(_changedProperties: PropertyValues) {
        super.updated(_changedProperties);
        this.log('updated');

        let molecule = null;

        if (this.idcode != null && this.idcode != "") {
            molecule = OCL.Molecule.fromIDCode(this.idcode);
        } else {
            molecule = OCL.Molecule.fromSmiles("");
        }

        if (this.canvas != null) {
            this.log("draw");
            OCL.StructureView.drawMolecule(this.canvas, molecule);
            // OCL.StructureView.drawStructure("123", "difH@BAIVUxZ`@@@", "!BbGvH@hc}b@K~_xc}bOw~_p");
        }
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
            <canvas style="width: 100%; height: 100%;" class="actstruct" draggable="true" @dragstart="${this._drag}" @dragover="${this._allowDrop}" @drop="${this._drop}"></canvas>
        `;
    }

    attributeChangedCallback(name: string, _old: string | null, value: string | null) {
        super.attributeChangedCallback(name, _old, value);
        this.log('changedProperties -> ' + name + ': ' + value);
        this.dispatchEvent(new CustomEvent(name + '-changed', {detail: value,}));
    }

    _setSmiles(smiles: string) {
        let molecule = OCL.Molecule.fromSmiles(smiles);
        this.idcode = molecule.getIDCode() + " " + molecule.getIDCoordinates();
    }

    _copy(){
        this.log('copy');
        navigator.clipboard.writeText(this.idcode).then(r => this.log('idcode copied'));
        // TODO handle other content types? see below.
    }

    _paste(){
        // TODO handle other content types?
        // for debugging: list available clipboard content
        navigator.clipboard.read().then(clipboardItems => {
            this.log(clipboardItems.length + " clipboardItem");
            clipboardItems.forEach(item => {
                item.types.forEach(type => {
                    item.getType(type).then(value => value.text().then(text => this.log("clipboardItem type: " + type + ": " + text)));
                })
            })
        });
        navigator.clipboard.readText().then(idcode => {
            this.idcode = idcode;
            this.log('idcode pasted');
        });
    }

    _drag(e: DragEvent) {
        this.log('drag');
        if(e.dataTransfer != null) {
            e.dataTransfer.setData("text/plain", this.idcode);
            // e.dataTransfer.setData('text/html', '<h1>Foo bar</h1>');
            // e.dataTransfer.setData('text/uri-list', 'https://example.com');
        }
    }

    _allowDrop(e: DragEvent) {
        if(this.editable)
            e.preventDefault();
    }

    _drop(e: DragEvent) {
        e.preventDefault();
        if(e.dataTransfer != null)
            this.idcode = e.dataTransfer.getData("text/plain");
    }

    log(msg: string) {
        // console.warn('StructureView(' + this.id + '): ' + msg)
    }
}

declare global {
    interface HTMLElementTagNameMap {
        'structure-view': StructureView;
    }
}