package cn.nukkit.command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.lang.TextContainer;
import cn.nukkit.level.GameRule;
import cn.nukkit.permission.PermissibleBase;
import cn.nukkit.permission.Permission;
import cn.nukkit.permission.PermissionAttachment;
import cn.nukkit.permission.PermissionAttachmentInfo;
import cn.nukkit.plugin.Plugin;

import java.util.Map;

/**
 * @author GoodLucky777
 */
public class CommandBlockCommandSender implements CommandSender {

    private final PermissibleBase perm;
    
    public CommandBlockCommandSender() {
        this.perm = new PermissibleBase(this);
    }
    
    @Override
    public boolean isPermissionSet(String name) {
        return this.perm.isPermissionSet(name);
    }
    
    @Override
    public boolean isPermissionSet(Permission permission) {
        return this.perm.isPermissionSet(permission);
    }
    
    @Override
    public boolean hasPermission(String name) {
        return this.perm.hasPermission(name);
    }
    
    @Override
    public boolean hasPermission(Permission permission) {
        return this.perm.hasPermission(permission);
    }
    
    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return this.perm.addAttachment(plugin);
    }
    
    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name) {
        return this.perm.addAttachment(plugin, name);
    }
    
    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, Boolean value) {
        return this.perm.addAttachment(plugin, name, value);
    }
    
    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        this.perm.removeAttachment(attachment);
    }
    
    @Override
    public void recalculatePermissions() {
        this.perm.recalculatePermissions();
    }
    
    @Override
    public Map<String, PermissionAttachmentInfo> getEffectivePermissions() {
        return this.perm.getEffectivePermissions();
    }
    
    public boolean isPlayer() {
        return false;
    }
    
    @Override
    public Server getServer() {
        return Server.getInstance();
    }
    
    @Override
    public void sendMessage(String message) {
        message = this.getServer().getLanguage().translateString(message);
        
        if (Server.getInstance().getDefaultLevel().getGameRules().getBoolean(GameRule.COMMAND_BLOCK_OUTPUT)) {
            for (Player player : Server.getInstance().getDefaultLevel().getPlayers().values()) {
                if (player.isOp()) {
                    player.sendMessage(message);
                }
            }
        }
    }
    
    @Override
    public void sendMessage(TextContainer message) {
        this.sendMessage(this.getServer().getLanguage().translate(message));
    }
    
    @Override
    public String getName() {
        return "Command Block";
    }
    
    @Override
    public boolean isOp() {
        return true;
    }
    
    @Override
    public void setOp(boolean value) {
        // Does Nothing
    }
}
