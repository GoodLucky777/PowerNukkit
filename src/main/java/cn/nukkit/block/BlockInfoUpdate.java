package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.utils.BlockColor;

/**
 * @author good777LUCKY
 */
public class BlockInfoUpdate extends BlockSolid {

    public BlockInfoUpdate() {
        // Does Nothing
    }
    
    @Override
    public String getName() {
        return "info_update";
    }
    
    @Override
    public int getId() {
        return INFO_UPDATE;
    }
    
    @Override
    public double getHardness() {
        return 0.5;
    }
    
    @Override
    public double getResistance() {
        return 0.5; // TODO: Correct Resistance
    }
    
    @Override
    public int getToolType() {
        return ItemTool.TYPE_SWORD;
    }
    
    @Override
    public Item[] getDrops(Item item) {
       return new Item[]{ toItem() };
    }
    
    @Override
    public BlockColor getColor() {
        return BlockColor.DIRT_BLOCK_COLOR;
    }
}
