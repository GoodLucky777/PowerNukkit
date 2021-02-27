package cn.nukkit.block;

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
        if (this.getLevel().getGameRules().getInteger(GameRule.MAX_COMMAND_CHAIN_LENGTH) < chain) {
            return false;
        }
        
        // Check power
        if (!(this.auto || this.powered)) {
            return false;
        }
        
        // Chain and conditional check
        if (this.conditionMet && (successCount <= 0)) {
            return false;
        }
        
        trigger();
        
        return true;
    }
}
