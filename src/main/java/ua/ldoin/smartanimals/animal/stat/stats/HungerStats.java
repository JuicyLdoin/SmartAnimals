package ua.ldoin.smartanimals.animal.stat.stats;

import ua.ldoin.smartanimals.animal.AnimalEntity;
import ua.ldoin.smartanimals.animal.food.Food;
import ua.ldoin.smartanimals.animal.stat.AnimalTaskStats;
import ua.ldoin.smartanimals.animal.task.tasks.HungerTask;

import java.util.List;

public class HungerStats extends AnimalTaskStats {

    private final float maxHunger;
    private float hunger;

    private final List<Food> canEat;

    public HungerStats(AnimalEntity animalEntity, float maxHunger, float hunger, List<Food> canEat) {

        super(animalEntity, new HungerTask(animalEntity));

        this.maxHunger = maxHunger;
        this.hunger = hunger;

        this.canEat = canEat;

    }

    public float getMaxHunger() {
        
        return maxHunger;

    }

    public float getHunger() {

        return hunger;

    }

    public List<Food> getCanEat() {

        return canEat;

    }

    public boolean isCanEat(Food food) {

        return canEat.contains(food);

    }

    public void setHunger(float hunger) {

        this.hunger = hunger;

    }

    public void removeHunger(float hunger) {

        if (this.hunger - hunger < 0) {

            this.hunger = 0;
            return;

        }

        this.hunger -= hunger;

    }

    public void addHunger(float hunger) {

        if (this.hunger + hunger > maxHunger) {

            this.hunger = maxHunger;
            return;

        }

        this.hunger += hunger;

    }
}