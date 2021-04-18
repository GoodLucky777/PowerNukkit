package cn.nukkit.inventory.stack.request.action;

import cn.nukkit.inventory.stack.request.StackRequestSlotInfo;

/**
 * @author GoodLucky777
 */
public class DropStackRequestAction implements StackRequestAction {

    public byte count;
    public StackRequestSlotInfo source;
    public boolean randomly; // TODO: What is this
    
    @Override
    public StackRequestActionType getType() {
        return StackRequestActionType.DROP;
    }
}
