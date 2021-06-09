package cn.nukkit.network.protocol;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;

import lombok.ToString;

/**
 * @author GoodLucky777
 */
@PowerNukkitOnly
@Since("1.4.0.1-PN")
@ToString
public class TickSyncPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.TICK_SYNC_PACKET;
    
    public long requestTimestamp;
    public long responseTimestamp;
    
    @Override
    public byte pid() {
        return NETWORK_ID;
    }
    
    @Override
    public void decode() {
        this.requestTimestamp = this.getLLong();
        this.responseTimestamp = this.getLLong();
    }
    
    @Override
    public void encode() {
        this.reset();
        this.putLLong(this.requestTimestamp);
        this.putLLong(this.responseTimestamp);
    }
}
