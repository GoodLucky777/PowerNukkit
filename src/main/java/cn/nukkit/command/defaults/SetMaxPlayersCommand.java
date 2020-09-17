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
        int onlinePlayers = Server.getInstance().getOnlinePlayers().size();
        
        byte failType = -1;
        if (maxPlayers < 1) {
            maxPlayers = 1;
            failType = 0;
        } else if (maxPlayers > 30) {
            maxPlayers = 30;
            failType = 1;
        } else if (maxPlayers < onlinePlayers) {
            maxPlayers = onlinePlayers;
            failType = 0;
        }
        
        Server.getInstance().setMaxPlayers(maxPlayers);
        Command.broadcastCommandMessage(sender, new TranslationContainer("commands.setmaxplayers.success", maxPlayers));
        
        if (failType == 0) {
            sender.sendMessage(new TranslationContainer("commands.setmaxplayers.success.lowerbound"));
        } else if (failType == 1) {
            sender.sendMessage(new TranslationContainer("commands.setmaxplayers.success.upperbound"));
        }
        
        return true;
    }
}
