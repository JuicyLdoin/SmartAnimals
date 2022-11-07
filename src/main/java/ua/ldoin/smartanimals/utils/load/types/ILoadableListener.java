package ua.ldoin.smartanimals.utils.load.types;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import ua.ldoin.smartanimals.SmartAnimalsPlugin;
import ua.ldoin.smartanimals.utils.load.ILoadable;

public interface ILoadableListener extends Listener, ILoadable {

    default void load() {

        Bukkit.getPluginManager().registerEvents(this, SmartAnimalsPlugin.plugin);

    }
}