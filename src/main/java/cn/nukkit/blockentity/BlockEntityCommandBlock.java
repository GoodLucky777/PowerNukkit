package cn.nukkit.blockentity;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockCommand;
import cn.nukkit.block.BlockID;
import cn.nukkit.command.ICommandBlock;
import cn.nukkit.lang.TextContainer;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.StringTag;
import cn.nukkit.permission.PermissibleBase;
import cn.nukkit.permission.Permission;
import cn.nukkit.permission.PermissionAttachment;
import cn.nukkit.permission.PermissionAttachmentInfo;
import cn.nukkit.plugin.Plugin;

import io.netty.util.internal.EmptyArrays;

import java.util.Map;

import static cn.nukkit.network.protocol.CommandBlockUpdatePacket.MODE_CHAIN;
import static cn.nukkit.network.protocol.CommandBlockUpdatePacket.MODE_NORMAL;
import static cn.nukkit.network.protocol.CommandBlockUpdatePacket.MODE_REPEATING;

/**
 * @author GoodLucky777
 */
public class BlockEntityCommandBlock extends BlockEntitySpawnable implements BlockEntityNameable, ICommandBlock {

    private PermissibleBase perm;
    
    private boolean auto;
    private String command;
    private boolean conditionMet;
    private boolean executeOnFirstTick;
    private int lpCommandMode;
    private boolean lpCondionalMode;
    private boolean lpRedstoneMode;
    private long lastExecution;
    private String lastOutput;
    private String[] lastOutputParams;
    private boolean powered;
    private int successCount;
    private int tickDelay;
    private boolean trackOutput;
    private int version;
    
    private int commandBlockMode;
    private int currentTick;
    private String tempCommand;
    
    public BlockEntityCommandBlock(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
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
    
    public boolean getAuto() {
        return auto;
    }
    
    public String getCommand() {
        return command;
    }
    
    public int getCommandBlockMode() {
        int blockID = this.getLevelBlock().getId();
        if (blockID == BlockID.REPEATING_COMMAND_BLOCK) {
            commandBlockMode = MODE_REPEATING;
            return MODE_REPEATING;
        } else if (blockID == BlockID.CHAIN_COMMAND_BLOCK) {
            commandBlockMode = MODE_CHAIN;
            return MODE_CHAIN;
        } else {
            commandBlockMode = MODE_NORMAL;
            return MODE_NORMAL;
        }
    }
    
    public boolean getConditionMet() {
        return conditionMet;
    }
    
    @Override
    public Map<String, PermissionAttachmentInfo> getEffectivePermissions() {
        return this.perm.getEffectivePermissions();
    }
    
    public boolean getExecuteOnFirstTick() {
        return executeOnFirstTick;
    }
    
    public int getLPCommandMode() {
        return lpCommandMode;
    }
    
    public boolean getLPCondionalMode() {
        return lpCondionalMode;
    }
    
    public boolean getLPRedstoneMode() {
        return lpRedstoneMode;
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
    
    public ListTag<StringTag> getLastOutputParamsListTag() {
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
    
    public boolean getPowered() {
        return powered;
    }
    
    @Override
    public Server getServer() {
        return this.getServer();
    }
    
    @Override
    public CompoundTag getSpawnCompound() {
        return getDefaultCompound(this, BlockEntity.COMMAND_BLOCK)
            .putBoolean("auto", this.auto)
            .putString("Command", this.command)
            .putBoolean("conditionMet", this.conditionMet)
            .putString("CustomName", this.hasName() ? this.getName() : "")
            .putBoolean("ExecuteOnFirstTick", this.executeOnFirstTick)
            .putInt("LPCommandMode", this.lpCommandMode)
            .putBoolean("LPCondionalMode", this.lpCondionalMode)
            .putBoolean("LPRedstoneMode", this.lpRedstoneMode)
            .putLong("LastExecution", this.lastExecution)
            .putString("LastOutput", this.lastOutput)
            .putList(this.getLastOutputParamsListTag())
            .putInt("SuccessCount", this.successCount)
            .putInt("TickDelay", this.tickDelay)
            .putBoolean("TrackOutput", this.trackOutput)
            .putInt("Version", this.version);
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
    
    public int getVersion() {
        return version;
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
    public boolean hasName() {
        if (this.namedTag.contains("CustomName")) {
            return !(this.namedTag.getString("CustomName").equals(""));
        }
        
        return false;
    }
    
    @Override
    protected void initBlockEntity() {
        this.perm = new PermissibleBase(this);
        
        if (this.namedTag.contains("auto")) {
            this.auto = this.namedTag.getBoolean("auto");
        } else {
            this.auto = false;
        }
        
        if (this.namedTag.contains("Command")) {
            this.command = this.namedTag.getString("Command");
        } else {
            this.command = "";
        }
        
        if (this.namedTag.contains("conditionMet")) {
            this.conditionMet = this.namedTag.getBoolean("conditionMet");
        } else {
            this.conditionMet = false;
        }
        
        if (!this.namedTag.contains("CustomName")) {
            this.namedTag.putString("CustomName", "");
        }
        
        if (this.namedTag.contains("ExecuteOnFirstTick")) {
            this.executeOnFirstTick = this.namedTag.getBoolean("ExecuteOnFirstTick");
        } else {
            this.executeOnFirstTick = false;
        }
        
        if (this.namedTag.contains("LPCommandMode")) {
            this.lpCommandMode = this.namedTag.getInt("LPCommandMode");
        } else {
            this.lpCommandMode = 0;
        }
        
        if (this.namedTag.contains("LPCondionalMode")) {
            this.lpCondionalMode = this.namedTag.getBoolean("LPCondionalMode");
        } else {
            this.lpCondionalMode = false;
        }
        
        if (this.namedTag.contains("LPRedstoneMode")) {
            this.lpRedstoneMode = this.namedTag.getBoolean("LPRedstoneMode");
        } else {
            this.lpRedstoneMode = true;
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
        
        if (this.namedTag.contains("powered")) {
            this.powered = this.namedTag.getBoolean("powered");
        } else {
            this.powered = false;
        }
        
        if (this.namedTag.contains("SuccessCount")) {
            this.successCount = this.namedTag.getInt("SuccessCount");
        } else {
            this.successCount = 0;
        }
        
        if (this.namedTag.contains("TickDelay")) {
            this.tickDelay = this.namedTag.getInt("TickDelay");
        } else {
            this.tickDelay = 0;
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
        
        this.commandBlockMode = this.getCommandBlockMode();
        
        this.currentTick = 0;
        
        super.initBlockEntity();
    }
    
    @Override
    public boolean isBlockEntityValid() {
        int blockID = this.getBlock().getId();
        return blockID == BlockID.COMMAND_BLOCK || blockID == BlockID.REPEATING_COMMAND_BLOCK || blockID == BlockID.CHAIN_COMMAND_BLOCK;
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
    public boolean onUpdate() {
        if (this.closed) {
            return false;
        }
        
        if (!(this.powered || this.auto)) {
            return false;
        }
        
        this.timing.startTiming();
        
        if (this.getCommandBlockMode() == MODE_REPEATING) {
            if ((this.currentTick % this.getTickDelay()) == 0 || (this.executeOnFirstTick && this.currentTick == 0)) {
                this.trigger();
            }
        } else {
            if (this.currentTick == this.getTickDelay()) {
                this.trigger();
            }
        }
        
        this.currentTick++;
        
        this.timing.stopTiming();
        
        return true;
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
        this.currentTick = 0;
        this.successCount = 0;
    }
    
    @Override
    public void saveNBT() {
        super.saveNBT();
        
        this.namedTag.putBoolean("auto", this.auto);
        this.namedTag.putString("Command", this.command);
        this.namedTag.putBoolean("conditionMet", this.conditionMet);
        if (!this.namedTag.contains("CustomName")) {
            this.namedTag.putString("CustomName", "");
        }
        this.namedTag.putBoolean("ExecuteOnFirstTick", this.executeOnFirstTick);
        this.namedTag.putInt("LPCommandMode", this.lpCommandMode);
        this.namedTag.putBoolean("LPCondionalMode", this.lpCondionalMode);
        this.namedTag.putBoolean("LPRedstoneMode", this.lpRedstoneMode);
        this.namedTag.putLong("LastExecution", this.lastExecution);
        this.namedTag.putString("LastOutput", this.lastOutput);
        this.namedTag.putList(this.getLastOutputParamsListTag());
        this.namedTag.putBoolean("powered", this.powered);
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
    
    public void setAuto(boolean auto) {
        this.auto = auto;
    }
    
    public void setCommand(String command) {
        this.command = command;
    }
    
    public void setCommandBlockMode(int commandBlockMode) {
        this.commandBlockMode = commandBlockMode;
        
        BlockCommand blockCommand = (BlockCommand) this.getLevelBlock();
        switch (commandBlockMode) {
            case MODE_NORMAL:
                if (blockCommand.getId() != BlockID.COMMAND_BLOCK) {
                    blockCommand = (BlockCommand) Block.get(BlockID.COMMAND_BLOCK, blockCommand.getDamage());
                }
                break;
            case MODE_REPEATING:
                if (blockCommand.getId() != BlockID.REPEATING_COMMAND_BLOCK) {
                    blockCommand = (BlockCommand) Block.get(BlockID.REPEATING_COMMAND_BLOCK, blockCommand.getDamage());
                }
                break;
            case MODE_CHAIN:
                if (blockCommand.getId() != BlockID.CHAIN_COMMAND_BLOCK) {
                    blockCommand = (BlockCommand) Block.get(BlockID.CHAIN_COMMAND_BLOCK, blockCommand.getDamage());
                }
                break;
        }
        
        this.getLevel().setBlock(blockCommand, blockCommand, true, true);
    }
    
    public void setConditionMet(boolean conditionMet) {
        this.conditionMet = conditionMet;
        
        BlockCommand blockCommand = (BlockCommand) this.getLevelBlock();
        blockCommand.setConditional(conditionMet);
        this.getLevel().setBlock(blockCommand, blockCommand, true, true);
    }
    
    public void setExecuteOnFirstTick(boolean executeOnFirstTick) {
        this.executeOnFirstTick = executeOnFirstTick;
    }
    
    public void setLPCommandMode(int lpCommandMode) {
        this.lpCommandMode = lpCommandMode;
    }
    
    public void setLPCondionalMode(boolean lpCondionalMode) {
        this.lpCondionalMode = lpCondionalMode;
    }
    
    public void setLPRedstoneMode(boolean lpRedstoneMode) {
        this.lpRedstoneMode = lpRedstoneMode;
    }
    
    public void setLastExecution(long lastExecution) {
        this.lastExecution = lastExecution;
    }
    
    public void setLastOutput(String lastOutput) {
        this.lastOutput = lastOutput;
    }
    
    public void setLastOutputParams(String lastOutputParams, int index) {
        this.lastOutputParams[index] = lastOutputParams;
    }
    
    public void setLastOutputParams(String[] lastOutputParams) {
        this.lastOutputParams = lastOutputParams;
    }
    
    @Override
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
    
    public void setPowered(boolean powered) {
        this.powered = powered;
    }
    
    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }
    
    public void setTickDelay(int tickDelay) {
        this.tickDelay = tickDelay;
    }
    
    public void setTrackOutput(boolean trackOutput) {
        this.trackOutput = trackOutput;
    }
    
    public void setVersion(int version) {
        this.version = version;
    }
    
    public boolean trigger() {
        return trigger(0);
    }
    
    public boolean trigger(int successCount) {
        return trigger(successCount, 0);
    }
    
    public boolean trigger(int successCount, int chain) {
        if (this.getLevel().getCurrentTick() == this.lastExecution) {
            return false;
        }
        
        if (this.command.equals("")) {
            return false;
        }
        
        if (!this.getLevel().getGameRules().getBoolean(GameRule.COMMAND_BLOCKS_ENABLED)) {
            return false;
        }
        
        if (this.getLevel().getGameRules().getInt(GameRule.MAX_COMMAND_CHAIN_LENGTH) < chain) {
            return false;
        }
        
        if (chain > 0 && this.commandBlockMode == MODE_CHAIN) {
            if (!(this.auto || this.powered)) {
                return false;
            }
        }
        
        if (this.conditionMet && (this.successCount <= 0)) {
            return false;
        }
        
        if (this.command.equals("Searge")) {
            this.lastOutput = "#itzlipofutzli";
            this.successCount = 1;
            return true;
        }
        
        this.tempCommand = this.command;
        if (this.tempCommand.startsWith("/")) {
            this.tempCommand = tempCommand.substring(1);
        }
        
        this.lastOutput = "";
        this.lastOutputParams = EmptyArrays.EMPTY_STRINGS;
        
        if (this.getServer().dispatchCommand(this, tempCommand)) {
            this.lastExecution = this.getLevel().getCurrentTick();
            this.lpCommandMode = this.commandBlockMode;
            this.lpCondionalMode = this.conditionMet;
            this.lpRedstoneMode = this.auto;
            this.successCount = 1; // TODO: Make successCount depend on command results
            
            Block block = this.getBlock().getSide(this.getBlock().getBlockFace());
            if (block instanceof BlockCommand) {
                (((BlockCommand) block).getBlockEntity()).trigger(this,successCount, chain++);
            }
        } else {
            this.successCount = 0;
        }
        
        return true;
    }
}
