package cn.nukkit.network.protocol.types;

import io.netty.util.internal.EmptyArrays;

import javax.annotation.Nonnull;

import lombok.ToString;

/**
 * @author GoodLucky777
 */
@ToString
public class CommandOutputMessage {

    public static final CommandOutputMessage[] EMPTY_ARRAY = new CommandOutputMessage[0];
    
    public final boolean internal;
    public final String messageId;
    public final String[] parameters = EmptyArrays.EMPTY_STRINGS;
    
    public CommandOutputMessage(boolean internal, @Nonnull String messageId) {
        this.internal = internal;
        this.messageId = messageId;
    }
    
    public CommandOutputMessage(boolean internal, @Nonnull String messageId, @Nonnull String[] parameters) {
        this.internal = internal;
        this.messageId = messageId;
        this.parameters = parameters;
    }
}
