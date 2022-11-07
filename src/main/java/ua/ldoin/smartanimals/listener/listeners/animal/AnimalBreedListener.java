package ua.ldoin.smartanimals.listener.listeners.animal;

import org.bukkit.entity.Animals;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityBreedEvent;
import ua.ldoin.smartanimals.animal.AnimalEntity;
import ua.ldoin.smartanimals.utils.load.types.ILoadableListener;

public class AnimalBreedListener implements ILoadableListener {

    @EventHandler
    public void onBreed(EntityBreedEvent event) {

        AnimalEntity mother = AnimalEntity.getAnimalEntity(event.getMother());
        AnimalEntity father = AnimalEntity.getAnimalEntity(event.getFather());

        if (!mother.getGender().equals(father.getGender()) && mother.isCanBreed() && father.isCanBreed()) {

            new AnimalEntity(event.getEntity(), true);

            mother.resetBreed();
            father.resetBreed();

        } else {

            event.setCancelled(true);

            ((Animals) mother.getParent()).setBreed(false);
            ((Animals) father.getParent()).setBreed(false);

        }
    }
}