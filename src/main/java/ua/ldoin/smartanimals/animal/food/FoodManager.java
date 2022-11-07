package ua.ldoin.smartanimals.animal.food;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import ua.ldoin.smartanimals.animal.food.block.BlockFood;
import ua.ldoin.smartanimals.animal.food.block.ReplaceableBlockFood;
import ua.ldoin.smartanimals.animal.food.item.ItemFood;
import ua.ldoin.smartanimals.utils.MaterialWithData;
import ua.ldoin.smartanimals.utils.load.ILoadable;
import ua.ldoin.smartanimals.utils.util.FileUtil;

public class FoodManager implements ILoadable {

    public void load() throws Exception {

        FileConfiguration config = FileUtil.CONFIG;

        for (String food : config.getConfigurationSection("options.food").getKeys(false)) {

            ConfigurationSection section = config.getConfigurationSection("options.food." + food);

            FoodType type = FoodType.valueOf(section.getString("type").toUpperCase());

            ConfigurationSection regenerateSection = section.getConfigurationSection("regenerate");

            float hunger = (float) regenerateSection.getDouble("hunger");
            float water = (float) regenerateSection.getDouble("water");

            float weight = (float) regenerateSection.getDouble("weight");

            if (type.equals(FoodType.BLOCK))
                Food.foods.put(food, new BlockFood(type, hunger, water, weight, new MaterialWithData(section.getString("block"))));
            else if (type.equals(FoodType.REPLACEABLE_BLOCK)) {

                ConfigurationSection blockSection = section.getConfigurationSection("block");
                Food.foods.put(food, new ReplaceableBlockFood(type, hunger, water, weight, new MaterialWithData(blockSection.getString("before_eat")), new MaterialWithData(blockSection.getString("after_eat"))));

            } else if (type.equals(FoodType.ITEM)) {

                ConfigurationSection itemSection = section.getConfigurationSection("item");
                Food.foods.put(food, new ItemFood(type, hunger, water, weight, itemSection.getInt("remove_amount"), new MaterialWithData(itemSection.getString("material"))));

            }
        }
    }
}