package dev.tidycozy.vaadinescapegame.views;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import dev.tidycozy.vaadinescapegame.components.MiniGameView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import dev.tidycozy.vaadinescapegame.session.SessionData;

import java.util.Arrays;

/**
 * This view demonstrates {@link IntegerField} and {@link ConfirmDialog} Vaadin component.
 */
@PageTitle("Vaadin Escape Game")
@Route(value = "exit", layout = LobbyView.class)
public class ExitView extends MiniGameView {

    private final IntegerField firstField = new IntegerField();

    private final IntegerField secondField = new IntegerField();

    private final IntegerField thirdField = new IntegerField();

    private final IntegerField fourthField = new IntegerField();

    public ExitView() {
        setSizeFull();

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeUndefined();
        layout.addClassName(LumoUtility.Background.BASE);

        layout.add(new Paragraph("Locker's code"));

        configureFields(firstField, secondField, thirdField, fourthField);
        layout.add(firstField, secondField, thirdField, fourthField);
        layout.setHorizontalComponentAlignment(Alignment.CENTER, firstField, secondField, thirdField, fourthField);

        Button button = new Button("Open", new Icon(VaadinIcon.UNLOCK));
        button.setIconAfterText(true);
        button.addClickShortcut(Key.ENTER);
        button.addClickListener(event -> tryOpenTheDoor());
        layout.add(button);

        add(layout);
        setHorizontalComponentAlignment(Alignment.CENTER, layout);
    }

    private void configureFields(IntegerField... fields) {
        Arrays.stream(fields).forEach(field -> {
            field.setStepButtonsVisible(true);
            field.setMin(0);
            field.setMax(9);
            field.setValue(0);
        });
    }

    @Override
    protected void customViewAttachCalls() {
        if (sessionData.isFirstVisitToLocker()) {
            sessionData.setFirstVisitToLocker(false);

            showProgressionNotification(
                    "Time to go back home! I just need what's in my locker but I forgot the code... Again!");
        }
    }

    private void tryOpenTheDoor() {
        if (codeCorrect()) {
            showEndGameDialog();
        } else {
            showWarningNotification("That's not the code... or you are trying a brut force =)");
        }
    }

    private boolean codeCorrect() {
        return sessionData.isCaseFinished()
                && firstField.getValue().equals(3)
                && secondField.getValue().equals(1)
                && thirdField.getValue().equals(8)
                && fourthField.getValue().equals(5);
    }

    private void showEndGameDialog() {
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setCloseOnEsc(false);
        confirmDialog.setHeader("Congratulation " + sessionData.getPlayerName() + "!");
        confirmDialog.setText("Thank you for playing, hope you have been enjoying it.");
        confirmDialog.setConfirmText("Quit");

        confirmDialog.addConfirmListener(event -> {
            VaadinSession.getCurrent().setAttribute(SessionData.SESSION_DATA_ATTRIBUTE, null);
            getUI().get().getPage().setLocation("https://tidycozy.dev");
        });

        confirmDialog.open();
    }
}
