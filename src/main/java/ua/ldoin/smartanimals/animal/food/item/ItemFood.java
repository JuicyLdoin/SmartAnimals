package ua.ldoin.smartanimals.animal.food.item;

import ua.ldoin.smartanimals.animal.food.Food;
import ua.ldoin.smartanimals.animal.food.FoodType;
import ua.ldoin.smartanimals.utils.MaterialWithData;

public class ItemFood extends Food {

    private final int remove;
    private final MaterialWithData material;

    public ItemFood(FoodType type, float hunger, float water, float weight, int remove, MaterialWithData material) {

        super(type, hunger, water, weight);

        this.remove = remove;
        this.material = material;

    }

    public int getRemove() {

        return remove;

    }

    public MaterialWithData getMaterial() {

        return material;

    }
}