package cn.nukkit.inventory.stack.request;

import cn.nukkit.inventory.stack.request.action.StackRequestAction;

import java.util.List;

/**
 * @author GoodLucky777
 */
public class ItemStackRequest {

    public int requestId;
    public List<StackRequestAction> actions;
    public List<String> filterStrings;
}
