package ua.ldoin.smartanimals.animal.task.tasks;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;
import ua.ldoin.smartanimals.SmartAnimalsPlugin;
import ua.ldoin.smartanimals.animal.AnimalEntity;
import ua.ldoin.smartanimals.animal.task.IAnimalTask;
import ua.ldoin.smartanimals.utils.util.FileUtil;
import ua.ldoin.smartanimals.utils.util.number.NumberUtil;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HealthTask implements IAnimalTask {

    AnimalEntity animalEntity;
    final BukkitRunnable bukkitRunnable;

    public HealthTask(AnimalEntity animalEntity) {

        this.animalEntity = animalEntity;

        bukkitRunnable = new BukkitRunnable() {

            public void run() {

                ConfigurationSection section = FileUtil.CONFIG.getConfigurationSection("options.health");
                ConfigurationSection regenerationSection = section.getConfigurationSection("regeneration");

                float health = (float) animalEntity.getParent().getHealth();
                float maxHealth = (float) animalEntity.getParent().getMaxHealth();

                double water = animalEntity.getWaterStats().getWater();
                double hunger = animalEntity.getHungerStats().getHunger();

                if (water <= 0)
                    animalEntity.getParent().damage(section.getDouble("water_damage"));

                if (hunger <= 0)
                    animalEntity.getParent().damage(section.getDouble("hunger_damage"));

                if (health < maxHealth) {

                    float needWater = NumberUtil.getPercentageOfValue(regenerationSection.getString("need.water"), (float) animalEntity.getWaterStats().getMaxWater());
                    float needHunger = NumberUtil.getPercentageOfValue(regenerationSection.getString("need.hunger"), (float) animalEntity.getHungerStats().getMaxHunger());

                    if (water >= needWater && hunger >= needHunger) {

                        if (health + regenerationSection.getDouble("amount") >= maxHealth)
                            animalEntity.getParent().setHealth(maxHealth);
                        else
                            animalEntity.getParent().setHealth((float) (health + regenerationSection.getDouble("amount")));

                    }
                }
            }
        };

        if (FileUtil.CONFIG.getBoolean("options.health.enabled"))
            bukkitRunnable.runTaskTimer(SmartAnimalsPlugin.plugin, 0, FileUtil.CONFIG.getInt("options.health.rate"));

    }

    public BukkitRunnable getTask() {

        return bukkitRunnable;

    }

    public void cancel() {

        bukkitRunnable.cancel();

    }
}