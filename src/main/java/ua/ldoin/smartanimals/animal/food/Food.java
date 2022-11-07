package ua.ldoin.smartanimals.animal.food;

import ua.ldoin.smartanimals.animal.food.interfaces.IWaterFood;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class Food implements IWaterFood {

    public static final Map<String, Food> foods = new HashMap<>();

    public static Food getFoodByName(String name) {

        return foods.get(name);

    }

    public static Collection<Food> getAllFood() {

        return foods.values();

    }

    private final FoodType type;

    private final float hunger;
    private final float water;

    private final float weight;

    public Food(FoodType type, float hunger, float water, float weight) {

        this.type = type;

        this.hunger = hunger;
        this.water = water;

        this.weight = weight;

    }

    public FoodType getType() {

        return type;

    }

    public float getHunger() {

        return hunger;

    }

    public float getWater() {

        return water;

    }

    public float getWeight() {

        return weight;

    }
}