package cn.nukkit.inventory.stack.request.action;

import cn.nukkit.inventory.stack.request.StackRequestSlotInfo;

/**
 * @author GoodLucky777
 */
public interface TransferStackRequestAction extends StackRequestAction {

    byte getCount();
    
    StackRequestSlotInfo getSource();
    
    StackRequestSlotInfo getDestination();
}
