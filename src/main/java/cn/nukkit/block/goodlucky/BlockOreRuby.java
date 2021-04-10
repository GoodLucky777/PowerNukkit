package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemRuby;
import cn.nukkit.item.ItemTool;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.math.NukkitRandom;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author GoodLucky777
 */
public class BlockOreRuby extends BlockSolid {

    public BlockOreRuby() {
        // Does Nothing
    }
    
    @Override
    public int getId() {
        return RUBY_ORE;
    }
    
    @Override
    public double getHardness() {
        return 3;
    }
    
    @Override
    public double getResistance() {
        return 15;
    }
    
    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }
    
    @Override
    public int getToolTier() {
        return ItemTool.TIER_IRON;
    }
    
    @Override
    public String getName() {
        return "Ruby Ore";
    }
    
    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= getToolTier()) {
            int count = 1;
            Enchantment fortune = item.getEnchantment(Enchantment.ID_FORTUNE_DIGGING);
            if (fortune != null && fortune.getLevel() >= 1) {
                int i = ThreadLocalRandom.current().nextInt(fortune.getLevel() + 2) - 1;
                
                if (i < 0) {
                    i = 0;
                }
                
                count = i + 1;
            }
            
            return new Item[]{
                new ItemRuby(0, count)
            };
        } else {
            return Item.EMPTY_ARRAY;
        }
    }
    
    @Override
    public int getDropExp() {
        return new NukkitRandom().nextRange(5, 9);
    }
    
    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
    
    @Override
    public boolean canSilkTouch() {
        return true;
    }
    
    @Override
    public int getRuntimeId() {
        return 5411;
    }
}
