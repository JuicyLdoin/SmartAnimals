package ua.ldoin.smartanimals.animal.stat;

import ua.ldoin.smartanimals.animal.AnimalEntity;
import ua.ldoin.smartanimals.animal.task.IAnimalTask;

public abstract class AnimalTaskStats extends AnimalStats {

    private IAnimalTask task;

    protected AnimalTaskStats(AnimalEntity animalEntity, IAnimalTask task) {

        super(animalEntity);

        this.task = task;

    }

    public IAnimalTask getTask() {

        return task;

    }

    public void setTask(IAnimalTask task) {

        task.setAnimalEntity(getAnimalEntity());
        this.task = task;

    }
}