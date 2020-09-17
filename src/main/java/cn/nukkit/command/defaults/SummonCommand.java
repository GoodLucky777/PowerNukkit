package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.TextFormat;

/**
 * @author good777LUCKY
 */
public class SummonCommand extends VanillaCommand {
    public SummonCommand(String name) {
        super(name, "%nukkit.command.summon.description", "%commands.summon.usage");
        this.setPermission("nukkit.command.summon");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
            //new CommandParameter("entityType", CommandParamType.ENTITY, false),
            new CommandParameter("spawnPos", CommandParamType.POSITION, true),
            //new CommandParameter("spawnEvent", CommandParamType.EVENT, true),
            new CommandParameter("nameTag", CommandParamType.STRING, true)
        });
        this.commandParameters.put("default", new CommandParameter[]{
            //new CommandParameter("entityType", CommandParamType.ENTITY, false),
            new CommandParameter("nameTag", CommandParamType.STRING, true),
            new CommandParameter("spawnPos", CommandParamType.POSITION, true)
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        return true;
    }
}
