package dev.tidycozy.vaadinescapegame.components;

import dev.tidycozy.vaadinescapegame.data.Continent;
import dev.tidycozy.vaadinescapegame.data.Person;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;

/**
 * Vaadin {@link FormLayout} and {@link BeanValidationBinder} are used in this form allowing to edit a {@link Person}.
 */
public class PersonForm extends FormLayout {

    private final SecretTextField secret = new SecretTextField("Secret");

    private final Checkbox showSecret = new Checkbox("Show secret");

    private final BeanValidationBinder<Person> binder = new BeanValidationBinder<>(Person.class);

    public PersonForm() {
        binder.bindInstanceFields(this);

        ComboBox<Continent> location = new ComboBox<>("Location");
        location.setItems(Continent.values());
        secret.setReadOnly(true);
        showSecret.addValueChangeListener(event -> handleSecretDisplay());

        DatePicker birthDate = new DatePicker("Birth date");
        TextField lastName = new TextField("Last name");
        TextField firstName = new TextField("First name");
        add(firstName, lastName, birthDate, location, secret, showSecret);

        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(event -> save());
        Button cancelButton = new Button("Cancel");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClickListener(event -> cancel());

        add(new HorizontalLayout(saveButton, cancelButton));
    }

    public void setPerson(Person person) {
        secret.setReadOnlyValue(person.getSecret().toString());
        binder.setBean(person);
        handleSecretDisplay();
    }

    private void handleSecretDisplay() {
        secret.updatePresentationValue(showSecret.getValue());
    }

    public void addPersonFormEventListener(ComponentEventListener<PersonFormEvent> listener) {
        addListener(PersonFormEvent.class, listener);
    }

    private void save() {
        if (binder.isValid()) {
            fireEvent(new PersonFormEvent(this, binder.getBean(), PersonFormEventType.SAVE));
        }
    }

    private void cancel() {
        fireEvent(new PersonFormEvent(this, binder.getBean(), PersonFormEventType.CLOSE));
    }

    public static class PersonFormEvent extends ComponentEvent<PersonForm> {

        private final Person person;

        private final PersonFormEventType eventType;

        protected PersonFormEvent(PersonForm source, Person person, PersonFormEventType eventType) {
            super(source, false);
            this.person = person;
            this.eventType = eventType;
        }

        public Person getPerson() {
            return person;
        }

        public PersonFormEventType getEventType() {
            return eventType;
        }
    }

    public static enum PersonFormEventType {
        SAVE,
        CLOSE;
    }
}
