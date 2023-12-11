package dev.tidycozy.vaadinescapegame.data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class Person implements Cloneable {

    @NotNull
    private final Integer id;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotNull
    private LocalDate birthDate;

    @NotNull
    private Continent location;

    @NotNull
    private final Integer secret;

    private boolean showSecret = false;

    public Person(int id, String firstName, String lastName, LocalDate birthDate, Continent location, int secret) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.location = location;
        this.secret = secret;
    }

    public Person clone() throws CloneNotSupportedException {
        return (Person) super.clone();
    }

    public Integer getId() {
        return this.id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Continent getLocation() {
        return location;
    }

    public void setLocation(Continent location) {
        this.location = location;
    }

    public Integer getSecret() {
        return secret;
    }

    public boolean isShowSecret() {
        return showSecret;
    }

    public void setShowSecret(boolean showSecret) {
        this.showSecret = showSecret;
    }
}
