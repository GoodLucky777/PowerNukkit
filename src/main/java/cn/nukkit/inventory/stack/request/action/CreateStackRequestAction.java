package cn.nukkit.inventory.stack.request.action;

import cn.nukkit.inventory.stack.request.StackRequestSlotInfo;

/**
 * @author GoodLucky777
 */
public class CreateStackRequestAction implements StackRequestAction {

    public byte count;
    
    @Override
    public StackRequestActionType getType() {
        return StackRequestActionType.CREATE;
    }
}
