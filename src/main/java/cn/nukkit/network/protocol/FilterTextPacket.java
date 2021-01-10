package cn.nukkit.network.protocol;

import cn.nukkit.api.Since;
import lombok.ToString;

@Since("1.4.0.0-PN")
@ToString
public class FilterTextPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.FILTER_TEXT_PACKET;

    @Since("1.4.0.0-PN") public String text;
    @Since("1.4.0.0-PN") public boolean fromServer;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.text = this.getString();
        this.fromServer = this.getBoolean();
    }

    @Override
    public void encode() {
        this.reset();
        this.putString(this.text);
        this.putBoolean(this.fromServer);
    }
}
