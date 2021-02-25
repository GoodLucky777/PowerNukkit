package cn.nukkit.command;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.network.protocol.types.CommandOriginData;

import java.util.List;

/**
 * @author MagicDroidX (Nukkit Project)
 */
public interface CommandMap {

    void registerAll(String fallbackPrefix, List<? extends Command> commands);

    boolean register(String fallbackPrefix, Command command);

    boolean register(String fallbackPrefix, Command command, String label);

    void registerSimpleCommands(Object object);
    
    @Since("1.4.0.0-PN")
    default boolean dispatch(CommandSender sender, String cmdLine) {
        this.dispatchCommand(sender, cmdLine, CommandOriginData.DEFAULT);
    }

    void clearCommands();

    Command getCommand(String name);
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    default boolean dispatch(CommandSender sender, String cmdLine, CommandOriginData commandOriginData) {
        
    }
}
