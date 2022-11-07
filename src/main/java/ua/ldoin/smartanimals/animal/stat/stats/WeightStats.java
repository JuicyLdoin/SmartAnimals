package ua.ldoin.smartanimals.animal.stat.stats;

import ua.ldoin.smartanimals.animal.AnimalEntity;
import ua.ldoin.smartanimals.animal.stat.AnimalTaskStats;
import ua.ldoin.smartanimals.animal.task.tasks.WeightTask;

public class WeightStats extends AnimalTaskStats {

    private final float maxWeight;
    private float weight;

    private float nextPoop;
    private float nextPee;

    public WeightStats(AnimalEntity animalEntity, float maxWeight, float weight) {

        super(animalEntity, new WeightTask(animalEntity));

        this.maxWeight = maxWeight;
        this.weight = weight;

        nextPoop = 0;
        nextPee = 0;

    }

    public float getMaxWeight() {

        return maxWeight;

    }

    public float getWeight() {

        return weight;

    }

    public float getNextPoop() {

        return nextPoop;

    }

    public float getNextPee() {

        return nextPee;

    }

    public void setWeight(float weight) {

        if (weight <= 0)
            return;

        this.weight = weight;

    }

    public void addWeight(float weight, boolean ignoreMax) {

        if (!ignoreMax && this.weight + weight >= maxWeight) {

            this.weight = maxWeight;
            return;

        }

        this.weight += weight;

    }

    public void addWeight(float weight) {

        addWeight(weight, false);

    }

    public void removeWeight(float weight) {

        if (this.weight - weight <= 0) {

            this.weight = 0;
            return;

        }

        this.weight -= weight;

    }

    public void resetNextPoop() {

        nextPoop = 0;

    }

    public void addNextPoop(float poop) {

        nextPoop += poop;

    }

    public void resetNextPee() {

        nextPee = 0;

    }

    public void addNextPee(float pee) {

        nextPee += pee;

    }
}