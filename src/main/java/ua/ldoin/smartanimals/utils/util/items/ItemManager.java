package ua.ldoin.smartanimals.utils.util.items;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import ua.ldoin.smartanimals.utils.load.ILoadable;
import ua.ldoin.smartanimals.utils.util.ColorUtil;
import ua.ldoin.smartanimals.utils.util.FileUtil;
import ua.ldoin.smartanimals.utils.util.version.VersionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemManager implements ILoadable {

    public static final ItemManager itemManager = new ItemManager();

    private final Map<String, ItemStack> items = new HashMap<>();

    public ItemStack getItem(String name) {

        return items.get(name);

    }

    public void load() throws Exception {

        FileConfiguration config = FileUtil.CONFIG;

        for (String item : config.getConfigurationSection("items").getKeys(false))
            try {

                items.put(item, buildItem(config.getConfigurationSection("items." + item)));

            } catch (Exception ex) {

                Bukkit.getConsoleSender().sendMessage("Cannot be load item " + item);
                ex.printStackTrace();

            }

        Bukkit.getConsoleSender().sendMessage("Loaded " + items.size() + " items!");

    }

    public static ItemStack buildItem(ConfigurationSection section) {

        Material material = Material.matchMaterial(section.getString("material"));
        byte data = (byte) (section.contains("data") ? section.getInt("data") : -1);

        int amount = section.contains("amount") ? section.getInt("amount") : 1;

        ItemStack itemStack;

        if (data == -1)
            itemStack = new ItemStack(material, amount);
        else
            itemStack = new ItemStack(material, amount, data);

        if (section.contains("enchantments"))
            for (String ench : section.getStringList("enchantments"))
                itemStack.addUnsafeEnchantment(Enchantment.getByName(ench.split("-")[0]), Integer.parseInt(ench.split("-")[1]));

        ItemMeta itemMeta = itemStack.getItemMeta();

        if (section.contains("name"))
            itemMeta.setDisplayName(ColorUtil.makeColor(section.getString("name")));

        if (section.contains("lore")) {

            List<String> lore = new ArrayList<>();

            for (String lorePiece : section.getStringList("lore"))
                lore.add(ColorUtil.makeColor(lorePiece));

            itemMeta.setLore(lore);

        }

        if (section.contains("flags"))
            for (String flag : section.getStringList("flags"))
                itemMeta.addItemFlags(ItemFlag.valueOf(flag));

        if (section.contains("options")) {

            ConfigurationSection options = section.getConfigurationSection("options");

            if (options.contains("unbreakable"))
                itemMeta.setUnbreakable(options.getBoolean("unbreakable"));

            if (options.contains("color"))
                try {

                    LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta)itemMeta;
                    String color = options.getString("color");

                    leatherArmorMeta.setColor(Color.fromRGB(Integer.parseInt(color.split(" ")[0]),
                            Integer.parseInt(color.split(" ")[1]), Integer.parseInt(color.split(" ")[2])));

                } catch (Exception ignored) {}
        }

        if (VersionManager.isHigherThan(13))
            if (section.contains("customModelData"))
                try {

                    itemMeta.getClass().getMethod("setCustomModelData", Integer.class).invoke(itemMeta, section.getInt("customModelData"));

                } catch (Exception exception) {

                    exception.printStackTrace();

                }

        itemStack.setItemMeta(itemMeta);
        return itemStack;

    }
}