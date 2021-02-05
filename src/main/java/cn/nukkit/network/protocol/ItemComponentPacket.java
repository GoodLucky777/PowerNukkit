package cn.nukkit.network.protocol;

import cn.nukkit.nbt.tag.CompoundTag;

import lombok.ToString;

/**
 * @author GoodLucky777
 */
@ToString
public class ItemComponentPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.ITEM_COMPONENT_PACKET;
    
    public Entry[] entries = Entry.EMPTY_ARRAY;
    
    @Override
    public byte pid() {
        return NETWORK_ID;
    }
    
    @Override
    public void decode() {
        
    }
    
    @Override
    public void encode() {
        this.reset();
        this.putUnsignedVarInt(this.entries.length);
        for (Entry entry : this.entries) {
            this.putString(entry.getName());
            
        }
    }
    
    @ToString
    public static class Entry {
        public static final Entry[] EMPTY_ARRAY = new Entry[0];
        
        private final String name;
        private final CompoundTag data;
        
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
