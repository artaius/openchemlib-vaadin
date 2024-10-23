package ch.artaios.openchemlib.vaadin;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;


@Route("structureeditortestview")
@RouteAlias("")
public class StructureEditorTestView extends VerticalLayout {

    private StructureEditor sv2;

    public StructureEditorTestView() {
        final TextField idcode = new TextField();
        idcode.setWidthFull();

        final StructureEditor structureEditor = new StructureEditor(false);
        structureEditor.setValue(StructureEditor.PRESENTATION_TO_MODEL.apply(null, "difH@BAIVUxZ`@@@"));
        structureEditor.addValueChangeListener(event -> {
            System.out.println(this.getClass().getSimpleName() + " change event: " + structureEditor.getValue().getIDCode());
            idcode.setValue(event.getValue().getIDCode());
        });
        structureEditor.setWidthFull();
        add(structureEditor);
        add(idcode);


        final Select<String> setIdCode = new Select<>("Set IdCode", e -> {
            structureEditor.setValue(StructureEditor.PRESENTATION_TO_MODEL.apply(null, e.getValue()));
        }, "difH@BAIVUxZ`@@@", "ffc`P@H`QxNQQJJIJIZJHiSkQSejB`jFjhhaEqFUh@", "gJX@@eKU@P gGQHDHaImfhB!defH@DAIfUVjj`B", "gJX@@eKU@@ gGQHDHaImfh@!defH@DAIfUVjj`@");
        setIdCode.setWidthFull();
        add(setIdCode);

        add(new TextField());

        add(new Checkbox("Mode Reaction", event -> {
            final OpenChemLibEditor.Mode mode = event.getValue() ? OpenChemLibEditor.Mode.REACTION : OpenChemLibEditor.Mode.MOLECULE;
            structureEditor.setMode(mode);
        }));

        add(new Checkbox("Readonly", event -> {
            structureEditor.setReadonly(event.getValue());
        }));

        add(new Checkbox("Editable", true, event -> {
            structureEditor.setEditable(event.getValue());
        }));

        add(new Checkbox("Fragment", event -> {
            structureEditor.setFragment(event.getValue());
        }));

        setWidth(450, Unit.PIXELS);
    }
}
