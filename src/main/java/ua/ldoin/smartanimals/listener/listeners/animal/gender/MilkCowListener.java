package ua.ldoin.smartanimals.listener.listeners.animal.gender;

import org.bukkit.Material;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import ua.ldoin.smartanimals.animal.AnimalEntity;
import ua.ldoin.smartanimals.animal.AnimalGender;
import ua.ldoin.smartanimals.utils.load.types.ILoadableListener;

public class MilkCowListener implements ILoadableListener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEntityEvent event) {

        if (event.getHand().equals(EquipmentSlot.HAND)) {

            ItemStack itemStack = event.getPlayer().getInventory().getItemInMainHand();

            if (itemStack != null)
                if (itemStack.getType().equals(Material.BUCKET)) {

                    Entity entity = event.getRightClicked();

                    if (entity instanceof Cow) {

                        AnimalEntity animalEntity = AnimalEntity.getAnimalEntity(entity);

                        if (animalEntity != null)
                            if (animalEntity.getGender().equals(AnimalGender.MALE))
                                event.setCancelled(true);

                    }
                }
        }
    }
}