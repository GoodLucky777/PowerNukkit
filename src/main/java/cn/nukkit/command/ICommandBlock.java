package cn.nukkit.command;

import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.StringTag;

public interface ICommandBlock extends CommandSender {

    public int CURRENT_VERSION = 13;
    
    static int getCurrentVersion() {
        return CURRENT_VERSION;
    }
    
    String getCommand();
    
    boolean getExecuteOnFirstTick();
    
    long getLastExecution();
    
    String getLastOutput();
    
    String[] getLastOutputParams();
    
    default ListTag<StringTag> getLastOutputParamsAsListTag(String[] lastOutputParams) {
        ListTag<StringTag> tempLastOutputParmas = new ListTag<StringTag>("LastOutputParams");
        for (int i = 0; i < lastOutputParams.length; i++) {
            tempLastOutputParmas.add(new StringTag("", lastOutputParams[i]));
        }
        
        return tempLastOutputParmas;
    }
    
    int getSuccessCount();
    
    int getTickDelay();
    
    boolean getTrackOutput();
    
    int getVersion();
    
    void reset();
    
    void setCommand(String command);
    
    void setExecuteOnFirstTick(boolean executeOnFirstTick);
    
    void setLastExecution(long lastExecution);
    
    void setLastOutput(String lastOutput);
    
    void setLastOutputParams(String lastOutputParams, int index);
    
    void setLastOutputParams(String[] lastOutputParams);
    
    void setSuccessCount(int successCount);
    
    void setTickDelay(int tickDelay);
    
    void setTrackOutput(boolean trackOutput);
    
    void setVersion(int version);
    
    default boolean trigger() {
        return this.trigger(1);
    }
    
    boolean trigger(int successCount);
}
