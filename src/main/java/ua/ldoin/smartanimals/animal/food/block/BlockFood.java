package ua.ldoin.smartanimals.animal.food.block;

import ua.ldoin.smartanimals.animal.food.Food;
import ua.ldoin.smartanimals.animal.food.FoodType;
import ua.ldoin.smartanimals.utils.MaterialWithData;

public class BlockFood extends Food {

    private final MaterialWithData block;

    public BlockFood(FoodType type, float hunger, float water, float weight, MaterialWithData block) {

        super(type, hunger, water, weight);

        this.block = block;

    }

    public MaterialWithData getBlock() {

        return block;

    }
}