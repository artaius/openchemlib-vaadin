package ch.artaios.openchemlib.vaadin;

import com.actelion.research.chem.IDCodeParser;
import com.actelion.research.chem.StereoMolecule;
import com.vaadin.flow.function.SerializableBiFunction;

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

public class StructureEditor extends OpenChemLibEditor<StereoMolecule> {
    public StructureEditor(boolean fragment) {
        super(Mode.MOLECULE, fragment, false, true, new StereoMolecule(), ChemUtils.PRESENTATION_TO_MODEL_STRUCTURE, ChemUtils.MODEL_TO_PRESENTATION_STRUCTURE);
    }
}
