package ua.ldoin.smartanimals.animal.task.tasks;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;
import ua.ldoin.smartanimals.SmartAnimalsPlugin;
import ua.ldoin.smartanimals.animal.AnimalEntity;
import ua.ldoin.smartanimals.animal.AnimalState;
import ua.ldoin.smartanimals.animal.task.IAnimalTask;
import ua.ldoin.smartanimals.utils.util.EntityUtil;
import ua.ldoin.smartanimals.utils.util.FileUtil;
import ua.ldoin.smartanimals.utils.util.number.NumberUtil;

import java.util.Arrays;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WaterTask implements IAnimalTask {

    static final ConfigurationSection section = FileUtil.CONFIG.getConfigurationSection("options.water");

    AnimalEntity animalEntity;
    final BukkitRunnable bukkitRunnable;

    Location targetWaterLocation;

    public WaterTask(AnimalEntity animal) {

        animalEntity = animal;

        new BukkitRunnable() {

            public void run() {

                nextDrink = generateDrink();

            }
        }.runTaskLaterAsynchronously(SmartAnimalsPlugin.plugin, 1);

        bukkitRunnable = new BukkitRunnable() {

            public void run() {

                animalEntity.getWaterStats().removeWater((float) section.getDouble("remove_per_update"));

                if (animalEntity.getAnimalState().equals(AnimalState.GOING_TO_WATER) &&
                        (targetWaterLocation == null || animalEntity.getNmsEntity().notMove()))
                    animalEntity.setAnimalState(AnimalState.LOOKING_FOR_WATER);

                if (animalEntity.getWaterStats().getWater() <= nextDrink)
                    findWater();

                drink();

            }
        };

        if (section.getBoolean("enabled"))
            bukkitRunnable.runTaskTimer(SmartAnimalsPlugin.plugin, 0, section.getInt("rate"));

    }

    public BukkitRunnable getTask() {

        return bukkitRunnable;

    }

    public List<Material> getWaterBlocks() {

        return Arrays.asList(Material.WATER, Material.STATIONARY_WATER);

    }

    private void stopDrink() {

        if (animalEntity.getAnimalState().isWaterState())
            animalEntity.setAnimalState(AnimalState.WANDERING);

    }

    public void cancel() {

        bukkitRunnable.cancel();
        stopDrink();

    }

    public void findWater() {

        if (!animalEntity.getAnimalState().equals(AnimalState.LOOKING_FOR_WATER) && !animalEntity.getAnimalState().equals(AnimalState.GOING_TO_WATER))
            animalEntity.setAnimalState(AnimalState.LOOKING_FOR_WATER);

        if (animalEntity.getAnimalState().equals(AnimalState.LOOKING_FOR_WATER) &&
                (animalEntity.getNmsEntity().notMove() || targetWaterLocation == null)) {

            targetWaterLocation = EntityUtil.findNearbyWaterLocation(animalEntity.getParent().getLocation(), FileUtil.CONFIG.getInt("options.water_find_radius"));

            if (targetWaterLocation == null)
                for (Location location : animalEntity.getWaterStats().getMemory())
                    if (location != null)
                        if (location.getBlock() != null)
                            if (getWaterBlocks().contains(location.getBlock().getType())) {

                                targetWaterLocation = location;
                                break;

                            }

            if (targetWaterLocation != null) {

                animalEntity.getNmsEntity().moveTo(targetWaterLocation);
                animalEntity.getWaterStats().addMemory(targetWaterLocation);

            }
        }
    }

    public void drink() {

        if ((animalEntity.getAnimalState().equals(AnimalState.LOOKING_FOR_WATER) || animalEntity.getAnimalState().equals(AnimalState.GOING_TO_WATER)) &&
                targetWaterLocation != null && targetWaterLocation.distance(animalEntity.getParent().getLocation()) <= 2) {

            animalEntity.setAnimalState(AnimalState.DRINKS);

            animalEntity.getNmsEntity().resetMoving();

            new BukkitRunnable() {

                public void run() {

                    if (animalEntity.getWaterStats().getWater() >= animalEntity.getWaterStats().getMaxWater() - 1 ||
                            EntityUtil.findNearbyBlockLocation(animalEntity.getParent().getLocation(), 2, getWaterBlocks()) == null) {

                        generateNextDrink();

                        animalEntity.setAnimalState(AnimalState.WANDERING);

                        targetWaterLocation = null;

                        cancel();
                        return;

                    }

                    float water = NumberUtil.getPercentageOfValue(section.getString("amount_in_sip"), animalEntity.getWaterStats().getMaxWater());

                    animalEntity.getWaterStats().addWater(water);
                    animalEntity.getWeightStats().addWeight(water / 20);
                    animalEntity.getWeightStats().addNextPee(water / 2);

                    Location location = animalEntity.getParent().getLocation();
                    location.getWorld().playSound(location, Sound.ENTITY_GENERIC_DRINK, 1, 1);

                }
            }.runTaskTimer(SmartAnimalsPlugin.plugin, 0, section.getInt("sip_rate"));
        }
    }

    int nextDrink;

    public int generateDrink() {

        return (int) NumberUtil.getPercentageOfValue(NumberUtil.randomInRange(10, 20), animalEntity.getWaterStats().getMaxWater());

    }

    public void generateNextDrink() {

        nextDrink = generateDrink();

    }
}