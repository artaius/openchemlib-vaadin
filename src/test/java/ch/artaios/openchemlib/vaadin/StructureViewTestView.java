package ch.artaios.openchemlib.vaadin;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;


@Route("structureviewtestview")
public class StructureViewTestView extends VerticalLayout {

    private StructureEditor sv2;

    public StructureViewTestView() {
        StructureView structureView1 = new StructureView(false, true);
        structureView1.setValue(StructureEditor.PRESENTATION_TO_MODEL.apply(null, "difH@BAIVUxZ`@@@"));
        structureView1.setWidth(200, Unit.POINTS);
        structureView1.setHeight(150, Unit.POINTS);
        add(structureView1);

        StructureView structureView2 = new StructureView(false, true);
        structureView2.setWidth(200, Unit.POINTS);
        structureView2.setHeight(150, Unit.POINTS);
        structureView2.addValueChangeListener(event -> {
            System.out.println("StructureView2 change event: " + structureView2.getValue().getIDCode());
        });
        add(structureView2);

        StructureView structureView3 = new StructureView(true, false);
        structureView3.setValue(StructureEditor.PRESENTATION_TO_MODEL.apply(null, "deVH@JAIgeQfj@@@MhS}xq| !BBKtH?Gy?BKtH_Gz?BKtHoWz?"));
        structureView3.setWidth(200, Unit.POINTS);
        structureView3.setHeight(150, Unit.POINTS);
        add(structureView3);

        StructureEditorDialog structureEditorDialog = new StructureEditorDialog(false);
        structureEditorDialog.addValueChangeListener(event -> {
            System.out.println("StructureEditorDialog change event: " + structureEditorDialog.structureView.getValue().getIDCode());
        });
        add(structureEditorDialog);

    }
}
