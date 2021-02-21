package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityCommandBlock;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.BooleanBlockProperty;
import cn.nukkit.inventory.CommandBlockInventory;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Faceable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static cn.nukkit.blockproperty.CommonBlockProperties.FACING_DIRECTION;

/**
 * @author GoodLucky777
 */
public class BlockCommand extends BlockSolidMeta implements BlockEntityHolder<BlockEntityCommandBlock>, Faceable {

    protected static final BooleanBlockProperty CONDITIONAL_BIT = new BooleanBlockProperty("conditional_bit", false);
    
    public static final BlockProperties PROPERTIES = new BlockProperties(
        CONDITIONAL_BIT,
        FACING_DIRECTION
    );
    
    public BlockCommand() {
        this(0);
    }
    
    public BlockCommand(int meta) {
        super(meta);
    }
    
    @Override
    public boolean canBeActivated() {
        return true;
    }
    
    @Override
    public boolean canBePulled() {
        return false;
    }
    
    @Override
    public boolean canBePushed() {
        return false;
    }
    
    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
    
    @Override
    @Nonnull
    public Class<? extends BlockEntityCommandBlock> getBlockEntityClass() {
        return BlockEntityCommandBlock.class;
    }
    
    @Override
    @Nonnull
    public String getBlockEntityType() {
        return BlockEntity.COMMAND_BLOCK;
    }
    
    @Override
    public BlockFace getBlockFace() {
        return getPropertyValue(FACING_DIRECTION);
    }
    
    @Override
    public BlockColor getColor() {
        return BlockColor.BROWN_BLOCK_COLOR;
    }
    
    @Override
    public double getHardness() {
        return -1;
    }
    
    @Override
    public int getId() {
        return COMMAND_BLOCK;
    }
    
    @Override
    public String getName() {
        return "Command Block";
    }
    
    @Override
    @Nonnull
    public BlockProperties getProperties() {
        return PROPERTIES;
    }
    
    @Override
    public double getResistance() {
        return 3600000;
    }
    
    @Override
    public boolean isBreakable(Item item) {
        return false;
    }
    
    public boolean isConditional() {
        return getBooleanValue(CONDITIONAL_BIT);
    }
    
    @Override
    public boolean onActivate(@Nonnull Item item, @Nullable Player player) {
        if (player != null) {
            getOrCreateBlockEntity();
            player.addWindow(new CommandBlockInventory(player.getUIInventory(), this));
        }
        
        return true;
    }
    
    @Override
    public boolean place(@Nonnull Item item, @Nonnull Block block, @Nonnull Block target, @Nonnull BlockFace face, double fx, double fy, double fz, Player player) {
        if (player != null) {
            if (Math.abs(player.x - this.x) < 2 && Math.abs(player.z - this.z) < 2) {
                double playerEyeY = player.y + player.getEyeHeight();
                if (playerEyeY - this.y > 2) {
                    this.setBlockFace(BlockFace.UP);
                } else if (this.y - playerEyeY > 0) {
                    this.setBlockFace(BlockFace.DOWN);
                } else {
                    this.setBlockFace(player.getHorizontalFacing().getOpposite());
                }
            } else {
                this.setBlockFace(player.getHorizontalFacing().getOpposite());
            }
        }
        
        return BlockEntityHolder.setBlockAndCreateEntity(this) != null;
    }
    
    @Override
    public void setBlockFace(BlockFace face) {
        setPropertyValue(FACING_DIRECTION, face);
    }
    
    public void setConditional(boolean condional) {
        setBooleanValue(CONDITIONAL_BIT, condional);
    }
}
