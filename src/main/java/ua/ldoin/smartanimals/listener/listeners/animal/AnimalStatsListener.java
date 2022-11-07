package ua.ldoin.smartanimals.listener.listeners.animal;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import ua.ldoin.smartanimals.SmartAnimalsPlugin;
import ua.ldoin.smartanimals.animal.AnimalEntity;
import ua.ldoin.smartanimals.utils.load.types.ILoadableListener;
import ua.ldoin.smartanimals.utils.util.items.ItemManager;
import ua.ldoin.smartanimals.utils.util.items.ItemUtil;

import java.util.HashMap;
import java.util.Map;

public class AnimalStatsListener implements ILoadableListener {

    private final Map<Player, Inventory> inventoryMap = new HashMap<>();

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {

        Player player = event.getPlayer();

        if (event.getHand().equals(EquipmentSlot.HAND)) {

            ItemStack itemStack = event.getPlayer().getInventory().getItemInMainHand();

            if (itemStack != null)
                if (itemStack.equals(ItemManager.itemManager.getItem("animal_book"))) {

                    LivingEntity entity = (LivingEntity) event.getRightClicked();
                    AnimalEntity animalEntity = AnimalEntity.getAnimalEntity(entity);

                    if (animalEntity != null) {

                        event.setCancelled(true);

                        Inventory inventory = Bukkit.createInventory(null, InventoryType.HOPPER, entity.getCustomName() == null ? entity.getType().name() : entity.getCustomName());

                        new BukkitRunnable() {

                            public void run() {

                                if (!inventoryMap.containsKey(player)) {

                                    cancel();
                                    return;

                                }

                                inventory.setItem(2, ItemUtil.getItem(Material.PAPER, 1, "§eInfo",
                                        "",
                                        "§fName: " + (entity.getCustomName() == null ? "§cNo" : "§e" + entity.getCustomName()),
                                        "§fHP: §c" + String.format("%.1f", entity.getHealth()),
                                        "",
                                        "§fGender: " + animalEntity.getGender().toString(),
                                        "",
                                        "§fAge: §e" + animalEntity.getAgeStats().getDays() + " d.",
                                        "",
                                        "§fThirst: §9" + String.format("%.1f", animalEntity.getWaterStats().getWater()),
                                        "§fHunger: §a" + String.format("%.1f", animalEntity.getHungerStats().getHunger()),
                                        "",
                                        "§fWeight: §e" + String.format("%.1f", animalEntity.getWeightStats().getWeight()) + " kg",
                                        "",
                                        "§fState: §7" + animalEntity.getAnimalState(),
                                        ""));

                            }
                        }.runTaskTimer(SmartAnimalsPlugin.plugin, 0, 20);

                        player.openInventory(inventory);

                        inventoryMap.put(player, inventory);

                    }
                }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {

        inventoryMap.remove((Player) event.getPlayer());

    }
}