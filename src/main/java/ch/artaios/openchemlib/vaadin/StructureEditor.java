package ch.artaios.openchemlib.vaadin;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;

@Tag("structure-editor")
@NpmPackage(value = "openchemlib", version = "8.7.2")
@JsModule(value = "openchemlib/full.pretty.js")
@JsModule(value = "./structure-editor.ts")
@CssImport(value = "./structure-editor.css")
/*
 If you wish to include your own JS modules in the add-on jar, add the module
 files to './src/main/resources/META-INF/resources/frontend' and insert an
 annotation @JsModule("./my-module.js") here.
*/
public class StructureEditor extends AbstractSinglePropertyField<StructureEditor, String> implements HasSize {
    public static final String ATTRIBUTE_FRAGMENT = "fragment";
    public static final String ATTRIBUTE_SHOWIDCODE = "showidcode";

    // PropertyDescriptor access does not seem to update attribute!!! Is this maybe related to the fact that StructureEditor doesn't have a shadow DOM?
    // Alternatively inherit from AbstractField and self handle events (https://vaadin.com/docs/latest/create-ui/web-components/java-api-for-a-web-component#synchronizing-the-value)
    protected static final PropertyDescriptor<Boolean, Boolean> fragmentProperty = PropertyDescriptors.propertyWithDefault(ATTRIBUTE_FRAGMENT, true);
    protected static final PropertyDescriptor<Boolean, Boolean> showIdCodeProperty = PropertyDescriptors.propertyWithDefault(ATTRIBUTE_SHOWIDCODE, false);


    public StructureEditor() {
        this(true, false);
    }

    public StructureEditor(boolean fragment) {
        this(fragment, false);
    }

    public StructureEditor(boolean fragment, boolean showIdCode) {
        super("idcode", "", true);
        setFragment(fragment);
        setShowIdCode(showIdCode);
    }

    public boolean isFragment() {
        return fragmentProperty.get(this);
//        return Boolean.parseBoolean(getElement().getAttribute(ATTRIBUTE_FRAGMENT));
    }

    public void setFragment(boolean fragment) {
        getElement().setAttribute(ATTRIBUTE_FRAGMENT, fragment);
        fragmentProperty.set(this, fragment);
    }

    public boolean isShowIdCode() {
        return showIdCodeProperty.get(this);
//        return Boolean.parseBoolean(getElement().getAttribute(ATTRIBUTE_SHOWIDCODE));
    }

    protected void setShowIdCode(boolean showIdCode) {
        getElement().setAttribute(ATTRIBUTE_SHOWIDCODE, showIdCode);
        showIdCodeProperty.set(this, showIdCode);
    }

//    public StructureEditor(String idcode) {
//        super("idcode", idcode, true);
//    }

//    public void setIdCode(String idcode) {
//        getElement().setAttribute("idcode", idcode);
////        idcodeProperty.set(this, idCode);
//    }
//
//    public String getIdCode() {
//        return getElement().getAttribute("idcode");
////        return idcodeProperty.get(this);
//    }
//
//    public Registration addChangeListener(ComponentEventListener<ChangeEvent> listener) {
//        return addListener(ChangeEvent.class, listener);
//    }
//
//    @DomEvent("changed")
//    public static class ChangeEvent extends ComponentEvent<StructureEditorTs> {
//        private final String idcode;
//        public ChangeEvent(StructureEditorTs source, boolean fromClient, @EventData("event.detail") String idcode) {
//            super(source, fromClient);
//            this.idcode = idcode;
//        }
//
//        public String getIdcode() {
//            return idcode;
//        }
//    }
}
