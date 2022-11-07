package ua.ldoin.smartanimals.animal;

import lombok.Value;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Sheep;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import ua.ldoin.smartanimals.utils.util.items.ItemUtil;
import ua.ldoin.smartanimals.utils.util.number.NumberRange;
import ua.ldoin.smartanimals.utils.util.number.NumberUtil;

@Value
public class AnimalDrop {

    ItemStack itemStack;

    float kgRate;
    NumberRange amountRange;

    public AnimalDrop(String item, float kgRate, NumberRange amountRange) {

        itemStack = ItemUtil.getItem(item);

        this.kgRate = kgRate;
        this.amountRange = amountRange;

    }

    public ItemStack calculateDrop(float kg) {

        ItemStack drop = itemStack.clone();

        while (kg > kgRate) {

            drop.setAmount((int) (drop.getAmount() + NumberUtil.randomInRange(amountRange)));
            kg -= kgRate;

        }

        drop.setAmount(drop.getAmount() - 1);

        return drop;

    }

    public void drop(AnimalEntity animalEntity) {

        LivingEntity entity = animalEntity.getParent();

        ItemStack drop = calculateDrop(animalEntity.getWeightStats().getWeight());

        if (entity.getType().equals(EntityType.SHEEP))
            drop.setData(new MaterialData(drop.getType(), ((Sheep) entity).getColor().getWoolData()));

        entity.getWorld().dropItemNaturally(entity.getLocation(), drop);

    }
}