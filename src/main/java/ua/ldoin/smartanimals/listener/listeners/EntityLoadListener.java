package ua.ldoin.smartanimals.listener.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.ChunkLoadEvent;
import ua.ldoin.smartanimals.animal.AnimalEntity;
import ua.ldoin.smartanimals.utils.load.types.ILoadableListener;
import ua.ldoin.smartanimals.utils.util.EntityUtil;

public class EntityLoadListener implements ILoadableListener {

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {

        for (Entity entity : event.getChunk().getEntities())
            if (entity instanceof LivingEntity)
                if (EntityUtil.isAnimal(entity))
                    new AnimalEntity((LivingEntity) entity, false);

    }
}