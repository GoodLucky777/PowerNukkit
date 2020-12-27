package cn.nukkit.item.enchantment.trident;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.entity.Entity;
import cn.nukkit.item.enchantment.Enchantment;

public class EnchantmentTridentImpaling extends EnchantmentTrident {

    public EnchantmentTridentImpaling() {
        super(Enchantment.ID_TRIDENT_IMPALING, "impaling", 2);
    }

    @Override
    public int getMinEnchantAbility(int level) {
        return 1 + (level - 1) * 10;
    }

    @Override
    public int getMaxEnchantAbility(int level) {
        return this.getMinEnchantAbility(level) + 15;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @Override
    public double getDamageBonus(Entity entity) {
        if (entity.isTouchingWater() || (entity.getLevel().isRaining() && entity.getLevel().canBlockSeeSky(entity))) {
            return getLevel() * 2.5;
        }
        return 0;
    }
}
