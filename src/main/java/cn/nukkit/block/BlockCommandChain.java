package cn.nukkit.block;

import cn.nukkit.blockentity.BlockEntityCommandBlock;
import cn.nukkit.level.GameRule;
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
    
    public boolean triggerChain(int successCount, int chainLength) {
        // Check max chain length gamerule
        if (this.getLevel().getGameRules().getInteger(GameRule.MAX_COMMAND_CHAIN_LENGTH) < chainLength) {
            return false;
        }
        
        BlockEntityCommandBlock blockEntityCommandBlock = this.getBlockEntity();
        
        // Check power
        if (!(blockEntityCommandBlock.getAuto() || blockEntityCommandBlock.getPowered())) {
            return false;
        }
        
        // Chain and conditional check
        if (blockEntityCommandBlock.getConditionMet() && (successCount <= 0)) {
            return false;
        }
        
        blockEntityCommandBlock.trigger();
        
        return true;
    }
}
