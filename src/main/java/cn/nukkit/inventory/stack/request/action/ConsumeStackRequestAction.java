package cn.nukkit.inventory.stack.request.action;

import cn.nukkit.inventory.stack.request.StackRequestSlotInfo;

/**
 * @author GoodLucky777
 */
public class ConsumeStackRequestAction implements StackRequestAction {

    public byte count;
    public StackRequestSlotInfo source;
    
    @Override
    public StackRequestActionType getType() {
        return StackRequestActionType.CONSUME;
    }
}
