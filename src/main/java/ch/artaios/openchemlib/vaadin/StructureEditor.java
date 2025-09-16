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

import com.actelion.research.chem.StereoMolecule;

import java.util.ArrayList;

/**
 * Editor for structures
 * @see OpenChemLibEditor
 */
public class StructureEditor extends OpenChemLibEditor<StereoMolecule> {
    /**
     * Constructor
     * @see OpenChemLibEditor
     *
     * Initializes the editor with an empty structure
     *
     * @param fragment if true, fragment mode is enabled
     */
    public StructureEditor(boolean fragment) {
        super(Mode.MOLECULE, fragment, false, true, new StereoMolecule(), ChemUtils.PRESENTATION_TO_MODEL_STRUCTURE, ChemUtils.MODEL_TO_PRESENTATION_STRUCTURE);
    }

    @Override
    protected void setCustomAtomProperties(StereoMolecule mol) {
        if (mol != null && !mol.getIDCode().equals("d@")) {
            for (int atom = 0; atom < mol.getAtoms(); atom++) {
                // Set custom atom labels
                if (mol.getAtomCustomLabel(atom) != null) {
                    setAtomCustomLabel(atom, mol.getAtomCustomLabel(atom), true);
                }

                // Set atom coloring
                setAtomColor(atom, mol.getAtomColor(atom), null, null, true);
            }

            // Set bond highlighting
            ArrayList<Integer> highlightedBondsForeground = new ArrayList<>();
            ArrayList<Integer> highlightedBondsBackground = new ArrayList<>();
            for (int bond = 0; bond < mol.getBonds(); bond++) {
                if (mol.isBondForegroundHilited(bond)) {
                    highlightedBondsForeground.add(bond);
                }
                if (mol.isBondBackgroundHilited(bond)) {
                    highlightedBondsBackground.add(bond);
                }
            }
            highlightBondsForeground(highlightedBondsForeground, null, null);
            highlightBondsBackground(highlightedBondsBackground, null, null);
        }
    }

    public void highlightBondsBackground(ArrayList<Integer> bondIndices) {
        highlightBondsBackground(bondIndices, null, null);
    }

    public void highlightBondsForeground(ArrayList<Integer> bondIndices) {
        highlightBondsForeground(bondIndices, null, null);
    }

    public void clearHighlights() {
        clearHighlights(null, null);
    }

    public void setAtomColor(int atom, int color, boolean canonicalOrdering) {
        setAtomColor(atom, color, null, null, canonicalOrdering);
    }

    public void removeAtomColors() {
        removeAtomColors(null, null);
    }
}
