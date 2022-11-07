package ua.ldoin.smartanimals;

import org.bukkit.plugin.java.JavaPlugin;
import ua.ldoin.smartanimals.utils.load.Loader;
import ua.ldoin.smartanimals.utils.util.FileUtil;

public class SmartAnimalsPlugin extends JavaPlugin {

    public static SmartAnimalsPlugin plugin;

    private Loader loader;

    public void onEnable() {

        plugin = this;

        new FileUtil();

        loader = new Loader();
        loader.loadAll();

    }

    public Loader getLoader() {

        return loader;

    }

    public void onDisable() {

        loader.unLoadAll();

    }
}