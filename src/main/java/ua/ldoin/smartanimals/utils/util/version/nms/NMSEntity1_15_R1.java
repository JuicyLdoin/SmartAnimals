package ua.ldoin.smartanimals.utils.util.version.nms;

import net.minecraft.server.v1_15_R1.EntityAnimal;
import net.minecraft.server.v1_15_R1.GenericAttributes;
import net.minecraft.server.v1_15_R1.Navigation;
import net.minecraft.server.v1_15_R1.PathEntity;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftAnimals;

public class NMSEntity1_15_R1 implements NMSEntity {

    private final EntityAnimal parent;

    public NMSEntity1_15_R1(CraftAnimals parent) {

        this.parent = parent.getHandle();

    }

    public Navigation getNavigation() {

        return (Navigation) parent.getNavigation();

    }

    public void moveTo(Location location) {

        PathEntity path = getNavigation().a(location.getX(), location.getY(), location.getZ(), 0);

        if (path != null)
            getNavigation().a(path, 1.5);

    }

    public void resetMoving() {

        getNavigation().o();

    }

    public boolean notMove() {

        return getNavigation().k() == null;

    }

    public void applyFollowRangeAttribute(float range) {

        parent.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(Math.max(parent.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).getValue(), range));

    }
}