package ua.ldoin.smartanimals.listener.listeners;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import ua.ldoin.smartanimals.animal.AnimalEntity;
import ua.ldoin.smartanimals.utils.util.EntityUtil;
import ua.ldoin.smartanimals.utils.load.types.ILoadableListener;
import ua.ldoin.smartanimals.utils.util.FileUtil;

public class EntitySpawnListener implements ILoadableListener {

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {

        LivingEntity entity = event.getEntity();

        if (EntityUtil.isAnimal(entity))
            if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.CUSTOM)) {

                ConfigurationSection section = FileUtil.CONFIG.getConfigurationSection("options." + entity.getType().name() + ".spawn");

                if (section.getStringList("biome_blacklist").contains(event.getLocation().getBlock().getBiome().name())) {

                    event.setCancelled(true);
                    return;

                }

                if (EntityUtil.findNearbyWaterLocation(entity.getLocation(), FileUtil.CONFIG.getInt("options.water_find_radius")) == null)
                    event.setCancelled(true);
                else
                    new AnimalEntity(entity, false);

            }
    }
}