package cn.nukkit.inventory.stack.request;

import cn.nukkit.inventory.stack.request.action.StackRequestAction;

/**
 * @author GoodLucky777
 */
public class ItemStackRequest {

    public int requestId;
    public StackRequestAction[] actions;
    public String[] filterStrings;
}
