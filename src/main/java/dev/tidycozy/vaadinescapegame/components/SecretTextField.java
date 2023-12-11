package dev.tidycozy.vaadinescapegame.components;

import com.vaadin.flow.component.textfield.TextField;

/**
 * This component is an override of the Vaadin {@link TextField} to handle
 * the presentation of a secret value.
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
