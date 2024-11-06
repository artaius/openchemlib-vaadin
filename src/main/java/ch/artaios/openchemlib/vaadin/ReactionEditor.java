package ch.artaios.openchemlib.vaadin;

import com.actelion.research.chem.reaction.Reaction;

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

public class ReactionEditor extends OpenChemLibEditor<Reaction> {
    public ReactionEditor(boolean fragment) {
        super(Mode.REACTION, fragment, false, true, new Reaction(), ChemUtils.PRESENTATION_TO_MODEL_REACTION, ChemUtils.MODEL_TO_PRESENTATION_REACTION);
    }
}
