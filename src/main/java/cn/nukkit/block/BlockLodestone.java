package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.utils.BlockColor;

/**
 * @author good777LUCKY
 */
public class BlockLodestone extends BlockSolid {

    public BlockLodestone() {
        // Does Nothing
    }
    
    @Override
    public String getName() {
        return "Lodestone";
    }
    
    @Override
    public int getId() {
        return LODESTONE;
    }
    
    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }
    
    @Override
    public double getHardness() {
        return 3.5;
    }
    
    @Override
    public double getResistance() {
        return 3.5;
    }
    
    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe()) {
            return new Item[]{
                toItem()
            };
        } else {
            return new Item[0];
        }
    }
    
    @Override
    public boolean canBePushed() {
        return false;
    }
    
    @Override
    public boolean canBePulled() {
        return false;
    }
    
    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
    
    @Override
    public BlockColor getColor() {
        return BlockColor.METAL_BLOCK_COLOR;
    }
}
