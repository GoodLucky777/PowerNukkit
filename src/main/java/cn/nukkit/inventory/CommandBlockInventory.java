package cn.nukkit.inventory;

import cn.nukkit.level.Position;

/**
 * @author GoodLucky777
 */
public class CommandBlockInventory extends FakeBlockUIComponent {

    public CommandBlockInventory(PlayerUIInventory playerUI, Position position) {
        super(playerUI, InventoryType.COMMAND_BLOCK, 0, position);
    }
}
