package ua.ldoin.smartanimals.animal.task.tasks;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import ua.ldoin.smartanimals.SmartAnimalsPlugin;
import ua.ldoin.smartanimals.animal.AnimalEntity;
import ua.ldoin.smartanimals.animal.AnimalState;
import ua.ldoin.smartanimals.animal.food.Food;
import ua.ldoin.smartanimals.animal.food.FoodType;
import ua.ldoin.smartanimals.animal.food.block.BlockFood;
import ua.ldoin.smartanimals.animal.food.block.ReplaceableBlockFood;
import ua.ldoin.smartanimals.animal.food.item.ItemFood;
import ua.ldoin.smartanimals.animal.task.IAnimalTask;
import ua.ldoin.smartanimals.utils.util.EntityUtil;
import ua.ldoin.smartanimals.utils.util.FileUtil;
import ua.ldoin.smartanimals.utils.util.number.NumberUtil;

import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HungerTask implements IAnimalTask {

    static final ConfigurationSection section = FileUtil.CONFIG.getConfigurationSection("options.hunger");

    AnimalEntity animalEntity;
    final BukkitRunnable bukkitRunnable;

    Location targetFoodLocation;
    Food targetFood;

    public HungerTask(AnimalEntity animal) {

        animalEntity = animal;

        new BukkitRunnable() {

            public void run() {

                nextEat = generateEat();

            }
        }.runTaskLaterAsynchronously(SmartAnimalsPlugin.plugin, 1);

        bukkitRunnable = new BukkitRunnable() {

            public void run() {

                animalEntity.getHungerStats().removeHunger((float) section.getDouble("remove_per_update"));

                if (animalEntity.getAnimalState().equals(AnimalState.GOING_TO_EAT) &&
                        (targetFoodLocation == null || animalEntity.getNmsEntity().notMove()))
                    animalEntity.setAnimalState(AnimalState.LOOKING_FOR_EAT);

                if (animalEntity.getHungerStats().getHunger() <= nextEat)
                    findFood();

                eat();

            }
        };

        if (section.getBoolean("enabled"))
            bukkitRunnable.runTaskTimer(SmartAnimalsPlugin.plugin, 0, section.getInt("rate"));

    }

    public BukkitRunnable getTask() {

        return bukkitRunnable;

    }

    public void stopEat() {

        if (animalEntity.getAnimalState().isEatState())
            animalEntity.setAnimalState(AnimalState.WANDERING);

    }

    public void cancel() {

        bukkitRunnable.cancel();
        stopEat();

    }

    public Location findNearbyFoodLocation(int radius) {

        Location currentLocation = animalEntity.getParent().getLocation();

        List<Material> blocks = new ArrayList<>();

        for (Food food : animalEntity.getHungerStats().getCanEat())
            if (food != null)
                if (food.getType().equals(FoodType.BLOCK) || food.getType().equals(FoodType.REPLACEABLE_BLOCK))
                    blocks.add(((BlockFood) food).getBlock().getMaterial());

        Location blockLocation = EntityUtil.findNearbyBlockLocation(currentLocation, radius, blocks);
        Location itemLocation = null;

        ItemFood itemFood = null;

        for (Entity entity : animalEntity.getParent().getNearbyEntities(radius, radius, radius))
            if (entity instanceof Item) {

                Item item = (Item) entity;

                for (Food food : animalEntity.getHungerStats().getCanEat())
                    if (food != null)
                        if (food.getType().equals(FoodType.ITEM)) {

                            itemFood = (ItemFood) food;

                            if (item.getItemStack().getType().equals(itemFood.getMaterial().getMaterial())) {

                                try {

                                    if (item.getItemStack().getData().getData() != itemFood.getMaterial().getData())
                                        continue;

                                } catch (Exception ignored) {
                                }

                                itemLocation = entity.getLocation();
                                break;

                            }
                        }
            }

        if (blockLocation != null)
            blockLocation = blockLocation.clone().add(0.5, 0, 0.5);

        if (itemLocation == null)
            return blockLocation;

        if (blockLocation == null) {

            targetFood = itemFood;
            return itemLocation;

        }

        if (currentLocation.distance(blockLocation) >= currentLocation.distance(itemLocation)) {

            targetFood = itemFood;
            return itemLocation;

        }

        return blockLocation;

    }

    public void findFood() {

        if (!animalEntity.getAnimalState().equals(AnimalState.LOOKING_FOR_EAT) && !animalEntity.getAnimalState().equals(AnimalState.GOING_TO_EAT))
            animalEntity.setAnimalState(AnimalState.LOOKING_FOR_EAT);

        if (animalEntity.getAnimalState().equals(AnimalState.LOOKING_FOR_EAT) &&
                (animalEntity.getNmsEntity().notMove() || targetFoodLocation == null || targetFood == null)) {

            targetFoodLocation = findNearbyFoodLocation(FileUtil.CONFIG.getInt("options.food_find_radius"));

            if (targetFoodLocation != null) {

                if (targetFoodLocation.getBlock() != null)
                    for (Food food : animalEntity.getHungerStats().getCanEat())
                        if (food.getType().equals(FoodType.BLOCK) || food.getType().equals(FoodType.REPLACEABLE_BLOCK))
                            if (targetFoodLocation.getBlock().getType().equals(((BlockFood) food).getBlock().getMaterial())) {

                                targetFood = food;
                                break;

                            }

                animalEntity.getNmsEntity().moveTo(targetFoodLocation);
                animalEntity.setAnimalState(AnimalState.GOING_TO_EAT);

            }
        }
    }

    public void eat() {

        if ((animalEntity.getAnimalState().equals(AnimalState.LOOKING_FOR_EAT) || animalEntity.getAnimalState().equals(AnimalState.GOING_TO_EAT)) &&
                targetFoodLocation != null && targetFood != null && targetFoodLocation.distance(animalEntity.getParent().getLocation()) <= 2) {

            animalEntity.setAnimalState(AnimalState.EATING);

            animalEntity.getNmsEntity().resetMoving();

            float hunger = targetFood.getHunger();
            float water = targetFood.getWater();

            float weight = targetFood.getWeight();

            if (targetFood.getType().equals(FoodType.BLOCK) || targetFood.getType().equals(FoodType.REPLACEABLE_BLOCK)) {

                if (targetFoodLocation.getBlock() != null)
                    if (targetFood.getType().equals(FoodType.BLOCK))
                        targetFoodLocation.getBlock().setType(Material.AIR);
                    else if (targetFood.getType().equals(FoodType.REPLACEABLE_BLOCK))
                        ((ReplaceableBlockFood) targetFood).getNewBlock().applyToBlock(targetFoodLocation.getBlock());

            } else if (targetFood.getType().equals(FoodType.ITEM)) {

                ItemFood itemFood = (ItemFood) targetFood;
                ItemStack itemStack = null;

                for (Entity entity : targetFoodLocation.getWorld().getNearbyEntities(targetFoodLocation, 1, 1, 1))
                    if (entity instanceof Item) {

                        Item item = (Item) entity;

                        if (item.getItemStack().getType().equals(itemFood.getMaterial().getMaterial())) {

                            try {

                                if (item.getItemStack().getData().getData() != itemFood.getMaterial().getData())
                                    continue;

                            } catch (Exception ignored) {
                            }

                            itemStack = item.getItemStack();

                            hunger = itemFood.getHunger() * Math.min(itemStack.getAmount(), itemFood.getRemove());
                            water = itemFood.getWater() * Math.min(itemStack.getAmount(), itemFood.getRemove());

                            break;

                        }
                    }

                if (itemStack == null)
                    return;

                if (itemStack.getAmount() <= itemFood.getRemove())
                    itemStack.setAmount(0);
                else
                    itemStack.setAmount(itemStack.getAmount() - itemFood.getRemove());

            }

            float finalWater = water;
            float finalHunger = hunger;

            new BukkitRunnable() {

                public void run() {

                    if (animalEntity.getHungerStats().getHunger() >= animalEntity.getHungerStats().getMaxHunger() - 1 ||
                            findNearbyFoodLocation(FileUtil.CONFIG.getInt("options.food_find_radius")) == null) {

                        generateNextEat();

                        animalEntity.setAnimalState(AnimalState.WANDERING);

                        targetFoodLocation = null;

                        cancel();
                        return;

                    }

                    animalEntity.setAnimalState(AnimalState.LOOKING_FOR_EAT);
                    findFood();

                    animalEntity.getHungerStats().addHunger(finalHunger);
                    animalEntity.getWaterStats().addWater(finalWater);

                    animalEntity.getWeightStats().addWeight(weight);

                    animalEntity.getWeightStats().addNextPoop(finalHunger / 2);
                    animalEntity.getWeightStats().addNextPee(finalWater / 2);

                    Location location = animalEntity.getParent().getLocation();
                    location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EAT, 1, 1);

                }
            }.runTaskTimer(SmartAnimalsPlugin.plugin, 0, section.getInt("sip_rate"));
        }
    }

    private int nextEat;

    public int generateEat() {

        return (int) NumberUtil.getPercentageOfValue(NumberUtil.randomInRange(10, 20), animalEntity.getHungerStats().getMaxHunger());

    }

    public void generateNextEat() {

        nextEat = generateEat();

    }
}