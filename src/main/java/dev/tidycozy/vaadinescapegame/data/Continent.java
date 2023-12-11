package dev.tidycozy.vaadinescapegame.data;

public enum Continent {

    AFRICA("Africa"),
    ANTARCTICA("Antarctica"),
    ASIA("Asia"),
    AUSTRALIA("Australia"),
    EUROPE("Europe"),
    NORTH_AMERICA("North America"),
    SOUTH_AMERICA("South America");

    private final String displayName;

    private Continent(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
