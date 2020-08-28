package cn.nukkit.block;

import cn.nukkit.blockentity.BlockEntityCommandBlock;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.BlockColor;

/**
 * @author good777LUCKY
 */
public class BlockCommandRepeating extends BlockCommandBase {

    @Override
    public String getName() {
        return "Repeating Command Block";
    }
    
    @Override
    public int getId() {
        return REPEATING_COMMAND_BLOCK;
    }
    
    @Override
    public BlockColor getColor() {
        return BlockColor.PURPLE_BLOCK_COLOR;
    }

    @Override
    protected CompoundTag createCompoundTag(CompoundTag nbt) {
        nbt.putBoolean(BlockEntityCommandBlock.TAG_EXECUTE_ON_FIRST_TICK, true);
        return nbt;
    }
}
