package cn.nukkit.event.entity;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import cn.nukkit.level.Position;

/**
 * Created by good777LUCKY
 */
public class SpawnSnowGolemEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    
    public static HandlerList getHandlers() {
        return SpawnSnowGolemEvent.handlers;
    }
    
    private Player player;
    private Position snowGolemPosition;
    private final BlockCause cause;
    
    public SpawnSnowGolemEvent(final Player player, final Position snowGolemPosition, final BlockCause cause) {
        this.player = player;
        this.snowGolemPosition = snowGolemPosition;
        this.cause = cause;
    }
    
    /**
     * Get the player who spawned the Snow Golem
     *
     * @return The player who spawned the Snow Golem
     */
    public Player getPlayer() {
        return this.player;
    }
    
    /**
     * Get the Snow Golem spawned position
     *
     * @return Position where the Snow Golem spawned
     */
    public Position getGolemPosition() {
        return this.snowGolemPosition;
    }
    
    /**
     * An enum to specify the cause of spawn the Snow Golem
     */
    public enum BlockCause {
        
        PUMPKIN,
        
        LIT_PUMPKIN
    }
}
