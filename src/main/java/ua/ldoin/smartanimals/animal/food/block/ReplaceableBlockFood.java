package ua.ldoin.smartanimals.animal.food.block;

import ua.ldoin.smartanimals.animal.food.FoodType;
import ua.ldoin.smartanimals.utils.MaterialWithData;

public class ReplaceableBlockFood extends BlockFood {

    private final MaterialWithData newBlock;

    public ReplaceableBlockFood(FoodType type, float hunger, float water, float weight, MaterialWithData block, MaterialWithData newBlock) {

        super(type, hunger, water, weight, block);

        this.newBlock = newBlock;

    }

    public MaterialWithData getNewBlock() {

        return newBlock;

    }
}