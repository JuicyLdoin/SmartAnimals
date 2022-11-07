package ua.ldoin.smartanimals.animal.task.tasks;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitRunnable;
import ua.ldoin.smartanimals.SmartAnimalsPlugin;
import ua.ldoin.smartanimals.animal.AnimalEntity;
import ua.ldoin.smartanimals.animal.task.IAnimalTask;
import ua.ldoin.smartanimals.utils.util.items.ItemUtil;

import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WeightTask implements IAnimalTask {

    public static final List<Item> items = new ArrayList<>();

    AnimalEntity animalEntity;
    final BukkitRunnable bukkitRunnable;

    public WeightTask(AnimalEntity animal) {

        animalEntity = animal;

        bukkitRunnable = new BukkitRunnable() {

            public void run() {

                if (animalEntity.getWeightStats().getNextPoop() >= animalEntity.getHungerStats().getMaxHunger()) {

                    Item item = animalEntity.getParent().getWorld().dropItemNaturally(animalEntity.getParent().getLocation(), ItemUtil.getItem(Material.INK_SACK, 1, (short) 3));

                    item.setPickupDelay(200);

                    new BukkitRunnable() {

                        public void run() {

                            items.remove(item);
                            item.remove();

                        }
                    }.runTaskLater(SmartAnimalsPlugin.plugin, 100);

                    items.add(item);

                    animalEntity.getWeightStats().removeWeight(animalEntity.getWeightStats().getNextPoop() / 20);
                    animalEntity.getWeightStats().resetNextPoop();

                }

                if (animalEntity.getWeightStats().getNextPee() >= animalEntity.getWaterStats().getMaxWater()) {

                    Item item = animalEntity.getParent().getWorld().dropItemNaturally(animalEntity.getParent().getLocation(), ItemUtil.getItem(Material.INK_SACK, 1, (short) 11));

                    item.setPickupDelay(200);

                    new BukkitRunnable() {

                        public void run() {

                            items.remove(item);
                            item.remove();

                        }
                    }.runTaskLater(SmartAnimalsPlugin.plugin, 100);

                    items.add(item);

                    animalEntity.getWeightStats().removeWeight(animalEntity.getWeightStats().getNextPee() / 50);
                    animalEntity.getWeightStats().resetNextPee();

                }
            }
        };

        bukkitRunnable.runTaskTimer(SmartAnimalsPlugin.plugin, 0, 20);

    }

    public BukkitRunnable getTask() {

        return bukkitRunnable;

    }

    public void cancel() {

        bukkitRunnable.cancel();

    }
}