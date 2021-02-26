package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.event.player.PlayerWhisperEvent;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;

/**
 * @author GoodLucky777
 */
public class MessageCommand extends VanillaCommand {

    public MessageCommand(String name) {
        super(name, "%nukkit.command.message.description", "%nukkit.command.message.usage", new String[]{ "tell", "w" });
        this.setPermission("nukkit.command.message");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                CommandParameter.newType("target", true, CommandParamType.TARGET),
                CommandParameter.newType("message", true, CommandParamType.MESSAGE)
        });
    }
    
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        
        if (args.length == 0 || args.length > 2) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }
        
        String message = "";
        if (args.length == 2) {
            message = args[1];
        }
        
        Player player = sender.getServer().getPlayer(args[0]);
        if (player != null) {
            if (((Player) sender).getRemoveFormat()) {
                message = TextFormat.clean(message, true);
            }
            
            for (String msg : message.split("\n")) {
                if (!msg.trim().isEmpty() && msg.length() <= 255 && (((Player) sender).getMessageCounter() - 1) > 0) {
                    PlayerWhisperEvent ev = new PlayerWhisperEvent((Player) sender, args[1], (CommandSender) player);
                    sender.getServer().getPluginManager().callEvent(ev);
                    if (ev.isCancelled()) {
                        break;
                    }
                    
                    player.sendWhisper(ev.getPlayer().getName(), ev.getMessage());
                }
            }
        } else {
            sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.noTargetMatch"));
            return false;
        }
        
        return true;
    }
}
