package ua.ldoin.smartanimals.utils.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ua.ldoin.smartanimals.SmartAnimalsPlugin;

import java.io.File;

public class FileUtil {

    public FileUtil() {

        try {

            if (!DATA_PARENT.exists())
                DATA_PARENT.mkdirs();

            if (!ANIMAL_DATA_PARENT.exists())
                ANIMAL_DATA_PARENT.mkdirs();

            if (!ANIMALS_DATA_PARENT.exists())
                ANIMALS_DATA_PARENT.mkdirs();

            if (!ANIMALS_FAMILY_DATA_PARENT.exists())
                ANIMALS_FAMILY_DATA_PARENT.mkdirs();

            File configFile = new File(PARENT.getAbsolutePath(), "/config.yml");

            if (!configFile.exists())
                SmartAnimalsPlugin.plugin.saveDefaultConfig();

            CONFIG = YamlConfiguration.loadConfiguration(configFile);

        } catch (Exception ex) {

            ex.printStackTrace();

        }
    }

    public static final File PARENT = SmartAnimalsPlugin.plugin.getDataFolder();

    public static final File DATA_PARENT = new File(PARENT.getAbsolutePath(), "/Data");

    public static final File ANIMAL_DATA_PARENT = new File(DATA_PARENT.getAbsolutePath(), "/Animal");

    public static final File ANIMALS_DATA_PARENT = new File(ANIMAL_DATA_PARENT.getAbsolutePath(), "/Animals");
    public static final File ANIMALS_FAMILY_DATA_PARENT = new File(ANIMAL_DATA_PARENT.getAbsolutePath(), "/Family");

    public static FileConfiguration CONFIG;

}