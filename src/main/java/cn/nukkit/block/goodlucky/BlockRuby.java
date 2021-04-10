package cn.nukkit.block;

import cn.nukkit.item.ItemTool;
import cn.nukkit.utils.BlockColor;

/**
 * @author GoodLucky777
 */
public class BlockRuby extends BlockSolid {

    public BlockRuby() {
        // Does Nothing
    }
    
    @Override
    public double getHardness() {
        return 5;
    }
    
    @Override
    public double getResistance() {
        return 30;
    }
    
    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }
    
    @Override
    public int getId() {
        return RUBY_BLOCK;
    }
    
    @Override
    public String getName() {
        return "Ruby Block";
    }
    
    @Override
    public int getToolTier() {
        return ItemTool.TIER_IRON;
    }
    
    @Override
    public BlockColor getColor() {
        return BlockColor.RED_BLOCK_COLOR;
    }
    
    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
    
    @Override
    public int getRuntimeId() {
        return 5410;
    }
}
