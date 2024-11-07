package ch.artaios.openchemlib.vaadin;

import com.actelion.research.chem.StereoMolecule;

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
}
