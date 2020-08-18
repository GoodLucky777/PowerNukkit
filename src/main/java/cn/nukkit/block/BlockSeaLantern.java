package cn.nukkit.block;

import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemPrismarineCrystals;
import cn.nukkit.utils.BlockColor;

import java.util.Random;


public class BlockSeaLantern extends BlockTransparent {

    public BlockSeaLantern() {
        // Does Nothing
    }

    @Override
    public String getName() {
        return "Sea Lantern";
    }

    @Override
    public int getId() {
        return SEA_LANTERN;
    }

    @Override
    public double getResistance() {
        return 1.5;
    }

    @Override
    public double getHardness() {
        return 0.3;
    }

    @Override
    public int getLightLevel() {
        return 15;
    }

    @Override
    public Item[] getDrops(Item item) {
        Random random = new Random();
        int count = 2 + random.nextInt(1);
        
        Enchantment fortune = item.getEnchantment(Enchantment.ID_FORTUNE_DIGGING);
        if (fortune != null && fortune.getLevel() >= 1) {
            count += fortune.getLevel();
        }
        
        return new Item[]{ new ItemPrismarineCrystals(0, Math.min(5, count)) };
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.QUARTZ_BLOCK_COLOR;
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }
}
