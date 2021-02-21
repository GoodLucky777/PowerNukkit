package cn.nukkit.blockentity;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.StringTag;

/**
 * @author GoodLucky777
 */
public class BlockEntityCommandBlock extends BlockEntitySpawnable implements BlockEntityNameable {

    public int CURRENT_VERSION = 13;
    
    private boolean auto;
    private String command;
    private boolean conditionMet;
    private boolean executeOnFirstTick;
    private int lpCommandMode;
    private boolean lpCondionalMode;
    private boolean lpRedstoneMode;
    private long lastExecution;
    private String lastOutput;
    private ListTag<StringTag> lastOutputParams;
    private int successCount;
    private int tickDelay;
    private boolean trackOutput;
    private int version;
    
    public BlockEntityCommandBlock(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }
    
    public boolean getAuto() {
        return auto;
    }
    
    public String getCommand() {
        return command;
    }
    
    public boolean getConditionMet() {
        return conditionMet;
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
    
    public ListTag<StringTag> getLastOutputParams() {
        return lastOutputParams;
    }
    
    @Override
    public String getName() {
        if (this.hasName()) {
            return this.namedTag.getString("CustomName");
        }
        
        return "Command Block";
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
    public boolean hasName() {
        if (this.namedTag.contains("CustomName")) {
            return !(this.namedTag.getString("CustomName").equals(""));
        }
        
        return false;
    }
    
    @Override
    protected void initBlockEntity() {
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
            this.lastOutputParams = (ListTag<StringTag>) this.namedTag.getList("LastOutputParams");
        } else {
            this.lastOutputParams = new ListTag<StringTag>("LastOutputParams");
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
            this.trackOutput = true;
        }
        
        if (this.namedTag.contains("Version")) {
            this.version = this.namedTag.getInt("Version");
        } else {
            this.version = CURRENT_VERSION;
        }
        
        super.initBlockEntity();
    }
    
    @Override
    public boolean isBlockEntityValid() {
        int blockId = this.getBlock().getId();
        return blockId == BlockID.COMMAND_BLOCK || blockId == BlockID.REPEATING_COMMAND_BLOCK || blockId == BlockID.CHAIN_COMMAND_BLOCK;
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
        this.namedTag.putList(this.lastOutputParams);
        this.namedTag.putInt("SuccessCount", this.successCount);
        this.namedTag.putInt("TickDelay", this.tickDelay);
        this.namedTag.putBoolean("TrackOutput", this.trackOutput);
        this.namedTag.putInt("Version", this.version);
    }
    
    public void setAuto(boolean auto) {
        this.auto = auto;
    }
    
    public void setCommand(String command) {
        this.command = command;
    }
    
    public void setConditionalMet(boolean conditionMet) {
        this.conditionMet = conditionMet;
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
    
    public void setLastOutputParams(ListTag<StringTag> lastOutputParams) {
        this.lastOutputParams = lastOutputParams;
    }
    
    @Override
    public void setName(String name) {
        if (name == null || name.equals("")) {
            this.namedTag.putString("CustomName", "");
        } else {
            this.namedTag.putString("CustomName", name);
        }
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
}
