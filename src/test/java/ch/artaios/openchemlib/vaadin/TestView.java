package ch.artaios.openchemlib.vaadin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class TestView  extends VerticalLayout {

    private StructureEditor sv2;

    public TestView() {
        final Dialog test1 = new Dialog("Test");
        final StructureView structureView = new StructureView(true);
        structureView.setValue("dkL@@DiUUUjjjj@H");
        test1.add(structureView);
        add(test1);

        final Button button1 = new Button("Change");
        button1.addClickListener(event -> structureView.setValue("difH@BAIVUxZ`@@@"));
        test1.getFooter().add(button1);

        final Button button5 = new Button("Dialog");
        button5.addClickListener(event -> test1.open());
        add(button5);


        final StructureEditor sv1 = new StructureEditor(true);
        sv1.setValue("difH@BAIVUxZ`@@@");
        add(sv1);

        final Button button2 = new Button("Change First");
        button2.addClickListener(event -> {
            sv1.setValue("dkL@@DiUUUjjjj@H");
        });
        add(button2);

        final Button button4 = new Button("Change Fragment First");
        button4.addClickListener(event -> {
            System.out.println(sv1.isFragment());
            sv1.setFragment(!sv1.isFragment());
        });
        add(button4);

        final Button button = new Button("Add Second");
        button.addClickListener(event -> {
            sv2 = new StructureEditor();
            sv2.setValue("dkL@@DiUUUjjjj@H");
            add(sv2);
        });
        add(button);

        final Button button3 = new Button("Change Second");
        button3.addClickListener(event -> {
            sv2.setValue("difH@BAIVUxZ`@@@");
        });
        add(button3);

        final StructureView structureView1 = new StructureView(true);
        structureView1.setValue("dkL@@DiUUUjjjj@H");
        add(structureView1);

        final StructureView structureView2 = new StructureView();
        structureView2.setValue("difH@BAIVUxZ`@@@");
        add(structureView2);

        final StructureEditor structureEditor2 = new StructureEditor(true, true);
        structureEditor2.addValueChangeListener(event -> {
            System.out.println("StructureEditor2 change event: " + structureEditor2.getValue());
            structureView2.setValue(structureEditor2.getValue());
        });
        add(structureEditor2);


        final Button test = new Button("Set idcode");
        test.addClickListener(event -> structureEditor2.setValue("dkL@@DiUUUjjjj@H"));
        add(test);

        final Button test2 = new Button("Get idcode");
        test2.addClickListener(event -> System.out.println(structureEditor2.getValue()));
        add(test2);

        final Button sm = new Button("Set smiles");
        sm.addClickListener(event -> structureView1.setSmiles("C1CCCC1N"));
        add(sm);

        final Button sm2 = new Button("Set smiles2");
        sm2.addClickListener(event -> structureView1.setSmiles("C1CCCC1O"));
        add(sm2);

        final Button fon = new Button("Fragment on");
        fon.addClickListener(event -> structureEditor2.setFragment(true));
        add(fon);
        final Button foff = new Button("Fragment off");
        foff.addClickListener(event -> structureEditor2.setFragment(false));
        add(foff);

        final Button editable = new Button("set editable");
        editable.addClickListener(event -> structureView1.setEditable(!structureView1.getEditable()));
        add(editable);


        final StructureEditorDialog structureEditorDialog = new StructureEditorDialog();
        structureEditorDialog.addValueChangeListener(event -> System.out.println(structureEditorDialog.getIdCode()));
        add(structureEditorDialog);

    }
}
