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

@Route("structureviewtestview")
public class StructureViewTestView extends VerticalLayout {
    public StructureViewTestView() {
        StructureView structureView = new StructureView();
//        structureView.setValue("gJX@@eKU@@ gGQHDHaImfh@!defH@DAIfUVjj`@##!R_vp@[G|S@AL]MHH !Rb@K~@Hc}b@JH?QwRH` !R?g~w?Xc}mpK~_x`Bm?vw?Xc}GYh|##");
        structureView.setValue("gJX@@eKU@@");
        add(structureView);
    }
}