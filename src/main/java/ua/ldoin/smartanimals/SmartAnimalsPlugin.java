package ua.ldoin.smartanimals;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.plugin.java.JavaPlugin;
import ua.ldoin.smartanimals.utils.load.Loader;
import ua.ldoin.smartanimals.utils.util.FileUtil;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SmartAnimalsPlugin extends JavaPlugin {

    public static SmartAnimalsPlugin plugin;

    Loader loader;

    public void onEnable() {

        plugin = this;

        new FileUtil();

        loader = new Loader();
        loader.loadAll();

    }

    public void onDisable() {

        loader.unLoadAll();

    }
}