package ch.artaios.openchemlib.vaadin;

import com.actelion.research.chem.StereoMolecule;

import java.util.ArrayList;

/**
 * Project: openchemlib-vaadin
 * Date:    22.10.24
 * <p>
 * Copyright (c) 2017 - 2024
 * Idorsia Pharmaceuticals Ltd.
 * Hegenheimermattweg 91
 * CH-4123 Allschwil, Switzerland
 * <p>
 * All Rights Reserved.
 * <p>
 * This software is the proprietary information of Idorsia Pharmaceuticals, Ltd.
 * Use is subject to license terms.
 * <p>
 * Author: Roman Bär
 */

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
                    setAtomCustomLabel(atom, mol.getAtomCustomLabel(atom));
                }

                // Set atom coloring
                setAtomColor(atom, mol.getAtomColor(atom), null, null);
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

    public void setAtomColor(int atom, int color) {
        setAtomColor(atom, color, null, null);
    }

    public void removeAtomColors() {
        removeAtomColors(null, null);
    }
}
