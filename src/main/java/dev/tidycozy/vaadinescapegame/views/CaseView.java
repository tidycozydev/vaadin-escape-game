package dev.tidycozy.vaadinescapegame.views;

import com.vaadin.flow.theme.lumo.LumoUtility;
import dev.tidycozy.vaadinescapegame.components.MiniGameView;
import dev.tidycozy.vaadinescapegame.data.Person;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import dev.tidycozy.vaadinescapegame.events.MiniGameDoneEvent;

/**
 * This view demonstrates {@link TabSheet}, {@link Accordion} and {@link TextArea} Vaadin components.
 */
@PageTitle("Vaadin Escape Game")
@Route(value = "case", layout = LobbyView.class)
public class CaseView extends MiniGameView {

    private final H1 title = new H1();
    private final Span name = new Span();
    private final Span birthDate = new Span();
    private final Span location = new Span();

    Registration registration;

    public CaseView() {
        setSizeFull();

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.addClassName(LumoUtility.Background.BASE);

        // We're setting the text in onAttach method to access the player's name
        title.addClassNames("text-l", "m-m");

        TabSheet tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        tabSheet.add("Suspect details", getSuspectDetails());
        tabSheet.add("Case notes", getNotes());

        layout.add(title, tabSheet);

        add(layout);
    }

    private Accordion getSuspectDetails() {
        Accordion accordion = new Accordion();

        VerticalLayout personalInfoLayout = new VerticalLayout();
        personalInfoLayout.add(name, birthDate, location);

        Span address = new Span("Spotted in Montreal not too long ago");
        Span uncertainty = new Span("Investigation says address can change often");

        VerticalLayout addressLayout = new VerticalLayout();
        addressLayout.add(address, uncertainty);

        Span job1 = new Span("Currently works at tidycozy.dev");
        Span job2 = new Span("Had been working for a company called Kosmos");
        Span job3 = new Span("Had previous experiences in France");
        VerticalLayout jobLayout = new VerticalLayout();
        jobLayout.add(job1, job2, job3);

        accordion.add("Personal information", personalInfoLayout);
        accordion.add("Address", addressLayout);
        accordion.add("Jobs", jobLayout);

        return accordion;
    }

    private Component getNotes() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();

        TextArea textArea = new TextArea("Notes");
        textArea.setSizeFull();
        textArea.setValue("Hey myself! Not really related to the case BUT just in case you forget. Our IT contact " +
                "had been working on a macro to help us remember our locker's code last digit . Just tap the first " +
                "three digits, you'll see.\n\n");
        textArea.setValueChangeMode(ValueChangeMode.LAZY);
        textArea.focus();

        registration = textArea.addValueChangeListener(event -> {
            if (textArea.getValue().contains("318")) {
                registration.remove(); // As we write in the text area, we remove the listener to prevent endless loop

                textArea.setValue(
                        textArea.getValue() +
                                "\n\nHi there. I see you forgot your code again! Here is the last digit: 5" +
                                "\nHave a good day!");

                showProgressionNotification(
                        "And finally the fourth digit. Let's open that locker and get out of here.");

                ComponentUtil.fireEvent(
                        getUI().get(), new MiniGameDoneEvent(this, MiniGameDoneEvent.Minigames.CASE));
            }
        });

        layout.add(textArea);

        return layout;
    }

    @Override
    protected void customViewAttachCalls() {
        if (!sessionData.isCaseFinished()) {
            showProgressionNotification(
                    "Let's dig into the case, I've almost retrieved that locker's code.");
        }

        Person suspect = sessionData.getSuspect();

        title.setText("Case number 8: Mr. " + suspect.getLastName());
        name.setText(suspect.getFirstName() + " " + suspect.getLastName());
        birthDate.setText(suspect.getBirthDate().toString());
        location.setText(suspect.getLocation().getDisplayName());
    }
}
