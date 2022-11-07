package ua.ldoin.smartanimals.listener.listeners.animal;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import ua.ldoin.smartanimals.utils.load.types.ILoadableListener;

public class SheepEatGrassListener implements ILoadableListener {

    @EventHandler
    public void onSheepEatGrass(EntityChangeBlockEvent event) {

        if (event.getTo().equals(Material.DIRT) && event.getEntity().getType().equals(EntityType.SHEEP))
            event.setCancelled(true);

    }
}