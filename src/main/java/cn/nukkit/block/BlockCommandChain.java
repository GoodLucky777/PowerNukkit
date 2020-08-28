package cn.nukkit.block;

import cn.nukkit.blockentity.BlockEntityCommandBlock;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.BlockColor;

/**
 * @author good777LUCKY
 */
public class BlockCommandChain extends BlockCommandBase {

    @Override
    public String getName() {
        return "Chain Command Block";
    }
    
    @Override
    public int getId() {
        return CHAIN_COMMAND_BLOCK;
    }
    
    @Override
    public BlockColor getColor() {
        return BlockColor.GREEN_BLOCK_COLOR;
    }

    @Override
    protected CompoundTag createCompoundTag(CompoundTag nbt) {
        nbt.putBoolean(BlockEntityCommandBlock.TAG_AUTO, true);
        return nbt;
    }
}
