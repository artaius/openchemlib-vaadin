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

package ch.artaios.openchemlib.vaadin;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.shared.Registration;

@Deprecated(since = "Use StructureEditor instead!")
@Tag("structure-view-old")
@NpmPackage(value = "openchemlib", version = "9.2.0")
@JsModule(value = "openchemlib")
@JsModule(value = "./structure-view-old.ts")
@CssImport(value = "./structure-view-old.css")
/*
 If you wish to include your own JS modules in the add-on jar, add the module
 files to './src/main/resources/META-INF/resources/frontend' and insert an
 annotation @JsModule("./my-module.js") here.
*/
public class StructureViewOld extends AbstractSinglePropertyField<StructureViewOld, String> implements HasSize {
    public static final String ATTRIBUTE_EDITABLE = "editable";
    private ContextMenu contextMenu;

    // See comment in StructureEditor
    // For some reason PropertyDescriptor works better here (here values are only set from server)
    private static final PropertyDescriptor<Boolean, Boolean> editableProperty = PropertyDescriptors.propertyWithDefault(ATTRIBUTE_EDITABLE, false);

    public StructureViewOld() {
        this(false);
    }

    public StructureViewOld(boolean editable) {
        super("idcode", "", true);
        setEditable(editable);

        contextMenu = new ContextMenu(this);
        contextMenu.addItem("Copy", event -> {
            System.out.println("Copy...");
            getElement().callJsFunction("_copy");
        });
        if(editable) {
            contextMenu.addItem("Paste", event -> {
                System.out.println("Paste...");
                getElement().callJsFunction("_paste");
            });
        }
    }

    public void setSmiles(String smiles) {
        getElement().callJsFunction("_setSmiles", smiles);
    }

    public boolean getEditable() {
//        final String property = getElement().getProperty(ATTRIBUTE_EDITABLE);
        final Boolean property = editableProperty.get(this);
        return property;
    }
    public void setEditable(boolean editable) {
        editableProperty.set(this, editable);
//        getElement().setAttribute(ATTRIBUTE_EDITABLE, editable);
        if(contextMenu!=null && contextMenu.getItems().size()>1)
            contextMenu.getItems().get(1).setEnabled(editable);
        editableProperty.set(this, editable);
    }

    public Registration addDblClickListener(ComponentEventListener<DblClickEvent> listener) {
        return addListener(DblClickEvent.class, listener);
    }

    @DomEvent("dblclick")
    public static class DblClickEvent extends ComponentEvent<StructureViewOld> {
        public DblClickEvent(StructureViewOld source, boolean fromClient) {
            super(source, fromClient);
        }
    }

}
