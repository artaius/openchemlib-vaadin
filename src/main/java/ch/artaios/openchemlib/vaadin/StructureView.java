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

public class StructureView extends OpenChemLibEditor<StereoMolecule> {
    public StructureView(boolean fragment, boolean editable) {
        this(fragment, true, editable);
    }

    public StructureView(boolean fragment, boolean readonly, boolean editable) {
        super(Mode.MOLECULE, fragment, readonly, editable, new StereoMolecule(), StructureEditor.PRESENTATION_TO_MODEL, StructureEditor.MODEL_TO_PRESENTATION);
    }
}
