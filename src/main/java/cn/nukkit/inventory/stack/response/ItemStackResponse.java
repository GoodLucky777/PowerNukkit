package cn.nukkit.inventory.stack.response;

import java.util.List;

/**
 * @author GoodLucky777
 */
public class ItemStackResponse {

    public StackResponseStatus result;
    public int requestId;
    public List<ContainerEntry> containers;
}
