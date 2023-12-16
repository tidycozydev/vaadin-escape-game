package dev.tidycozy.vaadinescapegame.components;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.progressbar.ProgressBar;

/**
 * A custom notification that doesn't display any text, just a loading bar for the specified duration.
 */
public class LoadingNotification extends Notification {

    public LoadingNotification(int duration) {
        super();

        ProgressBar progressBar = new ProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setMinWidth(200, Unit.PIXELS);

        setPosition(Notification.Position.TOP_CENTER);
        setDuration(duration);
        add(progressBar);
    }
}
