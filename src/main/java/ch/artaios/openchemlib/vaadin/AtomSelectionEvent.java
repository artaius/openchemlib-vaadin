package ch.artaios.openchemlib.vaadin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;

import java.util.List;

@DomEvent("ocl-selectionchange")
public class AtomSelectionEvent extends ComponentEvent<OpenChemLibEditor<?>> {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final String selectedAtomsJson;

    public AtomSelectionEvent(
            OpenChemLibEditor<?> source,
            boolean fromClient,
            @EventData("JSON.stringify(event.detail.selectedAtoms)") String selectedAtomsJson) {
        super(source, fromClient);
        this.selectedAtomsJson = selectedAtomsJson;
    }

    public String getSelectedAtomsJson() {
        return selectedAtomsJson;
    }

    public List<Integer> getSelectedAtoms() {
        try {
            return objectMapper.readValue(selectedAtomsJson, new TypeReference<List<Integer>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse selected atoms JSON: " + selectedAtomsJson, e);
        }
    }
}