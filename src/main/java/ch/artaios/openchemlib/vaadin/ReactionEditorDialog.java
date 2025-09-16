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
