package cn.nukkit.network.protocol;

import cn.nukkit.nbt.tag.CompoundTag;

import lombok.ToString;

/**
 * @author GoodLucky777
 */
@ToString
public class ItemComponentPacket extends DataPacket {

    @Override
    public byte pid() {
        return ProtocolInfo.ITEM_COMPONENT_PACKET;
    }
    
    @Override
    public void decode() {
        
    }
    
    @Override
    public void encode() {
        this.reset();
        this.putVarInt(0); // TODO
    }
    
    @ToString
    public static class Entry {
        public static final Entry[] EMPTY_ARRAY = new Entry[0];
        
        public final String name;
        public final CompoundTag data;
        
        public Entry(String name, CompoundTag data) {
            this(name, data);
        }
        
        public String getName() {
            return name;
        }
        
        public CompoundTag getData() {
            return data;
        }
    }
}
