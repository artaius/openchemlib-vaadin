package ch.artaios.openchemlib.vaadin;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;

@Tag("openchemlib-editor")
@NpmPackage(value = "openchemlib", version = "8.16.0")
@JsModule("openchemlib/full.pretty.js")
@JsModule("./openchemlib-editor-init.js")
@CssImport("./openchemlib-editor.css")

public class OpenChemLibEditor extends AbstractSinglePropertyField<OpenChemLibEditor, String> implements HasSize {
    private boolean initialized;
    private String initialIdcode;

    public enum Mode {
        MOLECULE,
        REACTION;
    }

    public static final String ATTRIBUTE_IDCODE = "idcode";
    public static final String ATTRIBUTE_READONLY = "readonly";
    public static final String ATTRIBUTE_EDITABLE = "editable";
    public static final String ATTRIBUTE_MODE = "mode";
    public static final String ATTRIBUTE_FRAGMENT = "fragment";

    private static final PropertyDescriptor<Boolean, Boolean> readonlyProperty = PropertyDescriptors.propertyWithDefault(ATTRIBUTE_READONLY, false);
    private static final PropertyDescriptor<Boolean, Boolean> editableProperty = PropertyDescriptors.propertyWithDefault(ATTRIBUTE_EDITABLE, false);
    private static final PropertyDescriptor<String, String> modeProperty = PropertyDescriptors.propertyWithDefault(ATTRIBUTE_MODE, Mode.MOLECULE.name().toLowerCase());
    private static final PropertyDescriptor<Boolean, Boolean> fragmentProperty = PropertyDescriptors.propertyWithDefault(ATTRIBUTE_FRAGMENT, false);

    protected final ContextMenu contextMenu;

    public OpenChemLibEditor() {
        this(Mode.MOLECULE, false, false, true);
    }

    /**
     * Creates a new OpenChemLibEditor.
     * @param mode the editor mode (MOLECULE/REACTION)
     * @param fragment if true, fragment mode is enabled
     * @param readonly if true, the editor doesn't allow drawing (corresponds to OCL-JS readonly property/attribute)
     * @param editable if true, the editor is editable (e.g. by setting the idcode by pasting/dropping)
     */
    public OpenChemLibEditor(Mode mode, boolean fragment, boolean readonly, boolean editable) {
        super("idcode", "", true);

        // set custom js event name
        setSynchronizedEvent("change");

        // init Vaadin specific JS
        getElement().executeJs("this.init();");

        // set initial values
        setMode(mode);
        setFragment(fragment);
        setReadonly(readonly);
        setEditable(editable);

        // create context menu
        contextMenu = new ContextMenu(this);
        contextMenu.addItem("Copy", event -> {
            System.out.println("Copy...");
            getElement().callJsFunction("copy");
        });
        final MenuItem paste = contextMenu.addItem("Paste", event -> {
            System.out.println("Paste...");
            getElement().callJsFunction("paste");
        });
        paste.setEnabled(editable);

    }

    /* Due to a bug in OCL-JS where "CanvasEditorElement.#state"
     * is not initialized early enough (it is initialized on connectedCallback)
     * properties are set by attribute instead of property.
     *
     * TODO recheck in a later version of OCL-JS if problem still persists
     */

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        initialized = true;
        getElement().setAttribute(ATTRIBUTE_IDCODE, initialIdcode);
    }

    /* Overriding property setter to enforce writing by attribute (see above) */
    @Override
    public void setValue(String idcode) {
        if(initialized)
            getElement().setProperty(ATTRIBUTE_IDCODE, idcode);
        else
            initialIdcode = idcode;
    }

    @Override
    public String getValue() {
        if (initialized)
            return super.getValue();
        else
            return getElement().getAttribute(ATTRIBUTE_IDCODE);
    }

    public boolean getReadonly() {
        return readonlyProperty.get(this);
    }
    public void setReadonly(boolean readonly) {
        // readonlyProperty.set(this, readonly);
        getElement().setAttribute(ATTRIBUTE_READONLY, readonly);

        // set draggable only if in readonly (non-drawing mode)
        getElement().setProperty("draggable", readonly);
    }

    public boolean getEditable() {
        return editableProperty.get(this);
    }
    public void setEditable(boolean editable) {
        editableProperty.set(this, editable);

        // enable paste context menu only if editable
        if(contextMenu!=null)
            contextMenu.getItems().get(1).setEnabled(editable);
    }

    public Mode getMode() {
        final String modeString = modeProperty.get(this);
        return Mode.valueOf(modeString.toUpperCase());
    }
    private void setMode(Mode mode) {
//        modeProperty.set(this, mode.name().toLowerCase());
        getElement().setAttribute(ATTRIBUTE_MODE, mode.name().toLowerCase());
    }

    public boolean getFragment() {
        return fragmentProperty.get(this);
    }
    public void setFragment(boolean fragment) {
//        fragmentProperty.set(this, fragment);
        getElement().setAttribute(ATTRIBUTE_FRAGMENT, fragment);
    }

//    public Registration addDblClickListener(ComponentEventListener<DblClickEvent> listener) {
//        return addListener(DblClickEvent.class, listener);
//    }
//
//    @DomEvent("dblclick")
//    public static class DblClickEvent extends ComponentEvent<OpenChemLibEditor> {
//        public DblClickEvent(OpenChemLibEditor source, boolean fromClient) {
//            super(source, fromClient);
//        }
//    }
}
