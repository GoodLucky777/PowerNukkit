package cn.nukkit.network.protocol;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.inventory.stack.response.ItemStackResponse;
import cn.nukkit.inventory.stack.response.StackResponseContainerInfo;
import cn.nukkit.inventory.stack.response.StackResponseSlotInfo;

import java.util.ArrayList;
import java.util.List;

import lombok.ToString;

/**
 * @author joserobjr
 * @author GoodLucky777
 */
@PowerNukkitOnly
@Since("1.4.0.0-PN")
@ToString
public class ItemStackResponsePacket extends DataPacket {

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public static final byte NETWORK_ID = ProtocolInfo.ITEM_STACK_RESPONSE_PACKET;
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public List<ItemStackResponse> responses = new ArrayList<>();
    
    @Override
    public void encode() {
        this.reset();
        this.putUnsignedVarInt(responses.size());
        for (ItemStackResponse response : this.responses) {
            this.putByte(response.result.ordinal());
            this.putVarInt(response.requestId);
            this.putUnsignedVarInt(response.containers.size());
            for (StackResponseContainerInfo container : response.containers) {
                this.putByte(container.container.ordinal());
                this.putUnsignedVarInt(container.slots.size());
                for (StackResponseSlotInfo slot : container.slots) {
                    
                }
            }
        }
    }

    @Override
    public void decode() {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}
