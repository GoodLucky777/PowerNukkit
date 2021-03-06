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
                    .putInt("creative_category", 4)
                    .putInt("max_stack_size", 64)))
        ),
        new Entry("goodlucky:sapphire", new CompoundTag("")
            .putString("name", "goodlucky:sapphire")
            .putInt("id", 5001)
            .putCompound("components", new CompoundTag("components")
                .putCompound("minecraft:display_name", new CompoundTag("minecraft:display_name")
                    .putString("value", "item.goodlucky:sapphire.name"))
                .putCompound("minecraft:icon", new CompoundTag("minecraft:icon")
                    .putString("texture", "goodlucky:sapphire"))
                .putCompound("item_properties", new CompoundTag("item_properties")
                    .putInt("creative_category", 4)
                    .putInt("max_stack_size", 64)))
        ),
        new Entry("goodlucky:opal", new CompoundTag("")
            .putString("name", "goodlucky:opal")
            .putInt("id", 5002)
            .putCompound("components", new CompoundTag("components")
                .putCompound("minecraft:display_name", new CompoundTag("minecraft:display_name")
                    .putString("value", "item.goodlucky:opal.name"))
                .putCompound("minecraft:icon", new CompoundTag("minecraft:icon")
                    .putString("texture", "goodlucky:opal"))
                .putCompound("item_properties", new CompoundTag("item_properties")
                    .putInt("creative_category", 4)
                    .putInt("max_stack_size", 64)))
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
                    .putInt("creative_category", 3)
                    .putInt("max_stack_size", 64)
                    .putInt("use_duration", 32))
                .putCompound("minecraft:food", new CompoundTag("minecraft:food")
                    .putFloat("nutrition", 1.0f)
                    .putString("saturation_modifier", "low")
                    .putBoolean("can_always_eat", false))
                .putInt("use_animation", 1)
                .putCompound("minecraft:use_duration", new CompoundTag("minecraft:use_duration")
                    .putInt("value", 1)))
        ),
        new Entry("goodlucky:baguette", new CompoundTag("")
            .putString("name", "goodlucky:baguette")
            .putInt("id", 5401)
            .putCompound("components", new CompoundTag("components")
                .putCompound("minecraft:display_name", new CompoundTag("minecraft:display_name")
                    .putString("value", "item.goodlucky:baguette.name"))
                .putCompound("minecraft:icon", new CompoundTag("minecraft:icon")
                    .putString("texture", "goodlucky:baguette"))
                .putCompound("item_properties", new CompoundTag("item_properties")
                    .putInt("creative_category", 3)
                    .putInt("max_stack_size", 64)
                    .putInt("use_duration", 32))
                .putCompound("minecraft:food", new CompoundTag("minecraft:food")
                    .putFloat("nutrition", 1.0f)
                    .putString("saturation_modifier", "low")
                    .putBoolean("can_always_eat", false))
                .putInt("use_animation", 1)
                .putCompound("minecraft:use_duration", new CompoundTag("minecraft:use_duration")
                    .putInt("value", 1)))
        ),
        new Entry("goodlucky:beer", new CompoundTag("")
            .putString("name", "goodlucky:beer")
            .putInt("id", 5402)
            .putCompound("components", new CompoundTag("components")
                .putCompound("minecraft:display_name", new CompoundTag("minecraft:display_name")
                    .putString("value", "item.goodlucky:beer.name"))
                .putCompound("minecraft:icon", new CompoundTag("minecraft:icon")
                    .putString("texture", "goodlucky:beer"))
                .putCompound("item_properties", new CompoundTag("item_properties")
                    .putInt("creative_category", 3)
                    .putInt("max_stack_size", 64)
                    .putInt("use_duration", 32))
                .putCompound("minecraft:food", new CompoundTag("minecraft:food")
                    .putFloat("nutrition", 1.0f)
                    .putString("saturation_modifier", "low")
                    .putBoolean("can_always_eat", true))
                .putInt("use_animation", 1)
                .putCompound("minecraft:use_duration", new CompoundTag("minecraft:use_duration")
                    .putInt("value", 1)))
        ),
        new Entry("goodlucky:brownie", new CompoundTag("")
            .putString("name", "goodlucky:brownie")
            .putInt("id", 5403)
            .putCompound("components", new CompoundTag("components")
                .putCompound("minecraft:display_name", new CompoundTag("minecraft:display_name")
                    .putString("value", "item.goodlucky:brownie.name"))
                .putCompound("minecraft:icon", new CompoundTag("minecraft:icon")
                    .putString("texture", "goodlucky:brownie"))
                .putCompound("item_properties", new CompoundTag("item_properties")
                    .putInt("creative_category", 3)
                    .putInt("max_stack_size", 64)
                    .putInt("use_duration", 32))
                .putCompound("minecraft:food", new CompoundTag("minecraft:food")
                    .putFloat("nutrition", 1.0f)
                    .putString("saturation_modifier", "low")
                    .putBoolean("can_always_eat", false))
                .putInt("use_animation", 1)
                .putCompound("minecraft:use_duration", new CompoundTag("minecraft:use_duration")
                    .putInt("value", 1)))
        ),
        new Entry("goodlucky:cheese", new CompoundTag("")
            .putString("name", "goodlucky:cheese")
            .putInt("id", 5404)
            .putCompound("components", new CompoundTag("components")
                .putCompound("minecraft:display_name", new CompoundTag("minecraft:display_name")
                    .putString("value", "item.goodlucky:cheese.name"))
                .putCompound("minecraft:icon", new CompoundTag("minecraft:icon")
                    .putString("texture", "goodlucky:cheese"))
                .putCompound("item_properties", new CompoundTag("item_properties")
                    .putInt("creative_category", 3)
                    .putInt("max_stack_size", 64)
                    .putInt("use_duration", 32))
                .putCompound("minecraft:food", new CompoundTag("minecraft:food")
                    .putFloat("nutrition", 1.0f)
                    .putString("saturation_modifier", "low")
                    .putBoolean("can_always_eat", false))
                .putInt("use_animation", 1)
                .putCompound("minecraft:use_duration", new CompoundTag("minecraft:use_duration")
                    .putInt("value", 1)))
        ),
        new Entry("goodlucky:chocolate", new CompoundTag("")
            .putString("name", "goodlucky:chocolate")
            .putInt("id", 5405)
            .putCompound("components", new CompoundTag("components")
                .putCompound("minecraft:display_name", new CompoundTag("minecraft:display_name")
                    .putString("value", "item.goodlucky:chocolate.name"))
                .putCompound("minecraft:icon", new CompoundTag("minecraft:icon")
                    .putString("texture", "goodlucky:chocolate"))
                .putCompound("item_properties", new CompoundTag("item_properties")
                    .putInt("creative_category", 3)
                    .putInt("max_stack_size", 64)
                    .putInt("use_duration", 32))
                .putCompound("minecraft:food", new CompoundTag("minecraft:food")
                    .putFloat("nutrition", 1.0f)
                    .putString("saturation_modifier", "low")
                    .putBoolean("can_always_eat", false))
                .putInt("use_animation", 1)
                .putCompound("minecraft:use_duration", new CompoundTag("minecraft:use_duration")
                    .putInt("value", 1)))
        ),
        new Entry("goodlucky:fried_chicken", new CompoundTag("")
            .putString("name", "goodlucky:fried_chicken")
            .putInt("id", 5406)
            .putCompound("components", new CompoundTag("components")
                .putCompound("minecraft:display_name", new CompoundTag("minecraft:display_name")
                    .putString("value", "item.goodlucky:fried_chicken.name"))
                .putCompound("minecraft:icon", new CompoundTag("minecraft:icon")
                    .putString("texture", "goodlucky:fried_chicken"))
                .putCompound("item_properties", new CompoundTag("item_properties")
                    .putInt("creative_category", 3)
                    .putInt("max_stack_size", 64)
                    .putInt("use_duration", 32))
                .putCompound("minecraft:food", new CompoundTag("minecraft:food")
                    .putFloat("nutrition", 1.0f)
                    .putString("saturation_modifier", "low")
                    .putBoolean("can_always_eat", false))
                .putInt("use_animation", 1)
                .putCompound("minecraft:use_duration", new CompoundTag("minecraft:use_duration")
                    .putInt("value", 1)))
        ),
        new Entry("goodlucky:fried_chicken_leg", new CompoundTag("")
            .putString("name", "goodlucky:fried_chicken_leg")
            .putInt("id", 5407)
            .putCompound("components", new CompoundTag("components")
                .putCompound("minecraft:display_name", new CompoundTag("minecraft:display_name")
                    .putString("value", "item.goodlucky:fried_chicken_leg.name"))
                .putCompound("minecraft:icon", new CompoundTag("minecraft:icon")
                    .putString("texture", "goodlucky:fried_chicken_leg"))
                .putCompound("item_properties", new CompoundTag("item_properties")
                    .putInt("creative_category", 3)
                    .putInt("max_stack_size", 64)
                    .putInt("use_duration", 32))
                .putCompound("minecraft:food", new CompoundTag("minecraft:food")
                    .putFloat("nutrition", 1.0f)
                    .putString("saturation_modifier", "low")
                    .putBoolean("can_always_eat", false))
                .putInt("use_animation", 1)
                .putCompound("minecraft:use_duration", new CompoundTag("minecraft:use_duration")
                    .putInt("value", 1)))
        ),
        new Entry("goodlucky:fried_egg", new CompoundTag("")
            .putString("name", "goodlucky:fried_egg")
            .putInt("id", 5408)
            .putCompound("components", new CompoundTag("components")
                .putCompound("minecraft:display_name", new CompoundTag("minecraft:display_name")
                    .putString("value", "item.goodlucky:fried_egg.name"))
                .putCompound("minecraft:icon", new CompoundTag("minecraft:icon")
                    .putString("texture", "goodlucky:fried_egg"))
                .putCompound("item_properties", new CompoundTag("item_properties")
                    .putInt("creative_category", 3)
                    .putInt("max_stack_size", 64)
                    .putInt("use_duration", 32))
                .putCompound("minecraft:food", new CompoundTag("minecraft:food")
                    .putFloat("nutrition", 1.0f)
                    .putString("saturation_modifier", "low")
                    .putBoolean("can_always_eat", false))
                .putInt("use_animation", 1)
                .putCompound("minecraft:use_duration", new CompoundTag("minecraft:use_duration")
                    .putInt("value", 1)))
        ),
        new Entry("goodlucky:apple_pie", new CompoundTag("")
            .putString("name", "goodlucky:apple_pie")
            .putInt("id", 5409)
            .putCompound("components", new CompoundTag("components")
                .putCompound("minecraft:display_name", new CompoundTag("minecraft:display_name")
                    .putString("value", "item.goodlucky:apple_pie.name"))
                .putCompound("minecraft:icon", new CompoundTag("minecraft:icon")
                    .putString("texture", "goodlucky:apple_pie"))
                .putCompound("item_properties", new CompoundTag("item_properties")
                    .putInt("creative_category", 3)
                    .putInt("max_stack_size", 64)
                    .putInt("use_duration", 32))
                .putCompound("minecraft:food", new CompoundTag("minecraft:food")
                    .putFloat("nutrition", 1.0f)
                    .putString("saturation_modifier", "low")
                    .putBoolean("can_always_eat", false))
                .putInt("use_animation", 1)
                .putCompound("minecraft:use_duration", new CompoundTag("minecraft:use_duration")
                    .putInt("value", 1)))
        ),
        new Entry("goodlucky:pretzel", new CompoundTag("")
            .putString("name", "goodlucky:pretzel")
            .putInt("id", 5410)
            .putCompound("components", new CompoundTag("components")
                .putCompound("minecraft:display_name", new CompoundTag("minecraft:display_name")
                    .putString("value", "item.goodlucky:pretzel.name"))
                .putCompound("minecraft:icon", new CompoundTag("minecraft:icon")
                    .putString("texture", "goodlucky:pretzel"))
                .putCompound("item_properties", new CompoundTag("item_properties")
                    .putInt("creative_category", 3)
                    .putInt("max_stack_size", 64)
                    .putInt("use_duration", 32))
                .putCompound("minecraft:food", new CompoundTag("minecraft:food")
                    .putFloat("nutrition", 1.0f)
                    .putString("saturation_modifier", "low")
                    .putBoolean("can_always_eat", false))
                .putInt("use_animation", 1)
                .putCompound("minecraft:use_duration", new CompoundTag("minecraft:use_duration")
                    .putInt("value", 1)))
        ),
        new Entry("goodlucky:whiskey", new CompoundTag("")
            .putString("name", "goodlucky:whiskey")
            .putInt("id", 5411)
            .putCompound("components", new CompoundTag("components")
                .putCompound("minecraft:display_name", new CompoundTag("minecraft:display_name")
                    .putString("value", "item.goodlucky:whiskey.name"))
                .putCompound("minecraft:icon", new CompoundTag("minecraft:icon")
                    .putString("texture", "goodlucky:whiskey"))
                .putCompound("item_properties", new CompoundTag("item_properties")
                    .putInt("creative_category", 3)
                    .putInt("max_stack_size", 64)
                    .putInt("use_duration", 32))
                .putCompound("minecraft:food", new CompoundTag("minecraft:food")
                    .putFloat("nutrition", 1.0f)
                    .putString("saturation_modifier", "low")
                    .putBoolean("can_always_eat", true))
                .putInt("use_animation", 1)
                .putCompound("minecraft:use_duration", new CompoundTag("minecraft:use_duration")
                    .putInt("value", 1)))
        ),
        new Entry("goodlucky:wine", new CompoundTag("")
            .putString("name", "goodlucky:wine")
            .putInt("id", 5412)
            .putCompound("components", new CompoundTag("components")
                .putCompound("minecraft:display_name", new CompoundTag("minecraft:display_name")
                    .putString("value", "item.goodlucky:wine.name"))
                .putCompound("minecraft:icon", new CompoundTag("minecraft:icon")
                    .putString("texture", "goodlucky:wine"))
                .putCompound("item_properties", new CompoundTag("item_properties")
                    .putBoolean("animates_in_toolbar", true)
                    .putInt("creative_category", 3)
                    .putInt("max_stack_size", 64)
                    .putInt("use_duration", 32))
                .putCompound("minecraft:food", new CompoundTag("minecraft:food")
                    .putFloat("nutrition", 1.0f)
                    .putString("saturation_modifier", "low")
                    .putBoolean("can_always_eat", true))
                .putInt("use_animation", 1))
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
                    .putInt("max_durable", 1562))
                .putCompound("minecraft:weapon", new CompoundTag("minecraft:weapon")))
        )
    };
}
