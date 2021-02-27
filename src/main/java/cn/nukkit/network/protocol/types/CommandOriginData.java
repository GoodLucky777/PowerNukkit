package cn.nukkit.network.protocol.types;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;

import lombok.ToString;

import java.util.UUID;

/**
 * @author SupremeMortal (Nukkit project)
 */
@ToString
public final class CommandOriginData {

    public static final CommandOriginData DEFAULT = new CommandOriginData(Origin.PLAYER, UUID.fromString("00000000-0000-0000-0000-000000000000"), "", -1L);
    
    public final Origin type;
    public final UUID uuid;
    public final String requestId;
    public final long event;
    
    public CommandOriginData(Origin type, UUID uuid, String requestId) {
        this(type, uuid, requestId, -1L);
    }
    
    public CommandOriginData(Origin type, UUID uuid, String requestId, long event) {
        this.type = type;
        this.uuid = uuid;
        this.requestId = requestId;
        this.event = event;
    }

    public enum Origin {
        PLAYER,
        BLOCK,
        MINECART_BLOCK,
        DEV_CONSOLE,
        TEST,
        AUTOMATION_PLAYER,
        CLIENT_AUTOMATION,
        DEDICATED_SERVER,
        ENTITY,
        VIRTUAL,
        GAME_ARGUMENT,
        ENTITY_SERVER,
        @PowerNukkitOnly @Since("1.4.0.0-PN") PRECOMPILED,
        @PowerNukkitOnly @Since("1.4.0.0-PN") GAME_MASTER_ENTITY_SERVER,
        @PowerNukkitOnly @Since("1.4.0.0-PN") SCRIPT
    }
    
    public CommandOriginData createCommandBlockOriginData() {
        return new CommandOriginData(Origin.BLOCK, UUID.randomUUID(), "", -1L); // TODO: Is this correct?
    }
    
    public CommandOriginData createCommandBlockMinecartOriginData() {
        return new CommandOriginData(Origin.MINECART_BLOCK, UUID.randomUUID(), "", -1L); // TODO: Is this correct?
    }
    
    public CommandOriginData createConsoleOriginData() {
        return new CommandOriginData(Origin.DEDICATED_SERVER, UUID.randomUUID(), "", -1L); // TODO: Is this correct?
    }
    
    public CommandOriginData createPlayerOriginData() {
        return new CommandOriginData(Origin.PLAYER, UUID.randomUUID(), "", -1L);
    }
}
