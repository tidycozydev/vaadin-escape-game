package dev.tidycozy.vaadinescapegame.components;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinSession;
import dev.tidycozy.vaadinescapegame.session.SessionData;
import dev.tidycozy.vaadinescapegame.views.TitleScreenView;

/**
 * An abstract view providing useful methods such as displaying notification and getting the {@link SessionData} object.
 */
public abstract class MiniGameView extends VerticalLayout {

    protected SessionData sessionData;

    protected void showProgressionNotification(String text) {
        Notification notification = Notification.show(text, 6000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
    }

    protected void showWarningNotification(String text) {
        Notification notification = Notification.show(text, 6000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        sessionData = (SessionData) VaadinSession.getCurrent().getAttribute(SessionData.SESSION_DATA_ATTRIBUTE);
        if (sessionData == null) {
            getUI().get().navigate(TitleScreenView.class);
        } else {
            customViewAttachCalls();
        }
    }

    protected abstract void customViewAttachCalls();
}
