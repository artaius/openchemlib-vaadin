package ch.artaios.openchemlib.vaadin;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;


@Route("structureviewoldtestview")
public class StructureViewOldTestView extends VerticalLayout {
    public StructureViewOldTestView() {
        StructureViewOld structureView1 = new StructureViewOld(true);
        structureView1.setValue("difH@BAIVUxZ`@@@");
        add(structureView1);
    }
}
