package cn.nukkit.block;

import cn.nukkit.utils.BlockColor;

/**
 * @author GoodLucky777
 */
public class BlockCommandRepeating extends BlockCommand {

    @Override
    public BlockColor getColor() {
        return BlockColor.PURPLE_BLOCK_COLOR;
    }
    
    @Override
    public int getId() {
        return REPEATING_COMMAND_BLOCK;
    }
    
    @Override
    public String getName() {
        return "Repeating Command Block";
    }
}
