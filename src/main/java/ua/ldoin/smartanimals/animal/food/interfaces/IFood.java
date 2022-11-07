package ua.ldoin.smartanimals.animal.food.interfaces;

import ua.ldoin.smartanimals.animal.food.FoodType;

public interface IFood {

    FoodType getType();

    float getHunger();

    float getWeight();

}