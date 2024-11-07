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
 * View for structures
 * @see OpenChemLibEditor
 */
public class StructureView extends OpenChemLibEditor<StereoMolecule> {
    /**
     * Constructor
     * @see OpenChemLibEditor
     *
     * Initializes the view with an empty structure
     *
     * @param fragment if true, fragment mode is enabled
     * @param editable if true, the editor is editable (e.g. by setting the idcode by pasting/dropping)
     */
    public StructureView(boolean fragment, boolean editable) {
        super(Mode.MOLECULE, fragment, true, editable, new StereoMolecule(), ChemUtils.PRESENTATION_TO_MODEL_STRUCTURE, ChemUtils.MODEL_TO_PRESENTATION_STRUCTURE);
    }
}
