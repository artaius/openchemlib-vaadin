package ch.artaios.openchemlib.vaadin;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

/**
 * Project: openchemlib-vaadin
 * Date:    17.10.24
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

@Route("structureviewoldtestview")
public class StructureViewOldTestView extends VerticalLayout {
    public StructureViewOldTestView() {
        StructureViewOld structureViewOld = new StructureViewOld();
        structureViewOld.setValue("gJX@@eKU@@");
        add(structureViewOld);
    }
}
