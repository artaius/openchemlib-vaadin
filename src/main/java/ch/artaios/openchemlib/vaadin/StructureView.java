package ch.artaios.openchemlib.vaadin;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.shared.Registration;

@Tag("structure-view")
@NpmPackage(value = "openchemlib", version = "8.16.0")
@JsModule(value = "openchemlib/full.pretty.js")
@JsModule(value = "./structure-view.ts")
@CssImport(value = "./structure-view.css")
/*
 If you wish to include your own JS modules in the add-on jar, add the module
 files to './src/main/resources/META-INF/resources/frontend' and insert an
 annotation @JsModule("./my-module.js") here.
*/
public class StructureView extends AbstractSinglePropertyField<StructureView, String> implements HasSize {
    public static final String ATTRIBUTE_EDITABLE = "editable";
    private ContextMenu contextMenu;

    // See comment in StructureEditor
    // For some reason PropertyDescriptor works better here (here values are only set from server)
    private static final PropertyDescriptor<Boolean, Boolean> editableProperty = PropertyDescriptors.propertyWithDefault(ATTRIBUTE_EDITABLE, false);

    public StructureView() {
        this(false);
    }

    public StructureView(boolean editable) {
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
    public static class DblClickEvent extends ComponentEvent<StructureView> {
        public DblClickEvent(StructureView source, boolean fromClient) {
            super(source, fromClient);
        }
    }

}
