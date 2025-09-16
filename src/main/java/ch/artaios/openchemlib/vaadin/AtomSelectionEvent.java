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