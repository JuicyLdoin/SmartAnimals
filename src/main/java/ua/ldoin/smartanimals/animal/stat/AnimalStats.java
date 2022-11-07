package ua.ldoin.smartanimals.animal.stat;

import ua.ldoin.smartanimals.animal.AnimalEntity;

public abstract class AnimalStats {

    private AnimalEntity animalEntity;

    protected AnimalStats(AnimalEntity animalEntity) {

        this.animalEntity = animalEntity;

    }

    public AnimalEntity getAnimalEntity() {

        return animalEntity;

    }

    public void setAnimalEntity(AnimalEntity animalEntity) {

        this.animalEntity = animalEntity;

    }
}