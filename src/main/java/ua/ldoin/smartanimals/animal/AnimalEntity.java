package ua.ldoin.smartanimals.animal;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import ua.ldoin.smartanimals.SmartAnimalsPlugin;
import ua.ldoin.smartanimals.animal.food.*;
import ua.ldoin.smartanimals.animal.stat.AnimalStats;
import ua.ldoin.smartanimals.animal.stat.stats.*;
import ua.ldoin.smartanimals.animal.task.IAnimalTask;
import ua.ldoin.smartanimals.animal.task.tasks.HealthTask;
import ua.ldoin.smartanimals.utils.util.*;
import ua.ldoin.smartanimals.utils.util.number.*;
import ua.ldoin.smartanimals.utils.util.version.nms.NMSEntity;

import java.io.File;
import java.util.*;

public class AnimalEntity {

    public static final Map<Entity, AnimalEntity> entities = new HashMap<>();

    public static AnimalEntity getAnimalEntity(Entity entity) {

        return entities.get(entity);

    }

    public static Collection<AnimalEntity> getAllEntities() {

        return entities.values();

    }

    public static void saveAll() {

        for (AnimalEntity animalEntity : entities.values())
            animalEntity.save();

    }

    private final LivingEntity parent;
    private final NMSEntity nmsEntity;

    private AnimalState animalState;

    private final AnimalGender gender;

    private final List<AnimalDrop> drops;

    private int nextBreed;

    private final Map<String, AnimalStats> stats;
    private final Map<String, IAnimalTask> tasks;

    public AnimalEntity(LivingEntity entity, boolean child) {

        parent = entity;

        NMSEntity nmsEntity;

        try {

            nmsEntity = NMSEntity.getNMSEntity(entity);

        } catch (Exception ex) {

            ex.printStackTrace();
            nmsEntity = null;

        }

        this.nmsEntity = nmsEntity;

        File file = new File(FileUtil.ANIMALS_DATA_PARENT.getAbsolutePath(), "/" + entity.getUniqueId() + ".dat");

        AnimalState animalState = AnimalState.WANDERING;
        AnimalGender gender = AnimalGender.getRandomGender();

        ConfigurationSection animalOptions = FileUtil.CONFIG.getConfigurationSection("options." + entity.getType().name());

        long ticksLived = child ? 0 : (long) (NumberUtil.randomInRange(new NumberRange(animalOptions.getString("spawn.age_range"))) * (FileUtil.CONFIG.getBoolean("options.age.real_age") ? 24 * 60 : 24000));
        int daysLived = (int) (FileUtil.CONFIG.getBoolean("options.age.real_age") ? ticksLived / 20 / 60 / 60 / 24 : ticksLived / 24000);
        float adult_in = animalOptions.getInt("adult_in");

        int dieOnDay = (int) NumberUtil.randomInRange(new NumberRange(FileUtil.CONFIG.getString("options." + entity.getType().name() + ".age.max_age")));

        float maxWater = (float) animalOptions.getDouble("maxWater");
        float water = maxWater;

        List<String> memory = new ArrayList<>();

        float maxHunger = (float) animalOptions.getDouble("maxHunger");
        float hunger = maxHunger;

        float maxWeight = NumberUtil.randomInRange(new NumberRange(animalOptions.getString("maxWeight." + gender.getName())));
        float weight = NumberUtil.getPercentageOfValue(Math.min(80, daysLived / NumberUtil.addPercentage(adult_in, 20) * 100), maxWeight);

        int nextBreed = 0;

        if (file.exists()) {

            FileConfiguration data = YamlConfiguration.loadConfiguration(file);

            if (data.contains("animalState"))
                animalState = AnimalState.valueOf(data.getString("animalState"));

            if (data.contains("gender"))
                gender = AnimalGender.valueOf(data.getString("gender"));

            if (data.contains("ticksLived"))
                ticksLived = data.getLong("ticksLived");

            if (data.contains("dieOnDay"))
                dieOnDay = data.getInt("dieOnDay");

            if (data.contains("maxWater"))
                maxWater = (float) data.getDouble("maxWater");

            if (data.contains("water"))
                water = (float) data.getDouble("water");

            if (data.contains("memory"))
                memory = data.getStringList("memory");

            if (data.contains("maxHunger"))
                maxHunger = (float) data.getDouble("maxHunger");

            if (data.contains("hunger"))
                hunger = (float) data.getDouble("hunger");

            if (data.contains("maxWeight"))
                maxWeight = (float) data.getDouble("maxWeight");

            if (data.contains("weight"))
                weight = (float) data.getDouble("weight");

            if (data.contains("nextBreed"))
                nextBreed = data.getInt("nextBreed");

        }

        this.animalState = animalState;

        this.gender = gender;

        drops = new ArrayList<>();

        if (animalOptions.contains("drop"))
            for (String drop : animalOptions.getConfigurationSection("drop").getKeys(false)) {

                ConfigurationSection dropSection = animalOptions.getConfigurationSection("drop." + drop);
                drops.add(new AnimalDrop(dropSection.getString("item"), (float) dropSection.getDouble("kgRate"), new NumberRange(dropSection.getString("amount"))));

            }

        this.nextBreed = nextBreed;

        List<Food> canEat = new ArrayList<>();

        for (String food : animalOptions.getStringList("canEat"))
            canEat.add(Food.getFoodByName(food));

        stats = new HashMap<>();
        tasks = new HashMap<>();

        initStats("age", new AgeStats(this, ticksLived, dieOnDay));
        initStats("water", new WaterStats(this, maxWater, water, memory));
        initStats("hunger", new HungerStats(this, maxHunger, hunger, canEat));
        initStats("weight", new WeightStats(this, maxWeight, weight));

        initTask("health", new HealthTask(this));

        assert nmsEntity != null;
        nmsEntity.applyFollowRangeAttribute(Math.min(FileUtil.CONFIG.getInt("options.food_find_radius"), FileUtil.CONFIG.getInt("options.water_find_radius")) + 1);

        AnimalEntity animalEntity = this;

        new BukkitRunnable() {

            public void run() {

                if (entity.isDead() || !entities.containsKey(entity)) {

                    file.delete();

                    getAgeStats().getTask().cancel();
                    getWaterStats().getTask().cancel();
                    getHungerStats().getTask().cancel();
                    getWeightStats().getTask().cancel();

                    getHealthTask().cancel();

                    entities.remove(entity);

                    cancel();

                }

                animalEntity.nextBreed--;

            }
        }.runTaskTimer(SmartAnimalsPlugin.plugin, 0, 20);

        if (!file.exists())
            save();

        entities.put(entity, this);

    }

    public NMSEntity getNmsEntity() {

        return nmsEntity;

    }

    public LivingEntity getParent() {

        return parent;

    }

    public AnimalState getAnimalState() {

        return animalState;

    }

    public AnimalGender getGender() {

        return gender;

    }

    public List<AnimalDrop> getDrops() {

        return drops;

    }

    public int getNextBreed() {

        return nextBreed;

    }

    public boolean isCanBreed() {

        return nextBreed <= 0;

    }

    public void resetBreed() {

        nextBreed = (int) NumberUtil.randomInRange(new NumberRange(FileUtil.CONFIG.getConfigurationSection("options." + parent.getType().name()).getString("breedRate")));

    }

    public void initStats(String name, AnimalStats stats) {

        this.stats.put(name, stats);

    }

    public AnimalStats getStats(String stats) {

        return this.stats.get(stats);

    }

    public AgeStats getAgeStats() {

        return (AgeStats) getStats("age");

    }

    public WaterStats getWaterStats() {

        return (WaterStats) getStats("water");

    }

    public HungerStats getHungerStats() {

        return (HungerStats) getStats("hunger");

    }

    public WeightStats getWeightStats() {

        return (WeightStats) getStats("weight");

    }

    public void initTask(String name, IAnimalTask task) {

        tasks.put(name, task);

    }

    public IAnimalTask getTask(String task) {

        return tasks.get(task);

    }

    public HealthTask getHealthTask() {

        return (HealthTask) getTask("health");

    }

    public void setAnimalState(AnimalState animalState) {

        this.animalState = animalState;

    }

    public void save() {

        try {

            File file = new File(FileUtil.ANIMALS_DATA_PARENT.getAbsolutePath(), "/" + parent.getUniqueId() + ".dat");
            FileConfiguration data = YamlConfiguration.loadConfiguration(file);

            data.set("type", parent.getType().name());
            data.set("location", LocationUtil.getLocation(parent.getLocation()));

            data.set("animalState", animalState.name());

            data.set("gender", gender.name());

            data.set("ticksLived", getAgeStats().getTicks());
            data.set("dieOnDay", getAgeStats().getDieOnDay());

            data.set("maxWater", getWaterStats().getMaxWater());
            data.set("water", getWaterStats().getWater());
            data.set("memory", getWaterStats().getMemory());

            data.set("maxHunger", getHungerStats().getMaxHunger());
            data.set("hunger", getHungerStats().getHunger());

            data.set("maxWeight", getWeightStats().getMaxWeight());
            data.set("weight", getWeightStats().getWeight());

            data.set("nextBreed", getNextBreed());

            data.save(file);

        } catch (Exception exception) {

            exception.printStackTrace();

        }
    }

    public void unload() {

        getAgeStats().getTask().cancel();
        getWaterStats().getTask().cancel();
        getHungerStats().getTask().cancel();
        getWeightStats().getTask().cancel();

        getHealthTask().cancel();

        save();

    }
}