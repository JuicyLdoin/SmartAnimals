package ua.ldoin.smartanimals.animal.stat.stats;

import ua.ldoin.smartanimals.animal.AnimalEntity;
import ua.ldoin.smartanimals.animal.stat.AnimalTaskStats;
import ua.ldoin.smartanimals.animal.task.tasks.AgeTask;
import ua.ldoin.smartanimals.utils.util.FileUtil;

public class AgeStats extends AnimalTaskStats {

    private long ticks;
    private final int dieOnDay;

    public AgeStats(AnimalEntity animalEntity, long ticks, int dieOnDay) {

        super(animalEntity, new AgeTask(animalEntity));

        this.ticks = ticks;
        this.dieOnDay = dieOnDay;

    }

    public long getTicks() {

        return ticks;

    }

    public int getDieOnDay() {

        return dieOnDay;

    }

    public int getDays() {

        return (int) (FileUtil.CONFIG.getBoolean("options.age.real_age") ? getTicks() / 20 / 60 / 60 / 24 : getTicks() / 24000);

    }

    public int getWeeks() {

        return getDays() / 7;

    }

    public int getMonths() {

        return getDays() / 30;

    }

    public int getYears() {

        return getDays() / 365;

    }

    public boolean isBaby() {

        return getDays() < FileUtil.CONFIG.getInt("options." + getAnimalEntity().getParent().getType().name() + ".age.adult_in");

    }

    public void setTicks(long ticks) {

        this.ticks = ticks;

    }

    public void addTick() {

        ticks++;

    }

    public void addTicks(long ticks) {

        this.ticks += ticks;

    }
}