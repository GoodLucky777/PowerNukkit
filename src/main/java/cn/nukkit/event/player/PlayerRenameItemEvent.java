package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;

public class PlayerRenameItemEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    private final String playerSubmittedText;
    private String adjustedText;

    public PlayerRenameItemEvent(Player player, String text) {
        this.player = player;
        this.playerSubmittedText = text;
        this.adjustedText = text;
    }

    public String getPlayerSubmittedText() {
        return playerSubmittedText;
    }

    public String getAdjustedText() {
        return adjustedText;
    }

    public void setAdjustedText(String adjustedText) {
        this.adjustedText = adjustedText;
    }
}
