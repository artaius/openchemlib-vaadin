package ch.artaios.openchemlib.vaadin;

import com.actelion.research.chem.IDCodeParser;
import com.actelion.research.chem.StereoMolecule;
import com.actelion.research.chem.reaction.Reaction;
import com.actelion.research.chem.reaction.ReactionEncoder;
import com.vaadin.flow.function.SerializableBiFunction;

/**
 * Project: openchemlib-vaadin
 * Date:    06.11.24
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

public class ChemUtils {
    public static StereoMolecule getStereoMolecule(String idcode) {
        // TODO: check why idcode with no coordinates (but trailing space) appear.
        idcode = idcode !=null? idcode.trim():null;
        StereoMolecule stereoMolecule = new StereoMolecule();
        IDCodeParser parser = new IDCodeParser();
        parser.parse(stereoMolecule, idcode);
        return stereoMolecule;
    }

    public static Reaction getReaction(String idcode) {
        return ReactionEncoder.decode(idcode, true);
    }

    public static String getIdcode(StereoMolecule stereoMolecule) {
        if(stereoMolecule==null)
            return null;
        if(stereoMolecule.getIDCoordinates()!=null && !stereoMolecule.getIDCoordinates().isBlank())
            return String.format("%s %s", stereoMolecule.getIDCode(), stereoMolecule.getIDCoordinates());
        else
            return stereoMolecule.getIDCode();
    }

    public static String getIdcode(Reaction reaction) {
        if(reaction==null)
            return null;
        return ReactionEncoder.encode(reaction, true, ReactionEncoder.INCLUDE_MAPPING | ReactionEncoder.INCLUDE_COORDS | ReactionEncoder.RETAIN_REACTANT_AND_PRODUCT_ORDER);
    }

    public final static SerializableBiFunction<OpenChemLibEditor<StereoMolecule>, String, StereoMolecule> PRESENTATION_TO_MODEL_STRUCTURE = new SerializableBiFunction<>() {
        @Override
        public StereoMolecule apply(OpenChemLibEditor openChemLibEditor, String idcode) {
            return getStereoMolecule(idcode);
        }
    };

    public final static SerializableBiFunction<OpenChemLibEditor<StereoMolecule>, StereoMolecule, String> MODEL_TO_PRESENTATION_STRUCTURE = new SerializableBiFunction<>() {
        @Override
        public String apply(OpenChemLibEditor openChemLibEditor, StereoMolecule stereoMolecule) {
            return getIdcode(stereoMolecule);
        }
    };

    public final static SerializableBiFunction<OpenChemLibEditor<Reaction>, String, Reaction> PRESENTATION_TO_MODEL_REACTION = new SerializableBiFunction<>() {
        @Override
        public Reaction apply(OpenChemLibEditor<Reaction> reactionOpenChemLibEditor, String idcode) {
            return getReaction(idcode);
        }
    };

    public final static SerializableBiFunction<OpenChemLibEditor<Reaction>, Reaction, String> MODEL_TO_PRESENTATION_REACTION = new SerializableBiFunction<>() {
        @Override
        public String apply(OpenChemLibEditor<Reaction> reactionOpenChemLibEditor, Reaction reaction) {
            return getIdcode(reaction);
        }
    };

}
