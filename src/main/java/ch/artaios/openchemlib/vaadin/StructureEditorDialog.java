package ch.artaios.openchemlib.vaadin;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;


@Uses(Dialog.class)
@Uses(Button.class)
@Uses(VerticalLayout.class)
public class StructureEditorDialog extends VerticalLayout {

    protected final StructureView structureView;
    protected final Dialog dialog;
    private final StructureEditorOld structureEditorOld;

    public StructureEditorDialog(String idCode) {
        this();
        structureView.setValue(idCode);
    }

    public StructureEditorDialog(boolean fragment) {
        this();
        structureEditorOld.setFragment(fragment);
    }

    public StructureEditorDialog() {
        this.setPadding(false);
        dialog = new Dialog("Structure Editor");
        structureView = new StructureView(true);
        structureEditorOld = new StructureEditorOld();

        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.add(structureEditorOld);

        Button okButton = new Button("Ok", e -> {
            structureView.setValue(structureEditorOld.getValue());
            dialog.close();
        });
        Button cancelButton = new Button("Cancel", e -> {
            dialog.close();
        });
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(okButton);

        structureView.addDblClickListener(event -> {
            final String idCode = structureView.getValue();
            structureEditorOld.setValue(idCode);
            dialog.open();
        });

        add(dialog, structureView);
    }

    public String getIdCode() {
        return structureView.getValue();
    }

    public void addValueChangeListener(HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<StructureView, String>> listener) {
        structureView.addValueChangeListener(listener);
    }
}
