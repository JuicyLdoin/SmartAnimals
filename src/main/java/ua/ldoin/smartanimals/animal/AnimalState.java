package ua.ldoin.smartanimals.animal;

public enum AnimalState {

    WANDERING,
    SLEEPING,
    EATING,
    DRINKS,
    LOOKING_FOR_EAT,
    GOING_TO_EAT,
    LOOKING_FOR_WATER,
    GOING_TO_WATER;

    public boolean isEatState() {

        return equals(EATING) || equals(LOOKING_FOR_EAT) || equals(GOING_TO_EAT);

    }

    public boolean isWaterState() {

        return equals(DRINKS) || equals(LOOKING_FOR_WATER) || equals(GOING_TO_WATER);

    }
}