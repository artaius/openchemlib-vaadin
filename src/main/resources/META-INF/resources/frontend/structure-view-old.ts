/*
 * Copyright (c) 2025 artaius
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import {html, LitElement, PropertyValues, TemplateResult} from 'lit';
import {property, customElement} from 'lit/decorators.js';
import OCL from 'openchemlib';

@customElement('structure-view-old')
export class StructureViewOld extends LitElement {
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
            this.setAttribute("id", this.tagName + "_" + (++StructureViewOld.id));
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
        'structure-view': StructureViewOld;
    }
}