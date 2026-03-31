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

import com.actelion.research.chem.IDCodeParser;
import com.actelion.research.chem.IsomericSmilesCreator;
import com.actelion.research.chem.SmilesParser;
import com.actelion.research.chem.StereoMolecule;
import com.actelion.research.chem.reaction.Reaction;
import com.actelion.research.chem.reaction.ReactionEncoder;
import com.vaadin.flow.function.SerializableBiFunction;

/**
 * Utility class for chemical operations.
 * Provides methods to convert between different chemical representations.
 */
public class ChemUtils {
    private final static SmilesParser SMILES_PARSER = new SmilesParser();

    /**
     * Returns a StereoMolecule from an idcode
     * @param idcode the idcode
     * @return the StereoMolecule
     */
    public static StereoMolecule getStereoMolecule(String idcode) {
        // TODO: check why idcode with no coordinates (but trailing space) appear.
        if (idcode == null || idcode.isBlank()) {
            return new StereoMolecule();
        }
        idcode = idcode.trim();
        StereoMolecule stereoMolecule = new StereoMolecule();
        IDCodeParser parser = new IDCodeParser();
        try {
            parser.parse(stereoMolecule, idcode);
        } catch (Exception e) {
            return new StereoMolecule();
        }
        return stereoMolecule;
    }

    /**
     * Returns a StereoMolecule from a SMILES string.
     *
     * @param smiles the SMILES string
     * @return the StereoMolecule
     * @throws Exception if parsing fails
     */
    public static StereoMolecule getStereoMoleculeFromSmiles(String smiles) throws Exception {
        StereoMolecule stereoMolecule = new StereoMolecule();
        SMILES_PARSER.parse(stereoMolecule, smiles);
        return stereoMolecule;
    }


    /**
     * Returns a Reaction from an idcode.
     *
     * @param idcode the idcode
     * @return the Reaction
     */
    public static Reaction getReaction(String idcode) {
        if (idcode == null || idcode.isBlank()) {
            return new Reaction();
        }
        try {
            return ReactionEncoder.decode(idcode, true);
        } catch (Exception e) {
            return new Reaction();
        }
    }

    /**
     * Returns the idcode of a StereoMolecule.
     *
     * @param stereoMolecule the StereoMolecule
     * @return the idcode
     */
    public static String getIdcode(StereoMolecule stereoMolecule) {
        if(stereoMolecule==null)
            return null;
        if(stereoMolecule.getIDCoordinates()!=null && !stereoMolecule.getIDCoordinates().isBlank())
            return String.format("%s %s", stereoMolecule.getIDCode(), stereoMolecule.getIDCoordinates());
        else
            return stereoMolecule.getIDCode();
    }

    /**
     * Returns the SMILES string of a StereoMolecule.
     *
     * @param stereoMolecule the StereoMolecule
     * @return the SMILES string
     */
    public static String getSmiles(StereoMolecule stereoMolecule) {
        if(stereoMolecule==null)
            return null;
        IsomericSmilesCreator smilesCreator = new IsomericSmilesCreator(stereoMolecule);

        return smilesCreator.getSmiles();
    }

    /**
     * Returns the idcode of a Reaction.
     *
     * @param reaction the Reaction
     * @return the idcode
     */
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
