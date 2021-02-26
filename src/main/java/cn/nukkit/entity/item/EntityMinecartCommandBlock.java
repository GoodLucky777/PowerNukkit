package cn.nukkit.entity.item;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.command,ICommandBlock;
import cn.nukkit.entity.Entity;
import cn.nukkit.inventory.CommandBlockInventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.StringTag;
import cn.nukkit.permission.PermissibleBase;
import cn.nukkit.permission.Permission;
import cn.nukkit.permission.PermissionAttachment;
import cn.nukkit.permission.PermissionAttachmentInfo;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.MinecartType;

import io.netty.util.internal.EmptyArrays;

import java.util.Map;

/**
 * @author GoodLucky777
 */
public class EntityMinecartCommandBlock extends EntityMinecartAbstract implements ICommandBlock {

    public static final int NETWORK_ID = 100;
    
    private PermissibleBase perm;
    
    private String command;
    private int currentTickCount;
    private boolean executeOnFirstTick;
    private long lastExecution;
    private String lastOutput;
    private String[] lastOutputParams;
    private int successCount;
    private int tickDelay;
    private boolean trackOutput;
    private int version;
    
    public EntityMinecartCommandBlock(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        setDisplayBlock(Block.get(Block.REPEATING_COMMAND_BLOCK), false);
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
    
    public String getCommand() {
        return command;
    }
    
    public boolean getCurrentTickCount() {
        return currentTickCount;
    }
    
    @Override
    public Map<String, PermissionAttachmentInfo> getEffectivePermissions() {
        return this.perm.getEffectivePermissions();
    }
    
    public boolean getExecuteOnFirstTick() {
        return executeOnFirstTick;
    }
    
    public long getLastExecution() {
        return lastExecution;
    }
    
    public String getLastOutput() {
        return lastOutput;
    }
    
    public String[] getLastOutputParams() {
        return lastOutputParams;
    }
    
    public ListTag<StringTag> getLastOutputParamsAsListTag() {
        ListTag<StringTag> tempLastOutputParmas = new ListTag<StringTag>("LastOutputParams");
        for (int i = 0; i < this.lastOutputParams.length; i++) {
            tempLastOutputParmas.add(new StringTag("", this.lastOutputParams[i]));
        }
        
        return tempLastOutputParmas;
    }
    
    @Override
    public String getName() {
        if (this.hasName()) {
            return this.namedTag.getString("CustomName");
        }
        
        return "!";
    }
    
    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }
    
    @Override
    public Server getServer() {
        return this.getServer();
    }
    
    public int getSuccessCount() {
        return successCount;
    }
    
    public int getTickDelay() {
        return tickDelay;
    }
    
    public boolean getTrackOutput() {
        return trackOutput;
    }
    
    @Override
    public MinecartType getType() {
        return MinecartType.valueOf(6);
    }
    
    public int getVersion() {
        return version;
    }
    
    public boolean hasName() {
        if (this.namedTag.contains("CustomName")) {
            return !(this.namedTag.getString("CustomName").equals(""));
        }
        
        return false;
    }
    
    @Override
    public boolean hasPermission(Permission permission) {
        return this.perm.hasPermission(permission);
    }
    
    @Override
    public boolean hasPermission(String name) {
        return this.perm.hasPermission(name);
    }
    
    @Override
    public void initEntity() {
        super.initEntity();
        
        if (this.namedTag.contains("Command")) {
            this.command = this.namedTag.getString("Command");
        } else {
            this.command = "";
        }
        
        if (this.namedTag.contains("CurrentTickCount")) {
            this.currentTickCount = this.namedTag.getInt("CurrentTickCount");
        } else {
            this.currentTickCount = 0;
        }
        
        if (!this.namedTag.contains("CustomName")) {
            this.namedTag.putString("CustomName", "");
        }
        
        if (this.namedTag.contains("ExecuteOnFirstTick")) {
            this.executeOnFirstTick = this.namedTag.getBoolean("ExecuteOnFirstTick");
        } else {
            this.executeOnFirstTick = false;
        }
        
        if (this.namedTag.contains("LastExecution")) {
            this.lastExecution = this.namedTag.getLong("LastExecution");
        } else {
            this.lastExecution = 0L;
        }
        
        if (this.namedTag.contains("LastOutput")) {
            this.lastOutput = this.namedTag.getString("LastOutput");
        } else {
            this.lastOutput = "";
        }
        
        if (this.namedTag.contains("LastOutputParams")) {
            ListTag<StringTag> tempLastOutputParams = (ListTag<StringTag>) this.namedTag.getList("LastOutputParams");
            for (int i = 0; i < tempLastOutputParams.size(); i++) {
                this.lastOutputParams[i] = tempLastOutputParams.get(i).parseValue();
            }
        } else {
            this.lastOutputParams = EmptyArrays.EMPTY_STRINGS;
        }
        
        if (this.namedTag.contains("SuccessCount")) {
            this.successCount = this.namedTag.getInt("SuccessCount");
        } else {
            this.successCount = 0;
        }
        
        if (this.namedTag.contains("TickDelay")) {
            this.tickDelay = this.namedTag.getInt("TickDelay");
        } else {
            this.tickDelay = 3;
        }
        
        if (this.namedTag.contains("TrackOutput")) {
            this.trackOutput = this.namedTag.getBoolean("TrackOutput");
        } else {
            this.trackOutput = this.getLevel().getGameRules().getBoolean(GameRule.SEND_COMMAND_FEEDBACK);
        }
        
        if (this.namedTag.contains("Version")) {
            this.version = this.namedTag.getInt("Version");
        } else {
            this.version = CURRENT_VERSION;
        }
    }
    
    @Override
    public boolean isOp() {
        return true;
    }
    
    @Override
    public boolean isPermissionSet(Permission permission) {
        return this.perm.isPermissionSet(permission);
    }
    
    @Override
    public boolean isPermissionSet(String name) {
        return this.perm.isPermissionSet(name);
    }
    
    public boolean isPlayer() {
        return false;
    }
    
    @Override
    public boolean isRideable(){
        return false;
    }
    
    @Override
    public boolean mountEntity(Entity entity, byte mode) {
        return false;
    }
    
    @Override
    public boolean onInteract(Player player, Item item, Vector3 clickedPos) {
        if (player.canUseCommandBlock()) {
            player.addWindow(new CommandBlockInventory(player.getUIInventory(), this));
        }
        
        return false;
    }
    
    @Override
    public void saveNBT() {
        super.saveNBT();

        // TODO
    }
}
