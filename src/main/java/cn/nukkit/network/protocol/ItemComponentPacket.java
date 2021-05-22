package cn.nukkit.network.protocol;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
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
@PowerNukkitOnly
@Since("1.4.0.0-PN")
public class ItemComponentPacket extends DataPacket {
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public static final byte NETWORK_ID = ProtocolInfo.ITEM_COMPONENT_PACKET;
    
    /*@PowerNukkitOnly
    @Since("1.4.0.0-PN")
    private Entry[] entries = Entry.EMPTY_ARRAY;*/
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public void setEntries(Entry[] entries) {
        this.entries = entries == null? null : entries.length == 0? Entry.EMPTY_ARRAY : entries.clone();
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public Entry[] getEntries() {
        return entries == null? null : entries.length == 0? Entry.EMPTY_ARRAY : entries.clone();
    }
    
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
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public static class Entry {
        @PowerNukkitOnly
        @Since("1.4.0.0-PN")
        public static final Entry[] EMPTY_ARRAY = new Entry[0];
        
        private final String name;
        private final CompoundTag data;
        
        @PowerNukkitOnly
        @Since("1.4.0.0-PN")
        public Entry(String name, CompoundTag data) {
            this.name = name;
            this.data = data;
        }
        
        @PowerNukkitOnly
        @Since("1.4.0.0-PN")
        public String getName() {
            return name;
        }
        
        @PowerNukkitOnly
        @Since("1.4.0.0-PN")
        public CompoundTag getData() {
            return data;
        }
    }

    private static CompoundTag getDefaultItemComponent(String name, int id, String icon, int maxStackSize, int creativeCategory, String creativeGroup) {
        return new CompoundTag("")
            .putString("name", name)
            .putInt("id", id)
            .putCompound("components", new CompoundTag("components")
                .putCompound("minecraft:icon", new CompoundTag("minecraft:icon")
                    .putString("texture", icon))
                .putCompound("item_properties", new CompoundTag("item_properties")
                    .putInt("max_stack_size", maxStackSize)
                    .putInt("creative_category", creativeCategory)
                    .putString("creative_group", creativeGroup)));
    }
    
    private static CompoundTag getFoodItemComponent(String name, int id, String icon, int maxStackSize, int creativeCategory, String creativeGroup, int useAnimation, int useDuration, int nutrition, boolean canAlwaysEat, float saturationModifier) {
        return new CompoundTag("")
            .putString("name", name)
            .putInt("id", id)
            .putCompound("components", new CompoundTag("components")
                .putCompound("minecraft:icon", new CompoundTag("minecraft:icon")
                    .putString("texture", icon))
                .putCompound("item_properties", new CompoundTag("item_properties")
                    .putInt("max_stack_size", maxStackSize)
                    .putInt("creative_category", creativeCategory)
                    .putString("creative_group", creativeGroup)
                    .putInt("use_animation", useAnimation)
                    .putInt("use_duration", useDuration))
                .putCompound("minecraft:food", new CompoundTag("minecraft:food")
                    .putInt("nutrition", nutrition)
                    .putBoolean("can_always_eat", canAlwaysEat))
                .putFloat("saturation_modifier", saturationModifier));
    }
    
    public static final Entry[] entries = {
        new Entry("goodlucky:ruby",
            getDefaultItemComponent("goodlucky:ruby", 5000, "goodlucky:ruby", 64, 4, "craftingScreen.tab.items")
        ),
        new Entry("goodlucky:sapphire",
            getDefaultItemComponent("goodlucky:sapphire", 5001, "goodlucky:sapphire", 64, 4, "craftingScreen.tab.items")
        ),
        new Entry("goodlucky:opal",
            getDefaultItemComponent("goodlucky:opal", 5002, "goodlucky:opal", 64, 4, "craftingScreen.tab.items")
        ),
        new Entry("goodlucky:bacon",
            getFoodItemComponent("goodlucky:bacon", 5400, "goodlucky:bacon", 64, 3, "itemGroup.name.miscFood", 1, 32, 1, false, 0.1f)
        ),
        new Entry("goodlucky:baguette", 
            getFoodItemComponent("goodlucky:baguette", 5401, "goodlucky:baguette", 64, 3, "itemGroup.name.miscFood", 1, 32, 15, false, 0.6f)
        ),
        new Entry("goodlucky:beer",
            getFoodItemComponent("goodlucky:beer", 5402, "goodlucky:beer", 64, 3, "itemGroup.name.miscFood", 1, 32, 1, true, 0.1f)
        ),
        new Entry("goodlucky:brownie",
            getFoodItemComponent("goodlucky:brownie", 5403, "goodlucky:brownie", 64, 3, "itemGroup.name.miscFood", 1, 32, 7, false, 0.6f)
        ),
        new Entry("goodlucky:cheese",
            getFoodItemComponent("goodlucky:cheese", 5404, "goodlucky:cheese", 64, 3, "itemGroup.name.miscFood", 1, 32, 2, false, 0.3f)
        ),
        new Entry("goodlucky:chocolate",
            getFoodItemComponent("goodlucky:chocolate", 5405, "goodlucky:chocolate", 64, 3, "itemGroup.name.miscFood", 1, 32, 2, false, 0.6f)
        ),
        new Entry("goodlucky:fried_chicken",
            getFoodItemComponent("goodlucky:fried_chicken", 5406, "goodlucky:fried_chicken", 64, 3, "itemGroup.name.miscFood", 1, 32, 8, false, 0.6f)
        ),
        new Entry("goodlucky:fried_chicken_leg",
            getFoodItemComponent("goodlucky:fried_chicken_leg", 5407, "goodlucky:fried_chicken_leg", 64, 3, "itemGroup.name.miscFood", 1, 32, 1, false, 0.3f)
        ),
        new Entry("goodlucky:fried_egg",
            getFoodItemComponent("goodlucky:fried_egg", 5408, "goodlucky:fried_egg", 64, 3, "itemGroup.name.miscFood", 1, 32, 1, false, 0.3f)
        ),
        new Entry("goodlucky:apple_pie",
            getFoodItemComponent("goodlucky:apple_pie", 5409, "goodlucky:apple_pie", 64, 3, "itemGroup.name.miscFood", 1, 32, 13, false, 0.6f)
        ),
        new Entry("goodlucky:pretzel",
            getFoodItemComponent("goodlucky:pretzel", 5410, "goodlucky:pretzel", 64, 3, "itemGroup.name.miscFood", 1, 32, 15, false, 0.6f)
        ),
        new Entry("goodlucky:whiskey",
            getFoodItemComponent("goodlucky:whiskey", 5411, "goodlucky:whiskey", 64, 3, "itemGroup.name.miscFood", 1, 32, 1, true, 0.1f)
        ),
        new Entry("goodlucky:wine",
            getFoodItemComponent("goodlucky:wine", 5412, "goodlucky:wine", 64, 3, "itemGroup.name.miscFood", 1, 32, 1, true, 0.1f)
        ),
        new Entry("goodlucky:ruby_sword", new CompoundTag("")
            .putString("name", "goodlucky:ruby_sword")
            .putInt("id", 6000)
            .putCompound("components", new CompoundTag("components")
                .putCompound("minecraft:icon", new CompoundTag("minecraft:icon")
                    .putString("texture", "goodlucky:ruby_sword"))
                .putCompound("item_properties", new CompoundTag("item_properties")
                    .putInt("creative_category", 3)
                    .putString("creative_group", "itemGroup.name.sword")
                    .putInt("damage", 9)
                    .putBoolean("hand_equipped", true)
                    .putInt("max_stack_size", 1))
                .putCompound("minecraft:durability", new CompoundTag("minecraft:durability")
                    .putCompound("damage_chance", new CompoundTag("damage_chance")
                        .putInt("max", 100)
                        .putInt("min", 60))
                    .putInt("max_durable", 1562))
                .putCompound("minecraft:weapon", new CompoundTag("minecraft:weapon")))
        ),
        new Entry("goodlucky:grenade_frag", new CompoundTag("")
            .putString("name", "goodlucky:grenade_frag")
            .putInt("id", 6100)
            .putCompound("components", new CompoundTag("components")
                .putCompound("minecraft:icon", new CompoundTag("minecraft:icon")
                    .putString("texture", "goodlucky:grenade_frag"))
                .putCompound("item_properties", new CompoundTag("item_properties")
                    .putInt("creative_category", 3)
                    .putString("creative_group", "craftingScreen.tab.equipment")
                    .putBoolean("hand_equipped", true)
                    .putInt("max_stack_size", 1))
                .putCompound("minecraft:projectile", new CompoundTag("minecraft:projectile")
                    .putFloat("minimum_critical_power", 0.5f)
                    .putString("projectile_entity", "goodlucky:grenade_frag<>"))
                .putCompound("minecraft:throwable", new CompoundTag("minecraft:throwable")
                    .putBoolean("do_swing_animation", true)
                    .putFloat("max_draw_duration", 2.0f)))
        )
    };
}
