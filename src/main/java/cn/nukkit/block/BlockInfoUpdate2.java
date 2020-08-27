package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.utils.BlockColor;

/**
 * @author good777LUCKY
 */
public class BlockInfoUpdate2 extends BlockSolid {

    public BlockInfoUpdate2() {
        // Does Nothing
    }
    
    @Override
    public String getName() {
        return "info_update2";
    }
    
    @Override
    public int getId() {
        return INFO_UPDATE2;
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
    public BlockColor getColor() {
        return BlockColor.DIRT_BLOCK_COLOR;
    }
}
