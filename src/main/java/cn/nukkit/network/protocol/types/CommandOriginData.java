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
}
