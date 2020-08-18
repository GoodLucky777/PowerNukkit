package cn.nukkit.block;

import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemSeedsWheat;
import cn.nukkit.item.ItemWheat;

import java.util.Random;

/**
 * @author xtypr
 * @since 2015/12/2
 */
public class BlockWheat extends BlockCrops {

    public BlockWheat() {
        this(0);
    }

    public BlockWheat(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return "Wheat Block";
    }

    @Override
    public int getId() {
        return WHEAT_BLOCK;
    }

    @Override
    public Item toItem() {
        return new ItemSeedsWheat();
    }

    @Override
    public Item[] getDrops(Item item) {
        if (this.getDamage() >= 0x07) {
            Enchantment fortune = item.getEnchantment(Enchantment.ID_FORTUNE_DIGGING);
            if (fortune != null && fortune.getLevel() >= 1) {
                int fortuneLevel = fortune.getLevel();
            } else {
                int fortuneLevel = 0;
            }
            
            Random random = new Random();
            int count = 0;
            for (int i = 0; i < (3 + fortuneLevel); i++) {
                if (random.nextInt(99) < 57) {
                    count++;
                }
            }
            
            return new Item[]{ new ItemWheat(), new ItemSeedsWheat(0, count) };
        } else {
            return new Item[]{ new ItemSeedsWheat() };
        }
    }
}
