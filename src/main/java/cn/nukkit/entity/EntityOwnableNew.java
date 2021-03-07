package cn.nukkit.entity;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.entity.data.LongEntityData;

/**
 * @author GoodLucky777
 */
@PowerNukkitOnly
@Since("1.4.0.0-PN")
public interface EntityOwnableNew {

    long getOwnerId();

    default void setOwnerId(long ownerId) {
        this.setDataProperty(new LongEntityData(Entity.DATA_OWNER_EID, ownerId));
    }
}
