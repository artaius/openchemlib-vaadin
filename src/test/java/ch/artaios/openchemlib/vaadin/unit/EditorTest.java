package ch.artaios.openchemlib.vaadin.unit;


import ch.artaios.openchemlib.vaadin.OpenChemLibEditor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EditorTest {

    @Test
    public void testGetIdCode() {
        OpenChemLibEditor openChemLibEditor = new OpenChemLibEditor();
        openChemLibEditor.setValue("difH@BAIVUxZ`@@@");
        assertNotNull(openChemLibEditor);
        assertNotNull(openChemLibEditor.getValue());
    }
    
}
