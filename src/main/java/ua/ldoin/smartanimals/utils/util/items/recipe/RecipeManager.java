package ua.ldoin.smartanimals.utils.util.items.recipe;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import ua.ldoin.smartanimals.SmartAnimalsPlugin;
import ua.ldoin.smartanimals.utils.load.ILoadable;
import ua.ldoin.smartanimals.utils.util.FileUtil;
import ua.ldoin.smartanimals.utils.util.ReflectionUtil;
import ua.ldoin.smartanimals.utils.util.items.ItemUtil;

import java.util.List;

public class RecipeManager implements ILoadable {

    public static final RecipeManager recipeManager = new RecipeManager();

    public void load() throws Exception {

        FileConfiguration config = FileUtil.CONFIG;

        int recipes = 0;

        for (String recipeName : config.getConfigurationSection("recipes").getKeys(false)) {

            ConfigurationSection section = config.getConfigurationSection("recipes." + recipeName);

            ItemStack[] items = new ItemStack[9];

            ItemStack result = ItemUtil.getItem(section.getString("result"));
            List<String> recipe = section.getStringList("recipe");

            if (recipe.size() != 3)
                continue;

            boolean add = true;

            for (int i = 0; i < recipe.size(); i++) {

                if (recipe.get(i).split("").length != 3) {

                    add = false;
                    break;

                }

                for (int j = 0; j < (recipe.get(i).split("")).length; j++)
                    items[i * 3 + j] = ItemUtil.getItem(section.getConfigurationSection("ingredients").getString(recipe.get(i).split("")[j]));

            }

            if (add) {

                addRecipe(items, result, recipeName);
                recipes++;

            }
        }

        Bukkit.getConsoleSender().sendMessage("Loaded " + recipes + " recipes!");

    }

    public boolean addRecipe(ItemStack[] items, ItemStack result, String name) {

        if (items == null)
            return false;

        if (items.length != 9)
            return false;

        NamespacedKey namespacedKey = new NamespacedKey(SmartAnimalsPlugin.plugin, name);
        String[] letters = { "ABC", "DEF", "GHI" };

        ShapedRecipe shapedRecipe = new ShapedRecipe(namespacedKey, result);
        shapedRecipe.shape(letters);

        for (int i = 0; i < letters.length; i++)
            for (int j = 0; j < (letters[i].split("")).length; j++) {

                ItemStack item = items[i * 3 + j];
                char letter = letters[i].charAt(j);

                try {

                    ReflectionUtil.getMethod(ShapedRecipe.class, "setIngredient", Character.class, ReflectionUtil.getBukkitClass("inventory.RecipeChoice"))
                            .invoke(shapedRecipe, letters, new RecipeChoice.ExactChoice(item));

                } catch (Exception ignored) {

                    if (item == null)
                        shapedRecipe.setIngredient(letter, Material.AIR);
                    else
                        shapedRecipe.setIngredient(letter, item.getType());

                }
            }

        return Bukkit.addRecipe(shapedRecipe);

    }
}