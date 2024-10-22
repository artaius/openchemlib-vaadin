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

    public final static SerializableBiFunction<OpenChemLibEditor<StereoMolecule>, String, StereoMolecule> PRESENTATION_TO_MODEL = new SerializableBiFunction<>() {
        @Override
        public StereoMolecule apply(OpenChemLibEditor openChemLibEditor, String idcode) {
            StereoMolecule stereoMolecule = new StereoMolecule();
            IDCodeParser parser = new IDCodeParser();
            parser.parse(stereoMolecule, idcode);
            return stereoMolecule;
        }
    };
    public final static SerializableBiFunction<OpenChemLibEditor<StereoMolecule>, StereoMolecule, String> MODEL_TO_PRESENTATION = new SerializableBiFunction<>() {
        @Override
        public String apply(OpenChemLibEditor openChemLibEditor, StereoMolecule stereoMolecule) {
            return String.format("%s %s", stereoMolecule.getIDCode(), stereoMolecule.getIDCoordinates());
        }
    };

    public StructureEditor(boolean fragment) {
        super(Mode.MOLECULE, fragment, false, true, new StereoMolecule(), PRESENTATION_TO_MODEL, MODEL_TO_PRESENTATION);
    }
}
