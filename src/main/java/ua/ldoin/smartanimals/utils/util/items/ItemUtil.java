package ua.ldoin.smartanimals.utils.util.items;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import ua.ldoin.smartanimals.SmartAnimalsPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@UtilityClass
public class ItemUtil {

    public static String color(String string) {

        return ChatColor.translateAlternateColorCodes('&', string);

    }

    public static ItemStack getItem(String name) {

        try {

            if (ItemManager.itemManager.getItem(name) != null)
                return ItemManager.itemManager.getItem(name);

            return new ItemStack(Material.getMaterial(name));

        } catch (Exception ex) {

            ex.printStackTrace();
            return new ItemStack(Material.AIR);

        }
    }

    public static ItemStack getItem(Material material, Integer amount, String name, List<String> lore) {

        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(name);

        if (lore != null && !lore.isEmpty())
            itemMeta.setLore(lore);

        itemStack.setItemMeta(itemMeta);

        return itemStack;

    }

    public static ItemStack getItem(Material material, Integer amount, String name, String... lore) {

        return getItem(material, amount, name, Arrays.asList(lore));

    }

    public static ItemStack getItem(Material material, Integer amount, String name) {

        return getItem(material, amount, name, new ArrayList<>());

    }

    public static ItemStack getItem(Material material, Integer amount) {

        return getItem(material, amount, null, new ArrayList<>());

    }

    public static ItemStack getItem(Material material, Integer amount, Short data, String name, List<String> lore) {

        ItemStack itemStack = new ItemStack(material, amount, data);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (name != null)
            itemMeta.setDisplayName(name);

        if (lore != null && !lore.isEmpty())
            itemMeta.setLore(lore);

        itemStack.setItemMeta(itemMeta);

        return itemStack;

    }

    public static ItemStack getItem(Material material, Integer amount, Short data, String name, String... lore) {

        return getItem(material, amount, data, name, Arrays.asList(lore));

    }

    public static ItemStack getItem(Material material, Integer amount, Short data, String name) {

        return getItem(material, amount, data, name, new ArrayList<>());

    }

    public static ItemStack getItem(Material material, Integer amount, Short data) {

        return getItem(material, amount, data, null, new ArrayList<>());

    }

    public static ItemStack getItem(ItemStack itemstack, String enchantment) {

        String[] totalEnch = enchantment.split(":");
        Enchantment aEnch = Enchantment.getByName(totalEnch[0]);

        int aEnchLvl = 1;

        try {

            aEnchLvl = Integer.parseInt(totalEnch[1]);

        } catch (NumberFormatException ex) {

            ex.printStackTrace();

        }

        itemstack.addEnchantment(aEnch, aEnchLvl);
        return itemstack;

    }

    public static ItemStack setHead(ItemStack item, String texture) {

        SkullMeta headMeta = (SkullMeta) item.getItemMeta();

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", texture));

        try {

            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);

        } catch (Exception ex) {

            ex.printStackTrace();

        }

        item.setItemMeta(headMeta);
        return item;

    }

    public static ItemStack buildItemStackFromSection(ConfigurationSection section) {

        ItemStack itemStack = new ItemStack(Material.getMaterial(section.getString("material")), 1, Short.parseShort(section.getString("data")));
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (section.contains("name"))
            itemMeta.setDisplayName(color(section.getString("name")));

        if (section.contains("lore")) {

            List<String> lore = new ArrayList<>();

            for (String lorePiece : section.getStringList("lore"))
                lore.add(color(lorePiece));

            itemMeta.setLore(lore);

        }

        itemStack.setItemMeta(itemMeta);

        return itemStack;

    }

    public static ItemStack buildItemStackFromConfig(FileConfiguration config, String path) {

        return buildItemStackFromSection(config.getConfigurationSection(path));

    }

    public static ItemStack buildItemStackFromConfig(String path) {

        return buildItemStackFromConfig(SmartAnimalsPlugin.plugin.getConfig(), path);

    }
}