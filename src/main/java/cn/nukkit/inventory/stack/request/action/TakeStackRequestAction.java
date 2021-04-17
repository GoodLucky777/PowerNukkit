package cn.nukkit.inventory.stack.request.action;

import cn.nukkit.inventory.stack.request.StackRequestSlotInfo;

/**
 * @author GoodLucky777
 */
public class TakeStackRequestAction implements TransferStackRequestAction {

    public byte count;
    public StackRequestSlotInfo source;
    public StackRequestSlotInfo destination;
    
    @Override
    public StackRequestActionType getType() {
        return StackRequestActionType.TAKE;
    }
}
