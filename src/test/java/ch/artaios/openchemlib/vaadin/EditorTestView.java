package ch.artaios.openchemlib.vaadin;

import com.actelion.research.chem.StereoMolecule;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.util.concurrent.atomic.AtomicBoolean;


@Route("editortestview")
@RouteAlias("")
public class EditorTestView extends VerticalLayout {

    public EditorTestView() {
        AtomicBoolean withinEvent = new AtomicBoolean(false);
        final StructureEditor structureEditor = new StructureEditor(false);

        structureEditor.addAtomSelectionListener(event -> {
            String message = "Atoms selected:\n" + event.getSelectedAtoms();
            System.out.println(message);
        });

        final TextField idcode = new TextField("IdCode");
        idcode.setWidthFull();
        idcode.setReadOnly(true);

        final TextField smiles = new TextField("Smiles");
        smiles.setValueChangeMode(ValueChangeMode.EAGER);
        smiles.setWidthFull();
        smiles.addValueChangeListener(event -> {
            if (event.isFromClient()) {
                try {
                    StereoMolecule stereoMoleculeFromSmiles = ChemUtils.getStereoMoleculeFromSmiles(event.getValue());
                    withinEvent.set(true);
                    structureEditor.setValue(stereoMoleculeFromSmiles);
                    withinEvent.set(false);
                } catch (Exception e) {
                    Notification notification = new Notification(e.getMessage(), 3000);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    notification.open();
                }
            }
        });

        structureEditor.setValue(ChemUtils.getStereoMolecule("difH@BAIVUxZ`@@@"));
        structureEditor.addValueChangeListener(event -> {
            System.out.println("StructureEditor change event: " + ChemUtils.getIdcode(structureEditor.getValue()));
            idcode.setValue(ChemUtils.getIdcode(event.getValue()));
            if(!withinEvent.get()) {
                smiles.setValue(ChemUtils.getSmiles(event.getValue()));
            }
        });

        final Select<String> setIdCode = new Select<>("Set IdCode", e -> {
            structureEditor.setValue(ChemUtils.getStereoMolecule(e.getValue()));
            },
            "difH@BAIVUxZ`@@@",
            "ffc`P@H`QxNQQJJIJIZJHiSkQSejB`jFjhhaEqFUh@",
            "eohZKL@BaekghDLBNAIEMCODj`OBcpX|LddrtbdsTRbrfbRrbrfVNnZjZZjjjhDBhH\\`TbVR@@ !B@k_~@K]}@OzH@k_|?XbH?PJH_[^w@`JHb@JH?]CRSk}Tn{lkpIf@SmKOUKlBbCF?tMK@fQaSFEOPtmCR",
            "fbmiP@DTxIPC^SgHheEDjheEdsdeNBuRsUUUUDtkItC@@ !BS`AgKECRS`APsTx@TMIgKEPDPB[RTD@f?PaN@G}NU@PH?Ee@BOt"
        );

        structureEditor.setWidth(45, Unit.VW);
//        structureEditor.setSizeFull();
        setIdCode.setWidthFull();

        VerticalLayout structureEditorLayout = new VerticalLayout(new H4(structureEditor.getClass().getSimpleName()), structureEditor, idcode, setIdCode, smiles);
        structureEditorLayout.setSpacing(false);
        structureEditorLayout.setPadding(false);

        // ---

        final ReactionEditor reactionEditor = new ReactionEditor(false);
        reactionEditor.setValue(ChemUtils.getReaction("gJX@@eKU@@ gGQHDHaImfh@!defH@DAIfUVjj`@"));
        reactionEditor.addValueChangeListener(event -> System.out.println("ReactionEditor change event: " + reactionEditor.getValue()));
        reactionEditor.setWidth(45, Unit.VW);
//        reactionEditor.setSizeFull();

        VerticalLayout reactionEditorLayout = new VerticalLayout(new H4(reactionEditor.getClass().getSimpleName()), reactionEditor);
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

        add(new Hr());

        // ---

        StructureView structureView = new StructureView(false, true);
        structureView.setWidth(20, Unit.EM);
        structureView.setHeight(15, Unit.EM);

        VerticalLayout structureViewLayout = new VerticalLayout(new H4(structureView.getClass().getSimpleName()), structureView);
        structureViewLayout.setSpacing(false);
        structureViewLayout.setPadding(false);

        // ---

        ReactionView reactionView = new ReactionView(false, true);
        reactionView.setWidth(20, Unit.EM);
        reactionView.setHeight(15, Unit.EM);

        VerticalLayout reactionViewLayout = new VerticalLayout(new H4(reactionView.getClass().getSimpleName()), reactionView);
        reactionViewLayout.setSpacing(false);
        reactionViewLayout.setPadding(false);

        // ---

        StructureEditorDialog structureEditorDialog = new StructureEditorDialog(false);
        structureEditorDialog.setWidth(20, Unit.EM);
        structureEditorDialog.setHeight(15, Unit.EM);

        VerticalLayout structureEditorDialogLayout = new VerticalLayout(new H4(structureEditorDialog.getClass().getSimpleName()), structureEditorDialog);
        structureEditorDialogLayout.setSpacing(false);
        structureEditorDialogLayout.setPadding(false);

        // ---

        ReactionEditorDialog reactionEditorDialog = new ReactionEditorDialog(false);
        reactionEditorDialog.setWidth(20, Unit.EM);
        reactionEditorDialog.setHeight(15, Unit.EM);

        VerticalLayout reactionEditorDialogLayout = new VerticalLayout(new H4(reactionEditorDialog.getClass().getSimpleName()), reactionEditorDialog);
        reactionEditorDialogLayout.setSpacing(false);
        reactionEditorDialogLayout.setPadding(false);

        // ---

        add(new HorizontalLayout(structureViewLayout, reactionViewLayout, structureEditorDialogLayout, reactionEditorDialogLayout));

        // ---

        add(new Hr());

        final StructureViewOld structureViewOld = new StructureViewOld(true);
        structureViewOld.setValue("difH@BAIVUxZ`@@@");
        structureViewOld.addValueChangeListener(event -> System.out.println("structureViewOld change event: " + structureViewOld.getValue()));

        VerticalLayout structureViewOldLayout = new VerticalLayout(new H4(structureViewOld.getClass().getSimpleName()), structureViewOld);
        structureViewOldLayout.setSpacing(false);
        structureViewOldLayout.setPadding(false);

        add(structureViewOldLayout);
    }
}
