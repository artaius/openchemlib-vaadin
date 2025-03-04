package ch.artaios.openchemlib.vaadin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;

import java.util.List;
import java.util.Map;

@DomEvent("ocl-selectionchange")
public class AtomSelectionEvent extends ComponentEvent<OpenChemLibEditor<?>> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final String mode;
    private final String selectedAtomsJson;

    public AtomSelectionEvent(
            OpenChemLibEditor<?> source,
            boolean fromClient,
            @EventData("event.detail.mode") String mode,
            @EventData("event.detail.selectedAtoms") String selectedAtomsJson) {
        super(source, fromClient);
        this.mode = mode;
        this.selectedAtomsJson = selectedAtomsJson;
    }

    public String getMode() {
        return mode;
    }

    public String getSelectedAtomsJson() {
        return selectedAtomsJson;
    }

    public Map<String, List<List<Integer>>> getSelectedReactionAtoms() {
        try {
            if (mode.equals(OpenChemLibEditor.Mode.REACTION.toString().toLowerCase())) {
                return objectMapper.readValue(selectedAtomsJson, new TypeReference<Map<String, List<List<Integer>>>>() {});
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse selected atoms JSON: " + selectedAtomsJson, e);
        }
    }

    public List<Integer> getSelectedMoleculeAtoms() {
        try {
            if (mode.equals(OpenChemLibEditor.Mode.MOLECULE.toString().toLowerCase())) {
                return objectMapper.readValue(selectedAtomsJson, new TypeReference<List<Integer>>() {
                });
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse selected atoms JSON: " + selectedAtomsJson, e);
        }
    }
}