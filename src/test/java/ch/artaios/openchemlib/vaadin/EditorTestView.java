package ch.artaios.openchemlib.vaadin;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;


@Route("editortestview")
@RouteAlias("")
public class EditorTestView extends VerticalLayout {

    private StructureEditorOld sv2;

    public EditorTestView() {
        final TextField idcode = new TextField();
        idcode.setWidthFull();

        final OpenChemLibEditor openChemLibEditor = new OpenChemLibEditor(OpenChemLibEditor.Mode.MOLECULE, true, true, false);
        openChemLibEditor.setValue("difH@BAIVUxZ`@@@");
        openChemLibEditor.addValueChangeListener(event -> {
            System.out.println("OpenChemLibEditor change event: " + openChemLibEditor.getValue());
            idcode.setValue(event.getValue());
        });
        openChemLibEditor.setWidthFull();
        add(openChemLibEditor);
        add(idcode);

        final Select<String> setIdCode = new Select<>("Set IdCode", e -> {
            openChemLibEditor.setValue(e.getValue());
        }, "difH@BAIVUxZ`@@@", "ffc`P@H`QxNQQJJIJIZJHiSkQSejB`jFjhhaEqFUh@", "gJX@@eKU@P gGQHDHaImfhB!defH@DAIfUVjj`B", "gJX@@eKU@@ gGQHDHaImfh@!defH@DAIfUVjj`@");
        setIdCode.setWidthFull();
        add(setIdCode);

        add(new TextField());

        final OpenChemLibEditor openChemLibEditor2 = new OpenChemLibEditor();
        openChemLibEditor2.setReadonly(false);
        openChemLibEditor2.setMode(OpenChemLibEditor.Mode.REACTION);
        openChemLibEditor2.setValue("gJX@@eKU@@ gGQHDHaImfh@!defH@DAIfUVjj`@");
        openChemLibEditor2.addValueChangeListener(event -> System.out.println("OpenChemLibEditor2 change event: " + openChemLibEditor2.getValue()));
        add(openChemLibEditor2);

        add(new Checkbox("Mode Reaction", event -> {
            final OpenChemLibEditor.Mode mode = event.getValue() ? OpenChemLibEditor.Mode.REACTION : OpenChemLibEditor.Mode.MOLECULE;
            openChemLibEditor.setMode(mode);
            openChemLibEditor2.setMode(mode);
        }));

        add(new Checkbox("Readonly", event -> {
            openChemLibEditor.setReadonly(event.getValue());
            openChemLibEditor2.setReadonly(event.getValue());
        }));

        add(new Checkbox("Editable", true, event -> {
            openChemLibEditor.setEditable(event.getValue());
            openChemLibEditor2.setEditable(event.getValue());
        }));

        add(new Checkbox("Fragment", event -> {
            openChemLibEditor.setFragment(event.getValue());
            openChemLibEditor2.setFragment(event.getValue());
        }));

        add(new Hr());

        final StructureEditorOld legacyStructureEditorOld = new StructureEditorOld(true);
        legacyStructureEditorOld.setValue("difH@BAIVUxZ`@@@");
        legacyStructureEditorOld.setFragment(true);
        legacyStructureEditorOld.addValueChangeListener(event -> System.out.println("legacyStructureEditor change event: " + legacyStructureEditorOld.getValue()));
        add(legacyStructureEditorOld);

//
//        final Button button2 = new Button("Change First");
//        button2.addClickListener(event -> {
//            legacyStructureEditor.setValue("dkL@@DiUUUjjjj@H");
//        });
//        add(button2);
//
//        final Button button4 = new Button("Change Fragment First");
//        button4.addClickListener(event -> {
//            System.out.println(legacyStructureEditor.isFragment());
//            legacyStructureEditor.setFragment(!legacyStructureEditor.isFragment());
//        });
//        add(button4);
//
//        final Button button = new Button("Add Second");
//        button.addClickListener(event -> {
//            sv2 = new StructureEditor();
//            sv2.setValue("dkL@@DiUUUjjjj@H");
//            add(sv2);
//        });
//        add(button);
//
//        final Button button3 = new Button("Change Second");
//        button3.addClickListener(event -> {
//            sv2.setValue("difH@BAIVUxZ`@@@");
//        });
//        add(button3);
//
//        final StructureView structureView1 = new StructureView(true);
//        structureView1.setValue("dkL@@DiUUUjjjj@H");
//        add(structureView1);
//
//        final StructureView structureView2 = new StructureView();
//        structureView2.setValue("difH@BAIVUxZ`@@@");
//        add(structureView2);
//
//        final StructureEditor structureEditor2 = new StructureEditor(true, true);
//        structureEditor2.addValueChangeListener(event -> {
//            System.out.println("StructureEditor2 change event: " + structureEditor2.getValue());
//            structureView2.setValue(structureEditor2.getValue());
//        });
//        add(structureEditor2);
//
//
//        final Button test = new Button("Set idcode");
//        test.addClickListener(event -> structureEditor2.setValue("dkL@@DiUUUjjjj@H"));
//        add(test);
//
//        final Button test2 = new Button("Get idcode");
//        test2.addClickListener(event -> System.out.println(structureEditor2.getValue()));
//        add(test2);
//
//        final Button sm = new Button("Set smiles");
//        sm.addClickListener(event -> structureView1.setSmiles("C1CCCC1N"));
//        add(sm);
//
//        final Button sm2 = new Button("Set smiles2");
//        sm2.addClickListener(event -> structureView1.setSmiles("C1CCCC1O"));
//        add(sm2);
//
//        final Button fon = new Button("Fragment on");
//        fon.addClickListener(event -> structureEditor2.setFragment(true));
//        add(fon);
//        final Button foff = new Button("Fragment off");
//        foff.addClickListener(event -> structureEditor2.setFragment(false));
//        add(foff);
//
//        final Button editable = new Button("set editable");
//        editable.addClickListener(event -> structureView1.setEditable(!structureView1.getEditable()));
//        add(editable);
//
//
//        final StructureEditorDialog structureEditorDialog = new StructureEditorDialog();
//        structureEditorDialog.addValueChangeListener(event -> System.out.println(structureEditorDialog.getIdCode()));
//        add(structureEditorDialog);

        setWidth(450, Unit.PIXELS);
    }
}
