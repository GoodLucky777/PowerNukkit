package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.GameRules;
import cn.nukkit.level.Level;
import cn.nukkit.network.protocol.SetDifficultyPacket;

import java.util.ArrayList;

/**
 * @author good777LUCKY
 */
public class DayLockCommand extends VanillaCommand {

    public DayLockCommand(String name) {
        super(name, "%nukkit.command.daylock.description", "%commands.daylock.usage", new String[]{"alwaysday"});
        this.setPermission("nukkit.command.daylock");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                new CommandParameter("lock", true, new String[]{"true", "false"})
        });
    }
    
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        
        if (args.length > 1) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }
        
        boolean lock = true;
        
        if (args.length == 1) {
            lock = Boolean.parseBoolean(args[0]);
        }
        
        Level level;
        if (sender instanceof Player) {
            level = ((Player) sender).getLevel();
        } else {
            level = sender.getServer().getDefaultLevel();
        }
        GameRules gameRules = level.getGameRules();
        
        if (lock) {
            gameRules.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            level.stopTime();
            level.setTime(5000);
            Command.broadcastCommandMessage(sender, new TranslationContainer("commands.always.day.locked"));
        } else {
            gameRules.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
            level.startTime();
            Command.broadcastCommandMessage(sender, new TranslationContainer("commands.always.day.unlocked"));
        }
        
        return true;
    }
}
