package cn.nukkit.network.protocol;

import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.MainLogger;

import java.io.IOException;
import java.nio.ByteOrder;

import lombok.ToString;

/**
 * @author GoodLucky777
 */
@ToString
public class ItemComponentPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.ITEM_COMPONENT_PACKET;
    
    //public Entry[] entries = Entry.EMPTY_ARRAY;
    
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
        try {
            for (Entry entry : this.entries) {
                this.putString(entry.getName());
                this.put(NBTIO.write(entry.getData(), ByteOrder.LITTLE_ENDIAN, true));
            }
        } catch (IOException e) {
            MainLogger.getLogger().error("Error while encoding NBT data of ItemComponentPacket", e);
        }
    }
    
    @ToString
    public static class Entry {
        public static final Entry[] EMPTY_ARRAY = new Entry[0];
        
        private final String name;
        private final CompoundTag data;
        
        public Entry(String name, CompoundTag data) {
            this.name = name;
            this.data = data;
        }
        
        public String getName() {
            return name;
        }
        
        public CompoundTag getData() {
            return data;
        }
    }
    
    public static final Entry[] entries = {
        new Entry("goodlucky:ruby", new CompoundTag("")
            .putString("name", "goodlucky:ruby")
            .putInt("id", 5000)
            .putCompound("components", new CompoundTag("components")
                .putCompound("minecraft:display_name", new CompoundTag("minecraft:display_name")
                    .putString("value", "item.goodlucky:ruby.name"))
                .putCompound("minecraft:icon", new CompoundTag("minecraft:icon")
                    .putString("texture", "goodlucky:ruby"))
                .putCompound("item_properties", new CompoundTag("item_properties")
                    .putInt("creative_category", 4)))
        ),
        new Entry("goodlucky:bacon", new CompoundTag("")
            .putString("name", "goodlucky:bacon")
            .putInt("id", 5400)
            .putCompound("components", new CompoundTag("components")
                .putCompound("minecraft:display_name", new CompoundTag("minecraft:display_name")
                    .putString("value", "item.goodlucky:bacon.name"))
                .putCompound("minecraft:icon", new CompoundTag("minecraft:icon")
                    .putString("texture", "goodlucky:bacon"))
                .putCompound("item_properties", new CompoundTag("item_properties")
                    .putInt("creative_category", 3))
                .putCompound("minecraft:food", new CompoundTag("minecraft:food")
                    .putInt("nutrition", 1)
                    .putString("saturation_modifier", "low"))
                .putCompound("minecraft:use_duration", new CompoundTag("minecraft:use_duration")
                    .putInt("value", 1)))
        ),
        new Entry("goodlucky:ruby_sword", new CompoundTag("")
            .putString("name", "goodlucky:ruby_sword")
            .putInt("id", 6000)
            .putCompound("components", new CompoundTag("components")
                .putCompound("minecraft:display_name", new CompoundTag("minecraft:display_name")
                    .putString("value", "item.goodlucky:ruby_sword.name"))
                .putCompound("minecraft:icon", new CompoundTag("minecraft:icon")
                    .putString("texture", "goodlucky:ruby_sword"))
                .putCompound("item_properties", new CompoundTag("item_properties")
                    .putInt("creative_category", 3)
                    .putInt("damage", 9)
                    .putBoolean("hand_equipped", true)
                    .putInt("max_stack_size", 1))
                .putCompound("minecraft:durability", new CompoundTag("minecraft:durability")
                    .putInt("damage_change", 1)
                    .putInt("max_durable", 1562)))
        )
    };
}
