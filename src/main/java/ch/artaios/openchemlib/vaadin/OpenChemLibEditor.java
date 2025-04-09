package ch.artaios.openchemlib.vaadin;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.function.SerializableBiFunction;
import com.vaadin.flow.shared.Registration;
import elemental.json.Json;
import elemental.json.JsonArray;

import java.util.ArrayList;

@Tag("openchemlib-editor")
@NpmPackage(value = "openchemlib", version = "8.19.0")
@JsModule("openchemlib/full.pretty.js")
@JsModule("./openchemlib-editor-init.js")
@CssImport("./openchemlib-editor.css")

/**
 * OpenChemLibEditor is aa abstract Vaadin component for drawing and displaying chemical structures/reactions.
 */
public abstract class OpenChemLibEditor<T> extends AbstractSinglePropertyField<OpenChemLibEditor<T>, T> implements HasSize {
    private boolean initialized;
    private T initialValue;

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

    protected final SerializableBiFunction<OpenChemLibEditor<T>, String, T> presentationToModel;
    protected final SerializableBiFunction<OpenChemLibEditor<T>, T, String> modelToPresentation;

    /**
     * Creates a new OpenChemLibEditor.
     * @param <P> the presentation type
     *           (e.g. StereoMolecule for molecules, Reaction for reactions)
     * @param defaultValue the default value
     * @param presentationToModel the function to convert the presentation value to the model value
     * @param modelToPresentation the function to convert the model value to the presentation value
     * @param mode the editor mode (MOLECULE/REACTION)
     * @param fragment if true, fragment mode is enabled
     * @param readonly if true, the editor doesn't allow drawing (corresponds to OCL-JS readonly property/attribute)
     * @param editable if true, the editor is editable (e.g. by setting the idcode by pasting/dropping)
     */
    public <P> OpenChemLibEditor(Mode mode, boolean fragment, boolean readonly, boolean editable, T defaultValue, SerializableBiFunction<OpenChemLibEditor<T>, String, T> presentationToModel, SerializableBiFunction<OpenChemLibEditor<T>, T, String> modelToPresentation) {
        super("idcode", defaultValue, String.class, presentationToModel, modelToPresentation);

        // keep function references (needed in setValue)
        this.presentationToModel = presentationToModel;
        this.modelToPresentation = modelToPresentation;

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

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        initialized = true;
        if (initialValue != null)
            getElement().setAttribute(ATTRIBUTE_IDCODE, modelToPresentation.apply(null, initialValue));
    }

    /* Due to a bug in OCL-JS where "CanvasEditorElement.#state"
     * is not initialized early enough (it is initialized on connectedCallback)
     * properties are set by attribute instead of property.
     *
     * TODO recheck in a later version of OCL-JS if problem still persists
     */

    @Override
    public T getValue() {
        if (initialized)
            return super.getValue();
        else
            return initialValue;
    }

    @Override
    public void setValue(T value) {
        // Handle lazy initialization
        if(initialized) {
            String idcode = modelToPresentation.apply(null, value);
            getElement().setProperty(ATTRIBUTE_IDCODE, idcode);
        } else {
            initialValue = value;
        }
    }

    public boolean getReadonly() {
        return readonlyProperty.get(this);
    }
    protected void setReadonly(boolean readonly) {
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
    protected void setMode(Mode mode) {
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

    public Registration addDblClickListener(ComponentEventListener<DblClickEvent> listener) {
        return addListener(DblClickEvent.class, listener);
    }

    @DomEvent("dblclick")
    public static class DblClickEvent extends ComponentEvent<OpenChemLibEditor> {
        public DblClickEvent(OpenChemLibEditor source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    public ContextMenu getContextMenu() {
        return contextMenu;
    }

    public Registration addAtomSelectionListener(ComponentEventListener<AtomSelectionEvent> listener) {
        return addListener(AtomSelectionEvent.class, listener);
    }

    public void highlightBondsBackground(ArrayList<Integer> bondIndices, Integer reactantMolId, Integer productMolId) {
        JsonArray jsonIndices = Json.createArray();
        for (int i = 0; i < bondIndices.size(); i++) {
            jsonIndices.set(i, bondIndices.get(i));
        }
        getElement().callJsFunction("highlightBondsBackground", jsonIndices, reactantMolId, productMolId);
    }

    public void highlightBondsForeground(ArrayList<Integer> bondIndices, Integer reactantMolId, Integer productMolId) {
        JsonArray jsonIndices = Json.createArray();
        for (int i = 0; i < bondIndices.size(); i++) {
            jsonIndices.set(i, bondIndices.get(i));
        }
        getElement().callJsFunction("highlightBondsForeground", jsonIndices, reactantMolId, productMolId);
    }

    public void clearHighlights(Integer reactantMolId, Integer productMolId) {
        getElement().callJsFunction("clearHighlights", reactantMolId, productMolId);
    }

    public void setAtomColor(int atom, int color, Integer reactantMolId, Integer productMolId) {
        getElement().callJsFunction("setAtomColor", atom, color, reactantMolId, productMolId);
    }

    public void removeAtomColors(Integer reactantMolId, Integer productMolId) {
        getElement().callJsFunction("removeAtomColors", reactantMolId, productMolId);
    }

    public void setAtomCustomLabel(int atom, String label) {
        getElement().callJsFunction("setAtomCustomLabel", atom, label);
    }
}
