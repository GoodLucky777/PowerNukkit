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

public interface ICommandBlock extends CommandSender {

    public int CURRENT_VERSION = 13;
    
    PermissibleBase perm = new PermissibleBase(this);
    
    static PermissionAttachment addAttachment(Plugin plugin) {
        return this.perm.addAttachment(plugin);
    }
    
    static PermissionAttachment addAttachment(Plugin plugin, String name) {
        return this.perm.addAttachment(plugin, name);
    }
    
    static PermissionAttachment addAttachment(Plugin plugin, String name, Boolean value) {
        return this.perm.addAttachment(plugin, name, value);
    }
    
    static int getCurrentVersion() {
        return CURRENT_VERSION;
    }
    
    static Map<String, PermissionAttachmentInfo> getEffectivePermissions() {
        return this.perm.getEffectivePermissions();
    }
    
    default String getName() {
        return "Command Block";
    }
    
    static Server getServer() {
        return Server.getInstance();
    }
    
    int getVersion();
    
    static boolean hasPermission(Permission permission) {
        return this.perm.hasPermission(permission);
    }
    
    static boolean hasPermission(String name) {
        return this.perm.hasPermission(name);
    }
    
    static boolean isOp() {
        return true;
    }
    
    static boolean isPermissionSet(Permission permission) {
        return this.perm.isPermissionSet(permission);
    }
    
    static boolean isPermissionSet(String name) {
        return this.perm.isPermissionSet(name);
    }
    
    static boolean isPlayer() {
        return false;
    }
    
    static void recalculatePermissions() {
        this.perm.recalculatePermissions();
    }
    
    static void removeAttachment(PermissionAttachment attachment) {
        this.perm.removeAttachment(attachment);
    }
    
    static void sendMessage(String message) {
        message = this.getServer().getLanguage().translateString(message);
        
        if (Server.getInstance().getDefaultLevel().getGameRules().getBoolean(GameRule.COMMAND_BLOCK_OUTPUT)) {
            for (Player player : Server.getInstance().getDefaultLevel().getPlayers().values()) {
                if (player.isOp()) {
                    player.sendMessage(message);
                }
            }
        }
    }
    
    static void sendMessage(TextContainer message) {
        this.sendMessage(this.getServer().getLanguage().translate(message));
    }
    
    static void setOp(boolean value) {
        // Does Nothing
    }
    
    void setVersion(int version);
}
