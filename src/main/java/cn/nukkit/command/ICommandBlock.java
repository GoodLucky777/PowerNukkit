package cn.nukkit.command;

public interface ICommandBlock {

    public int CURRENT_VERSION = 13;
    
    static int getCurrentVersion() {
        return CURRENT_VERSION;
    }
    
    int getVersion();
    
    void setVersion(int version);
}
