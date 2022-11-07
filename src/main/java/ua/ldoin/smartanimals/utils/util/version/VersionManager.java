package ua.ldoin.smartanimals.utils.util.version;

import org.bukkit.Bukkit;

public class VersionManager {

    public static String getVersion() {

        return Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];

    }

    public static boolean isHigherThan(int version) {

        return version < Integer.parseInt(getVersion().split("_")[1]);

    }
}