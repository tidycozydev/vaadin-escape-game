package dev.tidycozy.vaadinescapegame.session;

import dev.tidycozy.vaadinescapegame.data.Continent;
import dev.tidycozy.vaadinescapegame.data.Person;
import dev.tidycozy.vaadinescapegame.utils.ApplicationUtils;

import java.util.List;

/**
 * Keeps track of the progress in the game, the player's name
 * and the dumb database for the file mini-game. The instance
 * is stored in the {@link com.vaadin.flow.server.VaadinSession}.
 */
public class SessionData {

    public static final String SESSION_DATA_ATTRIBUTE = "sessionDataAttribute";


    public static final int ID_SUSPECT = 18;
    public static final int SECRET_SUSPECT = 1;

    private String playerName;

    private boolean firstVisitToLocker = true;
    private boolean fileUnlock = false;
    private boolean chatUnlock = false;
    private boolean caseUnlock = false;
    private boolean caseFinished = false;

    private final List<Person> personDatabase = List.of(
            new Person(1, "Patricia", "Smith",
                    ApplicationUtils.generateRandomDate(), Continent.EUROPE,
                    ApplicationUtils.generateRandomInt(0, 9)),
            new Person(2, "Kayne", "Oconnell",
                    ApplicationUtils.generateRandomDate(), Continent.AUSTRALIA,
                    ApplicationUtils.generateRandomInt(0, 9)),
            new Person(3, "Shane", "Weaver",
                    ApplicationUtils.generateRandomDate(), Continent.NORTH_AMERICA,
                    ApplicationUtils.generateRandomInt(0, 9)),
            new Person(4, "Genevieve", "Alexander",
                    ApplicationUtils.generateRandomDate(), Continent.ASIA,
                    ApplicationUtils.generateRandomInt(0, 9)),
            new Person(5, "Mike", "Hicks",
                    ApplicationUtils.generateRandomDate(), Continent.SOUTH_AMERICA,
                    ApplicationUtils.generateRandomInt(0, 9)),
            new Person(6, "Tina", "Roberts",
                    ApplicationUtils.generateRandomDate(), Continent.AFRICA,
                    ApplicationUtils.generateRandomInt(0, 9)),
            new Person(7, "Teddy", "Lee",
                    ApplicationUtils.generateRandomDate(), Continent.NORTH_AMERICA,
                    ApplicationUtils.generateRandomInt(0, 9)),
            new Person(8, "Rex", "English",
                    ApplicationUtils.generateRandomDate(), Continent.EUROPE,
                    ApplicationUtils.generateRandomInt(0, 9)),
            new Person(9, "Elaine", "Douglas",
                    ApplicationUtils.generateRandomDate(), Continent.EUROPE,
                    ApplicationUtils.generateRandomInt(0, 9)),
            new Person(10, "Mateo", "Lawson",
                    ApplicationUtils.generateRandomDate(), Continent.AFRICA,
                    ApplicationUtils.generateRandomInt(0, 9)),
            new Person(11, "Kyron", "Hunter",
                    ApplicationUtils.generateRandomDate(), Continent.ANTARCTICA,
                    ApplicationUtils.generateRandomInt(0, 9)),
            new Person(12, "Cecilia", "Rosario",
                    ApplicationUtils.generateRandomDate(), Continent.SOUTH_AMERICA,
                    ApplicationUtils.generateRandomInt(0, 9)),
            new Person(13, "Astrid", "Conway",
                    ApplicationUtils.generateRandomDate(), Continent.SOUTH_AMERICA,
                    ApplicationUtils.generateRandomInt(0, 9)),
            new Person(14, "Victoria", "Ramirez",
                    ApplicationUtils.generateRandomDate(), Continent.ASIA,
                    ApplicationUtils.generateRandomInt(0, 9)),
            new Person(15, "Anais", "Fields",
                    ApplicationUtils.generateRandomDate(), Continent.AUSTRALIA,
                    ApplicationUtils.generateRandomInt(0, 9)),
            new Person(16, "Madeline", "Cabrera",
                    ApplicationUtils.generateRandomDate(), Continent.EUROPE,
                    ApplicationUtils.generateRandomInt(0, 9)),
            new Person(17, "Rhianna", "Miranda",
                    ApplicationUtils.generateRandomDate(), Continent.NORTH_AMERICA,
                    ApplicationUtils.generateRandomInt(0, 9)),
            new Person(ID_SUSPECT, "Bobby", "Swift",
                    ApplicationUtils.generateRandomDate(), Continent.EUROPE, SECRET_SUSPECT),
            new Person(19, "Yusuf", "Vargas",
                    ApplicationUtils.generateRandomDate(), Continent.AFRICA,
                    ApplicationUtils.generateRandomInt(0, 9)),
            new Person(20, "Brandon", "Harris",
                    ApplicationUtils.generateRandomDate(), Continent.ASIA,
                    ApplicationUtils.generateRandomInt(0, 9)),
            new Person(21, "Evie", "Patrick",
                    ApplicationUtils.generateRandomDate(), Continent.NORTH_AMERICA,
                    ApplicationUtils.generateRandomInt(0, 9)),
            new Person(22, "Madiha", "Andrade",
                    ApplicationUtils.generateRandomDate(), Continent.ANTARCTICA,
                    ApplicationUtils.generateRandomInt(0, 9)),
            new Person(23, "Jayson", "Winters",
                    ApplicationUtils.generateRandomDate(), Continent.AFRICA,
                    ApplicationUtils.generateRandomInt(0, 9)),
            new Person(24, "Lina", "Noble",
                    ApplicationUtils.generateRandomDate(), Continent.ASIA,
                    ApplicationUtils.generateRandomInt(0, 9)),
            new Person(25, "Lawson", "O'Connor",
                    ApplicationUtils.generateRandomDate(), Continent.NORTH_AMERICA,
                    ApplicationUtils.generateRandomInt(0, 9)),
            new Person(26, "Miles", "Lucero",
                    ApplicationUtils.generateRandomDate(), Continent.SOUTH_AMERICA,
                    ApplicationUtils.generateRandomInt(0, 9)),
            new Person(27, "Alasdair", "Solomon",
                    ApplicationUtils.generateRandomDate(), Continent.NORTH_AMERICA,
                    ApplicationUtils.generateRandomInt(0, 9)),
            new Person(28, "Layla", "Pope",
                    ApplicationUtils.generateRandomDate(), Continent.AUSTRALIA,
                    ApplicationUtils.generateRandomInt(0, 9)),
            new Person(29, "Estelle", "Oneal",
                    ApplicationUtils.generateRandomDate(), Continent.AUSTRALIA,
                    ApplicationUtils.generateRandomInt(0, 9)),
            new Person(30, "Bryony", "Buchanan",
                    ApplicationUtils.generateRandomDate(), Continent.EUROPE,
                    ApplicationUtils.generateRandomInt(0, 9))

    );

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public List<Person> getPersonDatabase() {
        return personDatabase;
    }

    public boolean isFirstVisitToLocker() {
        return firstVisitToLocker;
    }

    public void setFirstVisitToLocker(boolean firstVisitToLocker) {
        this.firstVisitToLocker = firstVisitToLocker;
    }

    public boolean isFileUnlock() {
        return fileUnlock;
    }

    public void setFileUnlock(boolean fileUnlock) {
        this.fileUnlock = fileUnlock;
    }

    public boolean isChatUnlock() {
        return chatUnlock;
    }

    public void setChatUnlock(boolean chatUnlock) {
        this.chatUnlock = chatUnlock;
    }

    public boolean isCaseUnlock() {
        return caseUnlock;
    }

    public void setCaseUnlock(boolean caseUnlock) {
        this.caseUnlock = caseUnlock;
    }

    public boolean isCaseFinished() {
        return caseFinished;
    }

    public void setCaseFinished(boolean caseFinished) {
        this.caseFinished = caseFinished;
    }

    public Person getSuspect() {
        return personDatabase
                .stream()
                .filter(person -> person.getId().equals(ID_SUSPECT))
                .findFirst()
                .get();
    }
}
