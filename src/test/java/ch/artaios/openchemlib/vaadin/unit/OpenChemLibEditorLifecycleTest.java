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

package ch.artaios.openchemlib.vaadin.unit;

import ch.artaios.openchemlib.vaadin.ChemUtils;
import ch.artaios.openchemlib.vaadin.StructureEditor;
import com.actelion.research.chem.StereoMolecule;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OpenChemLibEditorLifecycleTest {

    @Test
    void reattachRestoresCurrentValueAfterClientStateLoss() {
        TestStructureEditor editor = new TestStructureEditor();
        StereoMolecule initial = ChemUtils.getStereoMolecule("difH@BAIVUxZ`@@@");
        StereoMolecule updated = ChemUtils.getStereoMolecule("ffc`P@H`QxNQQJJIJIZJHiSkQSejB`jFjhhaEqFUh@");

        editor.setValue(initial);
        editor.simulateAttach(true);
        assertEquals(ChemUtils.getIdcode(initial), editor.getElement().getAttribute("idcode"));

        editor.setValue(updated);
        assertEquals(ChemUtils.getIdcode(updated), editor.getElement().getAttribute("idcode"));

        editor.simulateDetach();
        editor.getElement().removeAttribute("idcode");

        editor.simulateAttach(false);
        assertEquals(ChemUtils.getIdcode(updated), editor.getElement().getAttribute("idcode"));
    }

    @Test
    void invalidClientPayloadsAreHandledSafely() {
        assertDoesNotThrow(() -> ChemUtils.getStereoMolecule(null));
        assertDoesNotThrow(() -> ChemUtils.getStereoMolecule("not-an-idcode"));
        assertDoesNotThrow(() -> ChemUtils.getReaction(null));
        assertDoesNotThrow(() -> ChemUtils.getReaction("not-a-reaction"));
    }

    private static final class TestStructureEditor extends StructureEditor {
        private TestStructureEditor() {
            super(false);
        }

        private void simulateAttach(boolean initialAttach) {
            super.onAttach(new AttachEvent(this, initialAttach));
        }

        private void simulateDetach() {
            super.onDetach(new DetachEvent(this));
        }
    }
}
