package ch.artaios.openchemlib.vaadin;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;


@Route("editortestview")
@RouteAlias("")
public class EditorTestView extends VerticalLayout {

    public EditorTestView() {
        final TextField idcode = new TextField("IdCode");
        idcode.setWidthFull();
        idcode.setReadOnly(true);

        final StructureEditor structureEditor = new StructureEditor(false);
        structureEditor.setValue(ChemUtils.getStereoMolecule("difH@BAIVUxZ`@@@"));
        structureEditor.addValueChangeListener(event -> {
            System.out.println("StructureEditor change event: " + ChemUtils.getIdcode(structureEditor.getValue()));
            idcode.setValue(ChemUtils.getIdcode(event.getValue()));
        });

        final Select<String> setIdCode = new Select<>("Set IdCode", e -> {
            structureEditor.setValue(ChemUtils.getStereoMolecule(e.getValue()));
        }, "difH@BAIVUxZ`@@@", "ffc`P@H`QxNQQJJIJIZJHiSkQSejB`jFjhhaEqFUh@", "gJX@@eKU@P gGQHDHaImfhB!defH@DAIfUVjj`B", "gJX@@eKU@@ gGQHDHaImfh@!defH@DAIfUVjj`@");

        structureEditor.setWidth(45, Unit.VW);
//        structureEditor.setSizeFull();
        setIdCode.setWidthFull();

        VerticalLayout structureEditorLayout = new VerticalLayout(structureEditor, idcode, setIdCode);
        structureEditorLayout.setSpacing(false);
        structureEditorLayout.setPadding(false);

        // ---

        final ReactionEditor reactionEditor = new ReactionEditor(false);
        reactionEditor.setValue(ChemUtils.getReaction("gJX@@eKU@@ gGQHDHaImfh@!defH@DAIfUVjj`@"));
        reactionEditor.addValueChangeListener(event -> System.out.println("ReactionEditor change event: " + reactionEditor.getValue()));
        reactionEditor.setWidth(45, Unit.VW);
//        reactionEditor.setSizeFull();

        VerticalLayout reactionEditorLayout = new VerticalLayout(reactionEditor);
        reactionEditorLayout.setSpacing(false);
        reactionEditorLayout.setPadding(false);

        // ---

        add(new HorizontalLayout(structureEditorLayout, reactionEditorLayout));

        // ---

        Checkbox editable = new Checkbox("Editable", true, event -> {
            structureEditor.setEditable(event.getValue());
            reactionEditor.setEditable(event.getValue());
        });

        Checkbox fragment = new Checkbox("Fragment", event -> {
            structureEditor.setFragment(event.getValue());
            reactionEditor.setFragment(event.getValue());
        });

        add(new HorizontalLayout(editable, fragment));

        // ---

        StructureView structureView = new StructureView(false, true);
        structureView.setWidth(20, Unit.EM);
        structureView.setHeight(15, Unit.EM);
        add(structureView);

        // ---

        StructureEditorDialog structureEditorDialog = new StructureEditorDialog(false);
        structureEditorDialog.setWidth(20, Unit.EM);
        structureEditorDialog.setHeight(15, Unit.EM);
        add(structureEditorDialog);

        // ---

        add(new Hr());

        add(new H1("Legacy Components"));
        final StructureViewOld legacyStructureView = new StructureViewOld(true);
        legacyStructureView.setValue("difH@BAIVUxZ`@@@");
        legacyStructureView.addValueChangeListener(event -> System.out.println("legacyStructureView change event: " + legacyStructureView.getValue()));
        add(legacyStructureView);

//        setWidth(450, Unit.PIXELS);
        setSpacing(false);
    }
}
