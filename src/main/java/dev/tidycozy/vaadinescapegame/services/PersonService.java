package dev.tidycozy.vaadinescapegame.services;

import dev.tidycozy.vaadinescapegame.data.Person;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * We don't deploy any database for this app. This service is just an example of
 * how to use a Spring Service with Vaadin and use the IoC.
 * That is why we pass a dumb database as a parameter of each method.
 */
@Service
public class PersonService {

    public List<Person> findPersons(List<Person> dumbDatabase, String filterValue) {
        return dumbDatabase
                .stream()
                .filter(person -> person.getFirstName().toLowerCase().contains(filterValue.toLowerCase())
                        || person.getLastName().toLowerCase().contains(filterValue.toLowerCase()))
                .collect(Collectors.toList());
    }

    public void savePerson(List<Person> dumbDatabase, Person personUpToDate) {
        Optional<Person> personOutOfDate =
                dumbDatabase
                        .stream()
                        .filter(person -> person.getId().equals(personUpToDate.getId()))
                        .findFirst();

        personOutOfDate.ifPresent(person -> {
            person.setFirstName(personUpToDate.getFirstName());
            person.setLastName(personUpToDate.getLastName());
            person.setBirthDate(personUpToDate.getBirthDate());
            person.setLocation(personUpToDate.getLocation());
            person.setShowSecret(personUpToDate.isShowSecret());
        });
    }
}
