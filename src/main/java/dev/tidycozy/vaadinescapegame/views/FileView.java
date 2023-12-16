package dev.tidycozy.vaadinescapegame.views;

import com.vaadin.flow.theme.lumo.LumoUtility;
import dev.tidycozy.vaadinescapegame.components.MiniGameView;
import dev.tidycozy.vaadinescapegame.components.PersonForm;
import dev.tidycozy.vaadinescapegame.data.Person;
import dev.tidycozy.vaadinescapegame.events.MiniGameDoneEvent;
import dev.tidycozy.vaadinescapegame.services.PersonService;
import dev.tidycozy.vaadinescapegame.session.SessionData;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * This view demonstrates {@link Grid}, {@link com.vaadin.flow.component.formlayout.FormLayout},
 * {@link com.vaadin.flow.component.formlayout.FormLayout} and {@link com.vaadin.flow.data.binder.BeanValidationBinder}
 * Vaadin component and API.
 */
@PageTitle("Vaadin Escape Game")
@Route(value = "file", layout = LobbyView.class)
public class FileView extends MiniGameView {

    public static final int FILE_VIEW_DIGIT = 1;

    private final TextField filterField = new TextField("Filter by name");

    private final Grid<Person> grid = new Grid<>(Person.class);

    private final PersonForm personForm = new PersonForm();

    private final PersonService personService;

    public FileView(PersonService personService) {
        this.personService = personService; // Injection of a Spring Service

        setSizeFull();

        configureFilterLayout();
        configureGridAndFormLayout();
    }

    private void configureFilterLayout() {
        HorizontalLayout filterLayout = new HorizontalLayout();
        filterLayout.setPadding(true);
        filterLayout.setWidthFull();
        filterLayout.addClassName(LumoUtility.Background.BASE);

        filterField.setClearButtonVisible(true);
        filterField.setValueChangeMode(ValueChangeMode.LAZY);
        filterField.addValueChangeListener(event -> updateGrid());
        filterLayout.add(filterField);

        add(filterLayout);
    }

    private void configureGridAndFormLayout() {
        grid.setSizeFull();
        grid.setColumns("firstName", "lastName", "birthDate", "location");
        grid.addColumn(person -> person.isShowSecret() ? person.getSecret() : "###SECRET###").setHeader("Secret");
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                openForm(event.getValue());
            } else {
                closeForm();
            }
        });

        personForm.setWidth(25, Unit.EM);
        personForm.addPersonFormEventListener(event -> {
            if (event.getEventType().equals(PersonForm.PersonFormEventType.SAVE)) {
                savePerson(event.getPerson());
            } else {
                closeForm();
            }
        });

        HorizontalLayout gridAndFormLayout = new HorizontalLayout(grid, personForm);
        gridAndFormLayout.setPadding(true);
        gridAndFormLayout.addClassName(LumoUtility.Background.BASE);
        gridAndFormLayout.setFlexGrow(2, grid);
        gridAndFormLayout.setFlexGrow(1, personForm);
        gridAndFormLayout.setSizeFull();

        add(gridAndFormLayout);
    }

    private void updateGrid() {
        grid.setItems(personService.findPersons(sessionData.getPersonDatabase(), filterField.getValue()));
    }

    private void openForm(Person person) {
        try {
            personForm.setPerson(person.clone());
            personForm.setVisible(true);
        } catch (CloneNotSupportedException e) {
            Notification notification = Notification.show(
                    "Unable to edit the person: " + e.getMessage(), -1, Notification.Position.TOP_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void closeForm() {
        personForm.setVisible(false);
    }

    private void savePerson(Person person) {
        personService.savePerson(sessionData.getPersonDatabase(), person);
        updateGrid();
        checkSecretShown(person);
    }

    private void checkSecretShown(Person person) {
        if (person.getId().equals(SessionData.ID_SUSPECT) && person.isShowSecret()) {
            showProgressionNotification("That's the second digit! Let's continue the investigation.");
            ComponentUtil.fireEvent(
                    getUI().get(), new MiniGameDoneEvent(this, MiniGameDoneEvent.Minigames.FILE));
        }
    }

    @Override
    protected void customViewAttachCalls() {
        if (!sessionData.isChatUnlock()) {
            showProgressionNotification(
                    "What was the name mentioned on that post-it again? " +
                            "Maybe I can find that person in my list of suspects.");
        }

        // Loading grid data
        updateGrid();

        // Closing the form by default when displaying the view
        closeForm();
    }

}

