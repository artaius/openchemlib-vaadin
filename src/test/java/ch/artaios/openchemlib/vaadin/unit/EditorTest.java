package ch.artaios.openchemlib.vaadin.unit;


import ch.artaios.openchemlib.vaadin.ChemUtils;
import ch.artaios.openchemlib.vaadin.OpenChemLibEditor;
import ch.artaios.openchemlib.vaadin.StructureEditor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EditorTest {

    @Test
    public void testGetIdCode() {
        StructureEditor structureEditor = new StructureEditor(false);
        structureEditor.setValue(ChemUtils.getStereoMolecule("difH@BAIVUxZ`@@@"));
        assertNotNull(structureEditor);
        assertNotNull(structureEditor.getValue());
    }
}
