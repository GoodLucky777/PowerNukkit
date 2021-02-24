package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.types.CommandOriginData;
import cn.nukkit.network.protocol.types.CommandOutputMessage;
import cn.nukkit.utils.BinaryStream;

import lombok.ToString;

import java.util.UUID;

/**
 * @author GoodLucky777
 */
@ToString
public class CommandOutputPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.COMMAND_OUTPUT_PACKET;
    
    public CommandOriginData commandOriginData;
    public CommandOutputType type;
    public int successCount;
    public CommandOutputMessage[] messages = CommandOutputMessage.EMPTY_ARRAY;
    public String data;
    
    @Override
    public byte pid() {
        return NETWORK_ID;
    }
    
    @Override
    public void decode() {
        CommandOriginData.Origin type = CommandOriginData.Origin.values()[this.getVarInt()];
        UUID uuid = this.getUUID();
        String requestId = this.getString();
        long event = -1L;
        if (type == CommandOriginData.Origin.DEV_CONSOLE || type == CommandOriginData.Origin.TEST) {
            event = this.getVarLong();
        }
        this.commandOriginData = new CommandOriginData(type, uuid, requestId, event);
        this.type = CommandOutputType.values()[this.getByte()];
        this.successCount = (int) this.getUnsignedVarInt();
        for (int i = 0; i < this.getUnsignedVarInt(); i++) {
            boolean internal = this.getBoolean();
            String messageId = this.getString();
            String[] parameters = this.getArray(String.class, BinaryStream::getString);
            this.messages[i] = new CommandOutputMessage(internal, messageId, parameters);
        }
        if (this.type == CommandOutputType.DATA_SET) {
            this.data = this.getString();
        }
    }
    
    @Override
    public void encode() {
        this.reset();
        this.putVarInt(commandOriginData.type.ordinal());
        this.putUUID(commandOriginData.uuid);
        this.putString(commandOriginData.requestId);
        if (commandOriginData.type == CommandOriginData.Origin.DEV_CONSOLE || commandOriginData.type == CommandOriginData.Origin.TEST) {
            this.putVarLong(commandOriginData.getVarLong().getAsLong());
        }
        this.putByte((byte) this.type.ordinal());
        this.putUnsignedVarInt(this.successCount);
        this.putUnsignedVarInt(this.messages.length);
        for (CommandOutputMessage commandOutputMessage : this.messages) {
            this.putBoolean(commandOutputMessage.internal);
            this.putString(commandOutputMessage.messageId);
            this.putUnsignedVarInt(commandOutputMessage.parameters.length);
            for (String parameter : commandOutputMessage.parameters) {
                this.putString(parameter);
            }
        }
        if (this.type == CommandOutputType.DATA_SET) {
            this.putString(this.data);
        }
    }
    
    public enum CommandOutputType {
        NONE,
        LAST_OUTPUT,
        SILENT,
        ALL_OUTPUT,
        DATA_SET
    }
}
