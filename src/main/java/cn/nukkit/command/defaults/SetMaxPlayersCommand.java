package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.lang.TranslationContainer;

/**
 * @author good777LUCKY
 */
public class SetMaxPlayersCommand extends VanillaCommand {

    public SetMaxPlayersCommand(String name) {
        super(name, "%nukkit.command.setmaxplayers.description", "%commands.setmaxplayers.usage");
        this.setPermission("nukkit.command.setdayplayers");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                new CommandParameter("maxPlayers", CommandParamType.INT, false)
        });
    }
    
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        
        if (args.length > 1 || args.length == 0) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }
        
        int maxPlayers = 1;
        
        maxPlayers = Integer.parseInt(args[0]);
        
        int fail = -1;
        if (maxPlayers < 1) {
            maxPlayers = 1;
            fail = 1;
        } else if (maxPlayers > 30) {
            maxPlayers = 30;
            fail = 2;
        } else if (maxPlayers < Server.getInstance().getOnlinePlayers().values()) {
            maxPlayers = Server.getInstance().getOnlinePlayers().values();
            fail = 1;
        }
        
        Server.getInstance().setMaxPlayers(maxPlayers);
        Command.broadcastCommandMessage(sender, new TranslationContainer("commands.setmaxplayers.success", maxPlayers));
        
        if (fail == 1) {
            sender.sendMessage(new TranslationContainer("commands.setmaxplayers.success.lowerbound"));
        } else if (fail == 2) {
            sender.sendMessage(new TranslationContainer("commands.setmaxplayers.success.upperbound"));
        }
        
        return true;
    }
}
