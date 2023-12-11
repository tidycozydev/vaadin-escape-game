package dev.tidycozy.vaadinescapegame.components;

import com.vaadin.flow.component.textfield.TextField;

/**
 * This component is a Vaadin {@link TextField} allowing to display or to hide the actual value of the field.
 */
public class SecretTextField extends TextField {

    private String readOnlyValue;

    public SecretTextField(String label) {
        super(label);
    }

    public void setReadOnlyValue(String readOnlyValue) {
        this.readOnlyValue = readOnlyValue;
    }

    public void updatePresentationValue(boolean showValue) {
        super.setPresentationValue(showValue ? readOnlyValue : "###SECRET###");
    }

    @Override
    public String getValue() {
        return readOnlyValue;
    }
}
