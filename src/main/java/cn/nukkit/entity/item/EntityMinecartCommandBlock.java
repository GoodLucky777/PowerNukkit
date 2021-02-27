package cn.nukkit.entity.item;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.command.ICommandBlock;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.ByteEntityData;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.entity.data.StringEntityData;
import cn.nukkit.inventory.CommandBlockInventory;
import cn.nukkit.item.Item;
import cn.nukkit.lang.TextContainer;
import cn.nukkit.level.GameRule;
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
    
    private boolean activated;
    private String tempCommand;
    
    public EntityMinecartCommandBlock(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        
        setDisplayBlock(Block.get(Block.REPEATING_COMMAND_BLOCK), false);
    }
    
    @Override
    public void activate(int x, int y, int z, boolean flag) {
        this.activated = flag;
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
    
    public int getCurrentTickCount() {
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
        setDataProperty(new StringEntityData(DATA_COMMAND_BLOCK_COMMAND, this.command));
        
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
        setDataProperty(new ByteEntityData(DATA_COMMAND_BLOCK_EXECUTE_ON_FIRST_TICK, this.executeOnFirstTick ? 1 : 0));
        
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
        setDataProperty(new StringEntityData(DATA_COMMAND_BLOCK_LAST_OUTPUT, this.lastOutput));
        
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
        setDataProperty(new IntEntityData(DATA_COMMAND_BLOCK_TICK_DELAY, this.tickDelay));
        
        if (this.namedTag.contains("TrackOutput")) {
            this.trackOutput = this.namedTag.getBoolean("TrackOutput");
        } else {
            this.trackOutput = this.getLevel().getGameRules().getBoolean(GameRule.SEND_COMMAND_FEEDBACK);
        }
        setDataProperty(new ByteEntityData(DATA_COMMAND_BLOCK_TRACK_OUTPUT, this.trackOutput ? 1 : 0));
        
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
    public boolean onUpdate(int currentTick) {
        this.timing.startTiming();
        
        if (this.activated) {
            int tickDiff = currentTick - lastUpdate;
            
            lastUpdate = currentTick;
            
            if (this.currentTickCount == this.tickDelay) {
                this.trigger();
                this.currentTickCount = 0;
            } else {
                this.currentTickCount += tickDiff;
            }
        }
        
        this.timing.stopTiming();
        
        return super.onUpdate(currentTick);
    }
    
    @Override
    public void recalculatePermissions() {
        this.perm.recalculatePermissions();
    }
    
    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        this.perm.removeAttachment(attachment);
    }
    
    public void reset() {
        this.currentTickCount = 0;
        this.successCount = 0;
    }
    
    @Override
    public void saveNBT() {
        super.saveNBT();

        this.namedTag.putString("Command", this.command);
        this.namedTag.putInt("CurrentTickCount", this.currentTickCount);
        if (!this.namedTag.contains("CustomName")) {
            this.namedTag.putString("CustomName", "");
        }
        this.namedTag.putBoolean("ExecuteOnFirstTick", this.executeOnFirstTick);
        this.namedTag.putLong("LastExecution", this.lastExecution);
        this.namedTag.putString("LastOutput", this.lastOutput);
        this.namedTag.putList(this.getLastOutputParamsAsListTag(this.lastOutputParams));
        this.namedTag.putInt("SuccessCount", this.successCount);
        this.namedTag.putInt("TickDelay", this.tickDelay);
        this.namedTag.putBoolean("TrackOutput", this.trackOutput);
        this.namedTag.putInt("Version", this.version);
    }
    
    @Override
    public void sendMessage(String message) {
        if (this.trackOutput) {
            this.lastOutput = message;
            this.lastOutputParams = EmptyArrays.EMPTY_STRINGS; // TODO: Implement lastOutputParams
        }
        
        if (this.getLevel().getGameRules().getBoolean(GameRule.COMMAND_BLOCK_OUTPUT)) {
            for (Player player : this.getLevel().getPlayers().values()) {
                if (player.isOp()) {
                    player.sendMessage(message);
                }
            }
        }
    }
    
    @Override
    public void sendMessage(TextContainer message) {
        this.sendMessage(message.toString());
    }
    
    public void setCommand(String command) {
        this.command = command;
        
        setDataProperty(new StringEntityData(DATA_COMMAND_BLOCK_COMMAND, this.command));
    }
    
    public void setExecuteOnFirstTick(boolean executeOnFirstTick) {
        this.executeOnFirstTick = executeOnFirstTick;
        
        setDataProperty(new ByteEntityData(DATA_COMMAND_BLOCK_EXECUTE_ON_FIRST_TICK, this.executeOnFirstTick ? 1 : 0));
    }
    
    public void setLastExecution(long lastExecution) {
        this.lastExecution = lastExecution;
    }
    
    public void setLastOutput(String lastOutput) {
        this.lastOutput = lastOutput;
        
        setDataProperty(new StringEntityData(DATA_COMMAND_BLOCK_LAST_OUTPUT, this.lastOutput));
    }
    
    public void setLastOutputParams(String lastOutputParams, int index) {
        this.lastOutputParams[index] = lastOutputParams;
    }
    
    public void setLastOutputParams(String[] lastOutputParams) {
        this.lastOutputParams = lastOutputParams;
    }
    
    public void setName(String name) {
        if (name == null) {
            this.namedTag.putString("CustomName", "");
        } else {
            this.namedTag.putString("CustomName", name);
        }
    }
    
    @Override
    public void setOp(boolean value) {
        // Does Nothing
    }
    
    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }
    
    public void setTickDelay(int tickDelay) {
        this.tickDelay = tickDelay;
        
        setDataProperty(new IntEntityData(DATA_COMMAND_BLOCK_TICK_DELAY, this.tickDelay));
    }
    
    public void setTrackOutput(boolean trackOutput) {
        this.trackOutput = trackOutput;
        
        setDataProperty(new ByteEntityData(DATA_COMMAND_BLOCK_TRACK_OUTPUT, this.trackOutput ? 1 : 0));
    }
    
    public void setVersion(int version) {
        this.version = version;
    }
    
    public boolean trigger() {
        // Don't trigger at same tick
        if (this.getLevel().getCurrentTick() == this.lastExecution) {
            return false;
        }
        
        // Ignore (Empty command)
        if (this.command.equals("")) {
            return false;
        }
        
        // Check gamerule
        if (!this.getLevel().getGameRules().getBoolean(GameRule.COMMAND_BLOCKS_ENABLED)) {
            return false;
        }
        
        // Easter Egg
        if (this.command.equals("Searge")) {
            this.lastOutput = "#itzlipofutzli";
            this.successCount = 1;
            return true;
        }
        
        // Remove a "/" if it exists
        this.tempCommand = this.command;
        if (this.tempCommand.startsWith("/")) {
            this.tempCommand = tempCommand.substring(1);
        }
        
        // Reset last outputs
        this.lastOutput = "";
        this.lastOutputParams = EmptyArrays.EMPTY_STRINGS;
        
        // Run command
        if (this.getServer().dispatchCommand(this, tempCommand)) {
            this.lastExecution = this.getLevel().getCurrentTick();
            this.successCount = 1; // TODO: Make successCount depend on command results
        } else {
            this.successCount = 0;
        }
        
        return true;
    }
}
