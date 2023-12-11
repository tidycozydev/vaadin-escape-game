package dev.tidycozy.vaadinescapegame.views;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import dev.tidycozy.vaadinescapegame.components.LoadingNotification;
import dev.tidycozy.vaadinescapegame.session.SessionData;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

/**
 * First view displaying a dialog to collect the player's name.
 *
 * The object {@link SessionData} which hold game progression is created when the player specify its name.
 */
@PageTitle("Vaadin Escape Game")
@Route(value = "")
public class TitleScreenView extends VerticalLayout {

    private final Dialog dialog = new Dialog();

    private final FormLayout formLayout = new FormLayout();

    private final TextField nameField = new TextField("Player name");

    private final Button startButton = new Button("Start");

    public TitleScreenView() {
        setSizeFull();
        setSpacing(false);

        configureDialog();
        configureForm();
        configureFooter();

        dialog.open();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        SessionData sessionData =
                (SessionData) VaadinSession.getCurrent().getAttribute(SessionData.SESSION_DATA_ATTRIBUTE);

        // If the player is trying to come back to that screen when already
        // into the escape game, we force him back in the game
        if (sessionData != null) {
            Notification notification =
                    new Notification("Do you think you can escape that easily " + sessionData.getPlayerName() + "?");
            notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
            notification.setPosition(Notification.Position.TOP_CENTER);
            notification.setDuration(4000);
            notification.open();

            // When a component is attached (onAttach method) getUI() always returns
            // the UI instance, therefore we don't use ifPresent()
            getUI().get().navigate(ExitView.class);
        }
    }

    private void configureDialog() {
        dialog.setCloseOnOutsideClick(false);
        dialog.setCloseOnEsc(false);

        add(dialog);
    }

    private void configureForm() {
        nameField.setClearButtonVisible(true);
        nameField.setValueChangeMode(ValueChangeMode.EAGER);
        nameField.addValueChangeListener(event -> startButton.setEnabled(!event.getValue().isEmpty()));
        nameField.focus();

        formLayout.add(nameField);

        dialog.add(formLayout);
    }

    private void configureFooter() {
        HorizontalLayout footerLayout = new HorizontalLayout();
        footerLayout.setWidthFull();
        footerLayout.setSpacing(true);

        Anchor anchor = new Anchor("https://tidycozy.dev", "Go back");

        startButton.setEnabled(false);
        startButton.addClickShortcut(Key.ENTER);
        startButton.addClickListener(event -> clickStartButton());

        footerLayout.add(anchor, startButton);
        footerLayout.expand(anchor);
        footerLayout.setAlignItems(Alignment.CENTER);

        dialog.getFooter().add(footerLayout);
    }

    private void clickStartButton() {
        dialog.setEnabled(false);

        SessionData sessionData = new SessionData();
        sessionData.setPlayerName(nameField.getValue());
        VaadinSession.getCurrent().setAttribute(SessionData.SESSION_DATA_ATTRIBUTE, sessionData);

        // Pseudo loading notification for aesthetic reason
        LoadingNotification loadingNotification = new LoadingNotification(4000);
        loadingNotification.open();
        loadingNotification.addOpenedChangeListener(event -> {
            if (!event.isOpened()) {
                getUI().get().navigate(ExitView.class);
            }
        });
    }

}
