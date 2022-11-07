package ua.ldoin.smartanimals.listener.listeners.animal;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import ua.ldoin.smartanimals.SmartAnimalsPlugin;
import ua.ldoin.smartanimals.animal.AnimalDrop;
import ua.ldoin.smartanimals.animal.AnimalEntity;
import ua.ldoin.smartanimals.utils.load.types.ILoadableListener;
import ua.ldoin.smartanimals.utils.util.EntityUtil;

public class AnimalDeathListener implements ILoadableListener {

    @EventHandler
    public void onDeath(EntityDeathEvent event) {

        LivingEntity entity = event.getEntity();

        if (EntityUtil.isAnimal(entity)) {

            AnimalEntity animalEntity = AnimalEntity.getAnimalEntity(entity);

            if (animalEntity != null)
                new BukkitRunnable() {

                    public void run() {

                        for (AnimalDrop drop : animalEntity.getDrops())
                            drop.drop(animalEntity);

                    }
                }.runTaskLater(SmartAnimalsPlugin.plugin, 1);

            event.getDrops().clear();

        }
    }
}