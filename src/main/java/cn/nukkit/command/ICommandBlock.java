package cn.nukkit.command;

public interface ICommandBlock extends CommandSender {

    public int CURRENT_VERSION = 13;
    
    static int getCurrentVersion() {
        return CURRENT_VERSION;
    }
    
    int getVersion();
    
    void setVersion(int version);
    
    boolean trigger(int successCount);
}
