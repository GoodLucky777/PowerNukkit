package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.types.CommandOutputMessage;

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
        Long event = null;
        if (type == CommandOriginData.Origin.DEV_CONSOLE || type == CommandOriginData.Origin.TEST) {
            event = this.getVarLong();
        }
        this.commandOriginData = new CommandOriginData(type, uuid, requestId, event);
        this.type = CommandOutputType.values()[this.getUnsignedByte()];
        this.successCount = this.getUnsignedVarInt();
        for (int i = 0; i < this.getUnsignedVarInt(); i++) {
            boolean internal = this.getBoolean();
            String messageId = this.getString();
            String[] parameters = this.getArray(String.class, BinaryStream::getString);
            this.messages[i] = new CommandOutputMessage(internal, messageId, parameters);
        }
        if (type == CommandOutputType.DATA_SET) {
            this.data = this.getString();
        }
    }
    
    @Override
    public void encode() {
        this.reset();
        
    }
    
    public enum CommandOutputType {
        NONE,
        LAST_OUTPUT,
        SILENT,
        ALL_OUTPUT,
        DATA_SET
    }
}
