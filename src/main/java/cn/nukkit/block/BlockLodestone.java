package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityLodestone;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author good777LUCKY
 */
public class BlockLodestone extends BlockSolid implements BlockEntityHolder<BlockEntityLodestone> {

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
    
    @Nonnull
    @Override
    public String getBlockEntityType() {
        return BlockEntity.LODESTONE;
    }
    
    @Nonnull
    @Override
    public Class<? extends BlockEntityLodestone> getBlockEntityClass() {
        return BlockEntityLodestone.class;
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
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }
    
    @Override
    public boolean canHarvestWithHand() {
        return false;
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
    public boolean place(@Nonnull Item item, @Nonnull Block block, @Nonnull Block target, @Nonnull BlockFace face, double fx, double fy, double fz, @Nullable Player player) {
        return BlockEntityHolder.setBlockAndCreateEntity(this) != null;
    }
    
    @Override
    public BlockColor getColor() {
        return BlockColor.IRON_BLOCK_COLOR;
    }
}
