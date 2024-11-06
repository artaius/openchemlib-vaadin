package ch.artaios.openchemlib.vaadin;

import com.actelion.research.chem.StereoMolecule;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;


@Uses(Dialog.class)
@Uses(Button.class)
@Uses(VerticalLayout.class)
public class StructureEditorDialog extends Div {

    protected final StructureView structureView;
    protected final Dialog dialog;
    private final StructureEditor structureEditor;

    public StructureEditorDialog(boolean fragment) {
        dialog = new Dialog("Structure Editor");
        structureView = new StructureView(fragment, true);
        structureEditor = new StructureEditor(fragment);

        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.add(structureEditor);

        Button okButton = new Button("Ok", e -> {
            StereoMolecule value = structureEditor.getValue();
            structureView.setValue(value);
            dialog.close();
        });
        Button cancelButton = new Button("Cancel", e -> {
            dialog.close();
        });
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(okButton);

        structureView.addDblClickListener(event -> {
            StereoMolecule value = structureView.getValue();
            structureEditor.setValue(value);
            dialog.open();
        });

        structureView.setSizeFull();

        add(dialog, structureView);
    }

    public void addValueChangeListener(HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<OpenChemLibEditor<StereoMolecule>, StereoMolecule>> listener) {
        structureView.addValueChangeListener(listener);
    }
}
