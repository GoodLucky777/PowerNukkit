package cn.nukkit.network.protocol.types;

import javax.annotation.Nonnull;

import lombok.ToString;

/**
 * @author GoodLucky777
 */
@ToString
public class CommandOutputMessage {

    public final boolean internal;
    public final String messageId;
    public final String[] parameters;
    
    public CommandOriginData(boolean internal, @Nonnull String messageId, @Nonnull String[] parameters) {
        this.internal = internal;
        this.messageId = messageId;
        this.parameters = parameters;
    }
}
