package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemDiamond;
import cn.nukkit.item.ItemIngotIron;
import cn.nukkit.item.ItemTool;

/**
 * Created by good777LUCKY
 */
public class BlockNetherReactor extends BlockSolid {

    @Override
    public int getId() {
        return NETHER_REACTOR;
    }
    
    @Override
    public String getName() {
        return "Nether Reactor Core";
    }
    
    @Override
    public double getHardness() {
        return 30;
    }
    
    @Override
    public double getResistance() {
        return 30;
    }
    
    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
    
    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }
    
    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe()) {
            return new Item[]{
                new ItemDiamond(0, 3),
                new ItemIngotIron(0, 6)
            };
        } else {
            return new Item[0];
        }
    }
    
    //TODO: Add Block Color
}
