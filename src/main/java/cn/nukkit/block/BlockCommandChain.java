package cn.nukkit.block;

import cn.nukkit.utils.BlockColor;

/**
 * @author GoodLucky777
 */
public class BlockCommandChain extends BlockCommand {

    @Override
    public BlockColor getColor() {
        return BlockColor.GREEN_BLOCK_COLOR;
    }
    
    @Override
    public int getId() {
        return CHAIN_COMMAND_BLOCK;
    }
    
    @Override
    public String getName() {
        return "Chain Command Block";
    }
}
