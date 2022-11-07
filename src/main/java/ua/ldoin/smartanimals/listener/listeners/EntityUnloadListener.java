package ua.ldoin.smartanimals.listener.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.ChunkUnloadEvent;
import ua.ldoin.smartanimals.animal.AnimalEntity;
import ua.ldoin.smartanimals.utils.load.types.ILoadableListener;
import ua.ldoin.smartanimals.utils.util.EntityUtil;

public class EntityUnloadListener implements ILoadableListener {

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {

        for (Entity entity : event.getChunk().getEntities())
            if (EntityUtil.isAnimal(entity))
                if (AnimalEntity.entities.containsKey(entity))
                    AnimalEntity.getAnimalEntity(entity).unload();

    }
}