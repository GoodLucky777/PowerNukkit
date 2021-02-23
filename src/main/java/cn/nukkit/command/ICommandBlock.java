package cn.nukkit.command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.lang.TextContainer;
import cn.nukkit.level.GameRule;
import cn.nukkit.permission.PermissibleBase;
import cn.nukkit.permission.Permission;
import cn.nukkit.permission.PermissionAttachment;
import cn.nukkit.permission.PermissionAttachmentInfo;

import java.util.Map;

public interface ICommandBlock extends CommandSender {

    public int CURRENT_VERSION = 13;
    
    private PermissibleBase perm;
    
    static ICommandBlock {
        this.perm = new PermissibleBase(this);
    }
    
    static int getCurrentVersion() {
        return CURRENT_VERSION;
    }
    
    int getVersion();
    
    static boolean hasPermission(String name) {
        return this.perm.hasPermission(name);
    }
    
    static boolean isPermissionSet(Permission permission) {
        return this.perm.isPermissionSet(permission);
    }
    
    static boolean isPermissionSet(String name) {
        return this.perm.isPermissionSet(name);
    }
    
    void setVersion(int version);
}
