package ua.ldoin.smartanimals.utils.util.version.nms;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import ua.ldoin.smartanimals.utils.util.ReflectionUtil;
import ua.ldoin.smartanimals.utils.util.version.VersionManager;

public interface NMSEntity {

    static NMSEntity getNMSEntity(LivingEntity entity) throws Exception {

        return (NMSEntity) ReflectionUtil.getClass("ua.ldoin.smartanimals.utils.util.version.nms.NMSEntity" + VersionManager.getVersion().replace("v", ""))
                .getDeclaredConstructor(entity.getClass().getSuperclass()).newInstance(entity);

    }

    void moveTo(Location location);

    void resetMoving();

    boolean notMove();

    void applyFollowRangeAttribute(float range);

}