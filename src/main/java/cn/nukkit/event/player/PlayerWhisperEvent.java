package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.permission.Permissible;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

/**
 * @author GoodLucky777
 */
public class PlayerWhisperEvent extends PlayerMessageEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    
    public static HandlerList getHandlers() {
        return handlers;
    }
    
    protected Set<CommandSender> recipients = new HashSet<>();
    
    public PlayerWhisperEvent(@Nonnull Player player, @Nonnull String message, @Nonnull CommandSender recipient) {
        this(player, message, new HashSet<CommandSender>(recipient));
    }
    
    public PlayerWhisperEvent(@Nonnull Player player, @Nonnull String message, @Nonnull Set<CommandSender> recipients) {
        this.player = player;
        this.message = message;
        this.recipients = recipients;
    }
    
    public void setPlayer(Player player) {
        this.player = player;
    }
    
    public Set<CommandSender> getRecipients() {
        return this.recipients;
    }
    
    public void setRecipients(Set<CommandSender> recipients) {
        this.recipients = recipients;
    }
}
