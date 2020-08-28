package cn.nukkit.block;

import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.BlockColor;

/**
 * @author good777LUCKY
 */
public class BlockCommand extends BlockCommandBase {

    public BlockCommand() {
        // Does Nothing
    }

    public BlockCommand(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return "Command Block";
    }
    
    @Override
    public int getId() {
        return COMMAND_BLOCK;
    }
    
    @Override
    public BlockColor getColor() {
        return BlockColor.BROWN_BLOCK_COLOR;
    }

    protected CompoundTag createCompoundTag(CompoundTag nbt) {
        return nbt;
    }
}
