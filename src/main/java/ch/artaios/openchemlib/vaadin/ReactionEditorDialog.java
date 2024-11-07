package ch.artaios.openchemlib.vaadin;

import com.actelion.research.chem.reaction.Reaction;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * View for reactions that is editable through a dialog (double click)
 * @see OpenChemLibEditor
 */
@Uses(Dialog.class)
@Uses(Button.class)
@Uses(VerticalLayout.class)
public class ReactionEditorDialog extends Div {

    protected final ReactionView reactionView;
    protected final Dialog dialog;
    private final ReactionEditor reactionEditor;

    /**
     * Constructor
     * @param fragment if true, fragment mode is enabled
     */
    public ReactionEditorDialog(boolean fragment) {
        dialog = new Dialog("Reaction Editor");
        reactionView = new ReactionView(fragment, true);
        reactionEditor = new ReactionEditor(fragment);

        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.add(reactionEditor);

        Button okButton = new Button("Ok", e -> {
            Reaction value = reactionEditor.getValue();
            reactionView.setValue(value);
            dialog.close();
        });
        Button cancelButton = new Button("Cancel", e -> {
            dialog.close();
        });
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(okButton);

        reactionView.addDblClickListener(event -> {
            Reaction value = reactionView.getValue();
            reactionEditor.setValue(value);
            dialog.open();
        });

        reactionView.setSizeFull();

        add(dialog, reactionView);
    }

    public void addValueChangeListener(HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<OpenChemLibEditor<Reaction>, Reaction>> listener) {
        reactionView.addValueChangeListener(listener);
    }
}
