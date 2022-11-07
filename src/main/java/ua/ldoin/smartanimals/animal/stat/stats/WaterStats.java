package ua.ldoin.smartanimals.animal.stat.stats;

import org.bukkit.Location;
import ua.ldoin.smartanimals.animal.AnimalEntity;
import ua.ldoin.smartanimals.animal.stat.AnimalTaskStats;
import ua.ldoin.smartanimals.animal.task.tasks.WaterTask;
import ua.ldoin.smartanimals.utils.util.FileUtil;
import ua.ldoin.smartanimals.utils.util.LocationUtil;

import java.util.ArrayList;
import java.util.List;

public class WaterStats extends AnimalTaskStats {

    public static int getMaxMemorySize() {

        return FileUtil.CONFIG.getInt("options.water.memory.size");

    }

    public static boolean isMemoryModeEnabled() {

        return FileUtil.CONFIG.getBoolean("options.water.memory.enabled");

    }

    private final float maxWater;
    private float water;

    private final List<Location> memory;
    private int nextMemoryReplace = 0;

    public WaterStats(AnimalEntity animalEntity, float maxWater, float water, List<String> memory) {

        super(animalEntity, new WaterTask(animalEntity));

        this.maxWater = maxWater;
        this.water = water;

        this.memory = new ArrayList<>();

        if (memory.size() != 0)
            for (String string : memory)
                this.memory.add(LocationUtil.getLocation(string));

    }

    public float getMaxWater() {

        return maxWater;

    }

    public float getWater() {

        return water;

    }

    public List<Location> getMemory() {

        return memory;

    }

    public void setWater(float water) {

        this.water = water;

    }

    public void removeWater(float water) {

        if (this.water - water < 0) {

            this.water = 0;
            return;

        }

        this.water -= water;

    }

    public void addWater(float water) {

        if (this.water + water > maxWater) {

            this.water = maxWater;
            return;

        }

        this.water += water;

    }

    public void addMemory(String location) {

        addMemory(LocationUtil.getLocation(location));

    }

    public void addMemory(Location location) {

        if (!isMemoryModeEnabled())
            return;

        if (memory.contains(location))
            return;

        if (nextMemoryReplace >= getMaxMemorySize())
            nextMemoryReplace = 0;

        if (memory.size() >= getMaxMemorySize())
            memory.set(nextMemoryReplace, location);
        else
            memory.add(location);

        nextMemoryReplace++;

    }
}