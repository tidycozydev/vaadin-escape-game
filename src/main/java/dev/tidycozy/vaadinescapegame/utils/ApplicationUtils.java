package dev.tidycozy.vaadinescapegame.utils;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;

public class ApplicationUtils {

    public static LocalDate generateRandomDate() {
        long startEpochDay = LocalDate.of(1950, 1,1).toEpochDay();
        long endEpochDay = LocalDate.of(2010,12,31).toEpochDay();
        long randomDay = ThreadLocalRandom
                .current()
                .nextLong(startEpochDay, endEpochDay);
        return LocalDate.ofEpochDay(randomDay);
    }

    public static int generateRandomInt(int origin, int bound) {
        return RandomGenerator.getDefault().nextInt(origin, bound);
    }
}
