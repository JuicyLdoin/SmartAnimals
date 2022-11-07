package ua.ldoin.smartanimals.listener.listeners.animal.gender;

import org.bukkit.Material;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ItemSpawnEvent;
import ua.ldoin.smartanimals.animal.AnimalEntity;
import ua.ldoin.smartanimals.animal.AnimalGender;
import ua.ldoin.smartanimals.utils.load.types.ILoadableListener;

public class HenLaidEggListener implements ILoadableListener {

    @EventHandler
    public void onLaidEgg(ItemSpawnEvent event) {

        if (event.getEntity().getItemStack().getType().equals(Material.EGG))
            for (Entity entity : event.getEntity().getNearbyEntities(0.01, 0.3, 0.01))
                if (entity instanceof Chicken) {

                    AnimalEntity animalEntity = AnimalEntity.getAnimalEntity(entity);

                    if (animalEntity != null)
                        if (animalEntity.getGender().equals(AnimalGender.MALE))
                            event.setCancelled(true);

                }
    }
}