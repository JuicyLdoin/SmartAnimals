package ua.ldoin.smartanimals.animal.task;

import org.bukkit.scheduler.BukkitRunnable;
import ua.ldoin.smartanimals.animal.AnimalEntity;

public interface IAnimalTask {

    AnimalEntity getAnimalEntity();

    BukkitRunnable getTask();

    void setAnimalEntity(AnimalEntity animalEntity);

    void cancel();

}