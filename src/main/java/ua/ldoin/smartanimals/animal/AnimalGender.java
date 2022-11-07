package ua.ldoin.smartanimals.animal;

import java.util.concurrent.ThreadLocalRandom;

public enum AnimalGender {

    MALE('b', '♂'),
    FEMALE('d', '♀');

    private final char color;
    private final char symbol;

    AnimalGender(char color, char symbol) {

        this.color = color;
        this.symbol = symbol;

    }

    public char getColor() {

        return color;

    }

    public char getSymbol() {

        return symbol;

    }

    public String getName() {

        return name().toLowerCase();

    }

    public String toString() {

        return "§" + color + symbol;

    }

    public static AnimalGender getByInt(int integer) {

        if (integer > 1)
            return null;

        return values()[integer];

    }

    public static AnimalGender getByBoolean(boolean bool) {

        return bool ? MALE : FEMALE;

    }

    public static AnimalGender getRandomGender() {

        return getByBoolean(ThreadLocalRandom.current().nextBoolean());

    }
}