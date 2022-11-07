package ua.ldoin.smartanimals.animal.task.tasks;

import org.bukkit.entity.Ageable;
import org.bukkit.scheduler.BukkitRunnable;
import ua.ldoin.smartanimals.SmartAnimalsPlugin;
import ua.ldoin.smartanimals.animal.AnimalEntity;
import ua.ldoin.smartanimals.animal.task.IAnimalTask;
import ua.ldoin.smartanimals.utils.util.FileUtil;

public class AgeTask implements IAnimalTask {

    private AnimalEntity animalEntity;
    private final BukkitRunnable bukkitRunnable;

    private int currentDay = 0;
    private int nextDay = 1;

    public AgeTask(AnimalEntity animal) {

        animalEntity = animal;

        Ageable entity = (Ageable) animalEntity.getParent();

        bukkitRunnable = new BukkitRunnable() {

            public void run() {

                if (!entity.getAgeLock())
                    entity.setAgeLock(true);

                if (animalEntity.getAgeStats().isBaby())
                    entity.setBaby();
                else
                    entity.setAdult();

                animalEntity.getAgeStats().addTicks(20);

                currentDay = animalEntity.getAgeStats().getDays();

                if (currentDay == nextDay) {

                    nextDay++;

                    if (currentDay == animalEntity.getAgeStats().getDieOnDay())
                        entity.remove();

                }
            }
        };

        if (FileUtil.CONFIG.getBoolean("options.age.enabled"))
            bukkitRunnable.runTaskTimerAsynchronously(SmartAnimalsPlugin.plugin, 0, 20);

    }

    public AnimalEntity getAnimalEntity() {

        return animalEntity;

    }

    public BukkitRunnable getTask() {

        return bukkitRunnable;

    }

    public void setAnimalEntity(AnimalEntity animalEntity) {

        this.animalEntity = animalEntity;

    }

    public void cancel() {

        bukkitRunnable.cancel();

    }
}