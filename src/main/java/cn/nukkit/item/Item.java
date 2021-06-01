package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.api.*;
import cn.nukkit.api.Since;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.blockproperty.UnknownRuntimeIdException;
import cn.nukkit.blockproperty.exception.InvalidBlockPropertyMetaException;
import cn.nukkit.blockstate.BlockState;
import cn.nukkit.blockstate.BlockStateRegistry;
import cn.nukkit.blockstate.exception.InvalidBlockStateException;
import cn.nukkit.entity.Entity;
import cn.nukkit.inventory.Fuel;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.*;
import cn.nukkit.utils.Binary;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.Identifier;
import cn.nukkit.utils.Utils;
import io.netty.util.internal.EmptyArrays;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Modifier;
import java.nio.ByteOrder;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author MagicDroidX (Nukkit Project)
 */
@Log4j2
public class Item implements Cloneable, BlockID, ItemID {

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public static final Item[] EMPTY_ARRAY = new Item[0];
    
    /**
     * Groups:
     * <ol>
     *     <li>namespace (optional)</li>
     *     <li>item name (choice)</li>
     *     <li>damage (optional, for item name)</li>
     *     <li>numeric id (choice)</li>
     *     <li>damage (optional, for numeric id)</li>
     * </ol>
     */
    private static final Pattern ITEM_STRING_PATTERN = Pattern.compile(
            //       1:namespace    2:name           3:damage   4:num-id    5:damage
            "^(?:(?:([a-z_]\\w*):)?([a-z._]\\w*)(?::(-?\\d+))?|(-?\\d+)(?::(-?\\d+))?)$");

    protected static String UNKNOWN_STR = "Unknown";
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    private static ItemRegistry itemRegistry;
    
    @Deprecated
    public static Class[] list = null;
    
    private static Map<String, Integer> itemIds = Arrays.stream(ItemID.class.getDeclaredFields())
            .filter(field-> field.getModifiers() == (Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL))
            .filter(field -> field.getType().equals(int.class))
            .collect(Collectors.toMap(
                    field -> field.getName().toLowerCase(),
                    field -> {
                        try {
                            return field.getInt(null);
                        } catch (IllegalAccessException e) {
                            throw new InternalError(e);
                        }
                    },
                    (e1, e2) -> e1, LinkedHashMap::new
            ));

    private static Map<String, Integer> blockIds = Arrays.stream(BlockID.class.getDeclaredFields())
            .filter(field-> field.getModifiers() == (Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL))
            .filter(field -> field.getType().equals(int.class))
            .collect(Collectors.toMap(
                    field -> field.getName().toLowerCase(),
                    field -> {
                        try {
                            int blockId = field.getInt(null);
                            if (blockId > 255) {
                                return 255 - blockId;
                            }
                            return blockId;
                        } catch (IllegalAccessException e) {
                            throw new InternalError(e);
                        }
                    },
                    (e1, e2) -> e1, LinkedHashMap::new
            ));
    
    @Deprecated
    public Item(int id) {
        this(id, 0, 1, UNKNOWN_STR);
    }
    
    @Deprecated
    public Item(int id, Integer meta) {
        this(id, meta, 1, UNKNOWN_STR);
    }
    
    @Deprecated
    public Item(int id, Integer meta, int count) {
        this(id, meta, count, UNKNOWN_STR);
    }
    
    @Deprecated
    public Item(int id, Integer meta, int count, String name) {
        this(this.getRegistry().getIdentifierFromLegacyId(id), meta, count, name);
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public Item(Identifier identifier) {
        this(identifier, 0);
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public Item(Identifier identifier, Integer meta) {
        this(identifier, meta, 1);
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public Item(Identifier identifier, Integer meta, int count) {
        this(identifier, meta, count, UNKNOWN_STR);
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public Item(Identifier identifier, Integer meta, int count, String name) {
        this.identifier = identifier;
        if (meta != null && meta >= 0) {
            this.meta = meta & 0xffff;
        } else {
            this.hasMeta = false;
        }
        this.count = count;
        this.name = name != null ? name.intern() : null;
        /*if (this.block != null && this.id <= 0xff && Block.list[id] != null) { //probably useless
            this.block = Block.get(this.id, this.meta);
            this.name = this.block.getName();
        }*/
    }
    
    @Deprecated
    public static Item get(int id) {
        return get(id, 0);
    }
    
    @Deprecated
    public static Item get(int id, Integer meta) {
        return get(id, meta, 1);
    }
    
    @Deprecated
    public static Item get(int id, Integer meta, int count) {
        return get(id, meta, count, EmptyArrays.EMPTY_BYTES);
    }
    
    @Deprecated
    @PowerNukkitDifference(
            info = "Prevents players from getting invalid items by limiting the return to the maximum damage defined in Block.getMaxItemDamage()",
            since = "1.4.0.0-PN")
    public static Item get(int id, Integer meta, int count, byte[] tags) {
        return get(this.getRegistry().getIdentifierFromLegacyId(id), meta, count, tags);
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public static Item get(Identifier identifier) {
        return get(id, 0);
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public static Item get(Identifier identifier, Integer meta) {
        return get(id, meta, 1);
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public static Item get(Identifier identifier, Integer meta, int count) {
        return get(identifier, meta, count, EmptyArrays.EMPTY_BYTES);
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public static Item get(Identifier identifier, Integer meta, int count, byte[] tags) {
        Item item = new Item(identifier, meta, count);
        
        if (tags.length != 0) {
            item.setCompoundTag(tags);
        }
        
        return item;
        
        /*try {
            Class c = null;
            if (id < 0) {
                int blockId = 255 - id;
                c = Block.list[blockId];
            } else {
                c = list[id];
            }
            
            Item item;
            
            // Item Block
            if (id < 256) {
                int blockId = id < 0 ? 255 - id : id;
                if (meta == 0) {
                    item = new ItemBlock(Block.get(blockId), 0, count);
                } else if (meta == -1) {
                    // Special case for item instances used in fuzzy recipes
                    item = new ItemBlock(Block.get(blockId), -1);
                } else {
                    BlockState state = BlockState.of(blockId, meta);
                    try {
                        state.validate();
                        item = state.asItemBlock(count);
                    } catch (InvalidBlockPropertyMetaException | InvalidBlockStateException e) {
                        log.warn("Attempted to get an ItemBlock with invalid block state in memory: {}, trying to repair the block state...", state);
                        log.catching(org.apache.logging.log4j.Level.DEBUG, e);
                        Block repaired = state.getBlockRepairing(null, 0, 0, 0);
                        item = repaired.asItemBlock(count);
                        log.error("Attempted to get an illegal item block {}:{} ({}), the meta was changed to {}",
                                id, meta, blockId, item.getDamage(), e);
                    } catch (UnknownRuntimeIdException e) {
                        log.warn("Attempted to get an illegal item block {}:{} ({}), the runtime id was unknown and the meta was changed to 0",
                                id, meta, blockId, e);
                        item = BlockState.of(id).asItemBlock(count);
                    }
                }
            } else if (c == null) { // Normal Item
                item = new Item(identifier, meta, count);
            } else {
                if (meta == -1) {
                    item = ((Item) c.getConstructor(Integer.class, int.class).newInstance(0, count)).createFuzzyCraftingRecipe();
                } else {
                    item = ((Item) c.getConstructor(Integer.class, int.class).newInstance(meta, count));
                }
            }

            if (tags.length != 0) {
                item.setCompoundTag(tags);
            }
            
            return item;
        } catch (Exception e) {
            log.error("Error getting the item {}:{}! Returning an unsafe item stack!", identifier.getFullString(), meta, e);
            return new Item(identifier, meta, count).setCompoundTag(tags);
        }*/
    }
    
    @Deprecated
    public static Item getBlock(int id) {
        return getBlock(id, 0);
    }
    
    @Deprecated
    public static Item getBlock(int id, Integer meta) {
        return getBlock(id, meta, 1);
    }
    
    @Deprecated
    public static Item getBlock(int id, Integer meta, int count) {
        return getBlock(id, meta, count, EmptyArrays.EMPTY_BYTES);
    }
    
    @Deprecated
    public static Item getBlock(int id, Integer meta, int count, byte[] tags) {
        if (id > 255) {
            id = 255 - id;
        }
        return get(id, meta, count, tags);
    }
    
    @PowerNukkitDifference(since = "1.4.0.0-PN", info = "Improve namespaced name handling and allows to get custom blocks by name")
    public static Item fromString(String str) {
        String normalized = str.trim().replace(' ', '_').toLowerCase();
        Matcher matcher = ITEM_STRING_PATTERN.matcher(normalized);
        if (!matcher.matches()) {
            return get(AIR);
        }
        
        String name = matcher.group(2);
        OptionalInt meta = OptionalInt.empty();
        String metaGroup;
        if (name != null) {
            metaGroup = matcher.group(3);
        } else {
            metaGroup = matcher.group(5);
        }
        if (metaGroup != null) {
            meta = OptionalInt.of(Short.parseShort(metaGroup));
        }

        String numericIdGroup = matcher.group(4);
        if (name != null) {
            String namespaceGroup = matcher.group(1);
            String namespacedId;
            if (namespaceGroup != null) {
                namespacedId = namespaceGroup + ":" + name;
            } else {
                namespacedId = "minecraft:" + name;
            }
            MinecraftItemID minecraftItemId = MinecraftItemID.getByNamespaceId(namespacedId);
            if (minecraftItemId != null) {
                Item item = minecraftItemId.get(1);
                if (meta.isPresent()) {
                    int damage = meta.getAsInt();
                    if (damage < 0) {
                        item = item.createFuzzyCraftingRecipe();
                    } else {
                        item.setDamage(damage);
                    }
                }
                return item;
            } else if (namespaceGroup != null && !namespaceGroup.equals("minecraft:")) {
                return get(AIR);
            }
        } else if (numericIdGroup != null) {
            int id = Integer.parseInt(numericIdGroup);
            return get(id, meta.orElse(0));
        }
        
        if (name == null) {
            return get(AIR);
        }

        int id = 0;

        try {
            id = ItemID.class.getField(name.toUpperCase()).getInt(null);
        } catch (Exception ignore1) {
            try {
                id = BlockID.class.getField(name.toUpperCase()).getInt(null);
                if (id > 255) {
                    id = 255 - id;
                }
            } catch (Exception ignore2) {

            }
        }

        return get(id, meta.orElse(0));
    }

    public static Item fromJson(Map<String, Object> data) {
        return fromJson(data, false);
    }

    private static Item fromJson(Map<String, Object> data, boolean ignoreNegativeItemId) {
        String nbt = (String) data.get("nbt_b64");
        byte[] nbtBytes;
        if (nbt != null) {
            nbtBytes = Base64.getDecoder().decode(nbt);
        } else { // Support old format for backwards compat
            nbt = (String) data.getOrDefault("nbt_hex", null);
            if (nbt == null) {
                nbtBytes = EmptyArrays.EMPTY_BYTES;
            } else {
                nbtBytes = Utils.parseHexBinary(nbt);
            }
        }

        int id = Utils.toInt(data.get("id"));
        if (ignoreNegativeItemId && id < 0) return null;

        return get(id, Utils.toInt(data.getOrDefault("damage", 0)), Utils.toInt(data.getOrDefault("count", 1)), nbtBytes);
    }

    private static Item fromJsonStringId(Map<String, Object> data) {
        String nbt = (String) data.get("nbt_b64");
        byte[] nbtBytes = nbt != null ? Base64.getDecoder().decode(nbt) : EmptyArrays.EMPTY_BYTES;

        String id = data.get("id").toString();
        Item item;
        if (data.containsKey("damage")) {
            int meta = Utils.toInt(data.get("damage"));
            item = fromString(id+":"+meta);
        } else {
            item = fromString(id);
        }
        item.setCompoundTag(nbtBytes);
        return item;
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public static Item fromJsonNetworkId(Map<String, Object> data) {
        String nbt = (String) data.get("nbt_b64");
        byte[] nbtBytes;
        if (nbt != null) {
            nbtBytes = Base64.getDecoder().decode(nbt);
        } else { // Support old format for backwards compat
            nbt = (String) data.getOrDefault("nbt_hex", null);
            if (nbt == null) {
                nbtBytes = EmptyArrays.EMPTY_BYTES;
            } else {
                nbtBytes = Utils.parseHexBinary(nbt);
            }
        }

        int networkId = Utils.toInt(data.get("id"));
        RuntimeItemMapping mapping = RuntimeItems.getRuntimeMapping();
        int legacyFullId = mapping.getLegacyFullId(networkId);
        int id = RuntimeItems.getId(legacyFullId);
        OptionalInt meta = RuntimeItems.hasData(legacyFullId)? OptionalInt.of(RuntimeItems.getData(legacyFullId)) : OptionalInt.empty();
        if (data.containsKey("damage")) {
            int jsonMeta = Utils.toInt(data.get("damage"));
            if (jsonMeta != Short.MAX_VALUE) {
                if (meta.isPresent() && jsonMeta != meta.getAsInt()) {
                    throw new IllegalArgumentException(
                            "Conflicting damage value for " + mapping.getNamespacedIdByNetworkId(networkId) + ". " +
                                    "From json: " + jsonMeta + ", from mapping: " + meta.getAsInt()
                    );
                }
                meta = OptionalInt.of(jsonMeta);
            } else if (!meta.isPresent()) {
                meta = OptionalInt.of(-1);
            }
        }
        return get(id, meta.orElse(0), Utils.toInt(data.getOrDefault("count", 1)), nbtBytes);
    }

    public static Item[] fromStringMultiple(String str) {
        String[] b = str.split(",");
        Item[] items = new Item[b.length - 1];
        for (int i = 0; i < b.length; i++) {
            items[i] = fromString(b[i]);
        }
        return items;
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public static ItemRegistry getRegistry() {
        return itemRegistry;
    }
    
    public static void init() {
        itemRegistry = new ItemRegistry();
    }

    private static List<String> itemList;

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public static List<String> rebuildItemList() {
        return itemList = Collections.unmodifiableList(Stream.of(
                BlockStateRegistry.getPersistenceNames().stream()
                        .map(name-> name.substring(name.indexOf(':') + 1)),
                itemIds.keySet().stream()
        ).flatMap(Function.identity()).distinct().collect(Collectors.toList()));
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public static List<String> getItemList() {
        List<String> itemList = Item.itemList;
        if (itemList == null) {
            return rebuildItemList();
        }
        return itemList;
    }
    
    @Deprecated
    private static final ArrayList<Item> creative = new ArrayList<>();
    
    @Deprecated
    //@SuppressWarnings("unchecked")
    private static void initCreativeItems() {
        this.getRegistry().clearCreativeItems();
        this.getRegistry().loadCreativeItems();
    }
    
    @Deprecated
    public static void clearCreativeItems() {
        this.getRegistry().clearCreativeItems();
    }
    
    @Deprecated
    public static ArrayList<Item> getCreativeItems() {
        return this.getRegistry().getCreativeItems();
    }
    
    @Deprecated
    public static void addCreativeItem(Item item) {
        this.getRegistry().registerCreativeItem(item);
    }
    
    @Deprecated
    public static void removeCreativeItem(Item item) {
        this.getRegistry().unregisterCreativeItem(item);
    }
    
    @Deprecated
    public static boolean isCreativeItem(Item item) {
        return this.getRegistry().isCreativeItem(item);
    }
    
    @Deprecated
    public static Item getCreativeItem(int index) {
        return this.getRegistry().getCreativeItem(index);
    }
    
    @Deprecated
    public static int getCreativeItemIndex(Item item) {
        return this.getRegistry().getCreativeItemIndex(item);
    }
    
    // Item data
    protected final Identifier id;
    protected Block block = null;
    protected boolean hasMeta = true;
    protected int meta;
    public int count;
    private byte[] tags = EmptyArrays.EMPTY_BYTES;
    private transient CompoundTag cachedNBT = null;
    protected String name;
    
    @Deprecated
    @DeprecationDetails(since = "1.4.0.0-PN", by = "PowerNukkit", reason = "Unused", replaceWith = "meta or getDamage()")
    protected int durability = 0;
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public Identifier getIdentifier() {
        return identifier;
    }
    
    public boolean hasMeta() {
        return hasMeta;
    }
    
    public Item setCompoundTag(CompoundTag tag) {
        this.setNamedTag(tag);
        return this;
    }

    public Item setCompoundTag(byte[] tags) {
        this.tags = tags;
        this.cachedNBT = null;
        return this;
    }

    public byte[] getCompoundTag() {
        return tags;
    }

    public boolean hasCompoundTag() {
        return this.tags != null && this.tags.length > 0;
    }

    public boolean hasCustomBlockData() {
        if (!this.hasCompoundTag()) {
            return false;
        }

        CompoundTag tag = this.getNamedTag();
        return tag.contains("BlockEntityTag") && tag.get("BlockEntityTag") instanceof CompoundTag;

    }

    public Item clearCustomBlockData() {
        if (!this.hasCompoundTag()) {
            return this;
        }
        CompoundTag tag = this.getNamedTag();

        if (tag.contains("BlockEntityTag") && tag.get("BlockEntityTag") instanceof CompoundTag) {
            tag.remove("BlockEntityTag");
            this.setNamedTag(tag);
        }

        return this;
    }

    public Item setCustomBlockData(CompoundTag compoundTag) {
        CompoundTag tags = compoundTag.copy();
        tags.setName("BlockEntityTag");

        CompoundTag tag;
        if (!this.hasCompoundTag()) {
            tag = new CompoundTag();
        } else {
            tag = this.getNamedTag();
        }

        tag.putCompound("BlockEntityTag", tags);
        this.setNamedTag(tag);

        return this;
    }

    public CompoundTag getCustomBlockData() {
        if (!this.hasCompoundTag()) {
            return null;
        }

        CompoundTag tag = this.getNamedTag();

        if (tag.contains("BlockEntityTag")) {
            Tag bet = tag.get("BlockEntityTag");
            if (bet instanceof CompoundTag) {
                return (CompoundTag) bet;
            }
        }

        return null;
    }
    
    public boolean hasEnchantments() {
        if (!this.hasCompoundTag()) {
            return false;
        }

        CompoundTag tag = this.getNamedTag();

        if (tag.contains("ench")) {
            Tag enchTag = tag.get("ench");
            return enchTag instanceof ListTag;
        }

        return false;
    }

    /**
     * Convenience method to check if the item stack has positive level on a specific enchantment by it's id.
     * @param id The enchantment ID from {@link Enchantment} constants.
     */
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public boolean hasEnchantment(int id) {
        return getEnchantmentLevel(id) > 0;
    }

    /**
     * Find the enchantment level by the enchantment id.
     * @param id The enchantment ID from {@link Enchantment} constants.
     * @return {@code 0} if the item don't have that enchantment or the current level of the given enchantment.
     */
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public int getEnchantmentLevel(int id) {
        if (!this.hasEnchantments()) {
            return 0;
        }

        for (CompoundTag entry : this.getNamedTag().getList("ench", CompoundTag.class).getAll()) {
            if (entry.getShort("id") == id) {
                return entry.getShort("lvl");
            }
        }
        
        return 0;
    }

    public Enchantment getEnchantment(int id) {
        return getEnchantment((short) (id & 0xffff));
    }

    public Enchantment getEnchantment(short id) {
        if (!this.hasEnchantments()) {
            return null;
        }

        for (CompoundTag entry : this.getNamedTag().getList("ench", CompoundTag.class).getAll()) {
            if (entry.getShort("id") == id) {
                Enchantment e = Enchantment.getEnchantment(entry.getShort("id"));
                if (e != null) {
                    e.setLevel(entry.getShort("lvl"), false);
                    return e;
                }
            }
        }

        return null;
    }

    public void addEnchantment(Enchantment... enchantments) {
        CompoundTag tag;
        if (!this.hasCompoundTag()) {
            tag = new CompoundTag();
        } else {
            tag = this.getNamedTag();
        }

        ListTag<CompoundTag> ench;
        if (!tag.contains("ench")) {
            ench = new ListTag<>("ench");
            tag.putList(ench);
        } else {
            ench = tag.getList("ench", CompoundTag.class);
        }

        for (Enchantment enchantment : enchantments) {
            boolean found = false;

            for (int k = 0; k < ench.size(); k++) {
                CompoundTag entry = ench.get(k);
                if (entry.getShort("id") == enchantment.getId()) {
                    ench.add(k, new CompoundTag()
                            .putShort("id", enchantment.getId())
                            .putShort("lvl", enchantment.getLevel())
                    );
                    found = true;
                    break;
                }
            }

            if (!found) {
                ench.add(new CompoundTag()
                        .putShort("id", enchantment.getId())
                        .putShort("lvl", enchantment.getLevel())
                );
            }
        }

        this.setNamedTag(tag);
    }

    public Enchantment[] getEnchantments() {
        if (!this.hasEnchantments()) {
            return Enchantment.EMPTY_ARRAY;
        }

        List<Enchantment> enchantments = new ArrayList<>();

        ListTag<CompoundTag> ench = this.getNamedTag().getList("ench", CompoundTag.class);
        for (CompoundTag entry : ench.getAll()) {
            Enchantment e = Enchantment.getEnchantment(entry.getShort("id"));
            if (e != null) {
                e.setLevel(entry.getShort("lvl"), false);
                enchantments.add(e);
            }
        }

        return enchantments.toArray(Enchantment.EMPTY_ARRAY);
    }

    @Since("1.4.0.0-PN")
    public int getRepairCost() {
        if (this.hasCompoundTag()) {
            CompoundTag tag = this.getNamedTag();
            if (tag.contains("RepairCost")) {
                Tag repairCost = tag.get("RepairCost");
                if (repairCost instanceof IntTag) {
                    return ((IntTag) repairCost).data;
                }
            }
        }
        return 0;
    }

    @Since("1.4.0.0-PN")
    public Item setRepairCost(int cost) {
        if (cost <= 0 && this.hasCompoundTag()) {
            return this.setNamedTag(this.getNamedTag().remove("RepairCost"));
        }

        CompoundTag tag;
        if (!this.hasCompoundTag()) {
            tag = new CompoundTag();
        } else {
            tag = this.getNamedTag();
        }
        return this.setNamedTag(tag.putInt("RepairCost", cost));
    }
    
    public boolean hasCustomName() {
        if (!this.hasCompoundTag()) {
            return false;
        }

        CompoundTag tag = this.getNamedTag();
        if (tag.contains("display")) {
            Tag tag1 = tag.get("display");
            return tag1 instanceof CompoundTag && ((CompoundTag) tag1).contains("Name") && ((CompoundTag) tag1).get("Name") instanceof StringTag;
        }

        return false;
    }

    public String getCustomName() {
        if (!this.hasCompoundTag()) {
            return "";
        }

        CompoundTag tag = this.getNamedTag();
        if (tag.contains("display")) {
            Tag tag1 = tag.get("display");
            if (tag1 instanceof CompoundTag && ((CompoundTag) tag1).contains("Name") && ((CompoundTag) tag1).get("Name") instanceof StringTag) {
                return ((CompoundTag) tag1).getString("Name");
            }
        }

        return "";
    }

    public Item setCustomName(String name) {
        if (name == null || name.equals("")) {
            this.clearCustomName();
        }

        CompoundTag tag;
        if (!this.hasCompoundTag()) {
            tag = new CompoundTag();
        } else {
            tag = this.getNamedTag();
        }
        if (tag.contains("display") && tag.get("display") instanceof CompoundTag) {
            tag.getCompound("display").putString("Name", name);
        } else {
            tag.putCompound("display", new CompoundTag("display")
                    .putString("Name", name)
            );
        }
        this.setNamedTag(tag);
        return this;
    }

    public Item clearCustomName() {
        if (!this.hasCompoundTag()) {
            return this;
        }

        CompoundTag tag = this.getNamedTag();

        if (tag.contains("display") && tag.get("display") instanceof CompoundTag) {
            tag.getCompound("display").remove("Name");
            if (tag.getCompound("display").isEmpty()) {
                tag.remove("display");
            }

            this.setNamedTag(tag);
        }

        return this;
    }

    public String[] getLore() {
        Tag tag = this.getNamedTagEntry("display");
        ArrayList<String> lines = new ArrayList<>();

        if (tag instanceof CompoundTag) {
            CompoundTag nbt = (CompoundTag) tag;
            ListTag<StringTag> lore = nbt.getList("Lore", StringTag.class);

            if (lore.size() > 0) {
                for (StringTag stringTag : lore.getAll()) {
                    lines.add(stringTag.data);
                }
            }
        }

        return lines.toArray(EmptyArrays.EMPTY_STRINGS);
    }

    public Item setLore(String... lines) {
        CompoundTag tag;
        if (!this.hasCompoundTag()) {
            tag = new CompoundTag();
        } else {
            tag = this.getNamedTag();
        }
        ListTag<StringTag> lore = new ListTag<>("Lore");

        for (String line : lines) {
            lore.add(new StringTag("", line));
        }

        if (!tag.contains("display")) {
            tag.putCompound("display", new CompoundTag("display").putList(lore));
        } else {
            tag.getCompound("display").putList(lore);
        }

        this.setNamedTag(tag);
        return this;
    }

    public Tag getNamedTagEntry(String name) {
        CompoundTag tag = this.getNamedTag();
        if (tag != null) {
            return tag.contains(name) ? tag.get(name) : null;
        }

        return null;
    }

    public CompoundTag getNamedTag() {
        if (!this.hasCompoundTag()) {
            return null;
        }

        if (this.cachedNBT == null) {
            this.cachedNBT = parseCompoundTag(this.tags);
        }

        if (this.cachedNBT != null) {
            this.cachedNBT.setName("");
        }

        return this.cachedNBT;
    }

    public Item setNamedTag(CompoundTag tag) {
        if (tag.isEmpty()) {
            return this.clearNamedTag();
        }
        tag.setName(null);

        this.cachedNBT = tag;
        this.tags = writeCompoundTag(tag);

        return this;
    }

    public Item clearNamedTag() {
        return this.setCompoundTag(EmptyArrays.EMPTY_BYTES);
    }

    public static CompoundTag parseCompoundTag(byte[] tag) {
        try {
            return NBTIO.read(tag, ByteOrder.LITTLE_ENDIAN);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public byte[] writeCompoundTag(CompoundTag tag) {
        try {
            tag.setName("");
            return NBTIO.write(tag, ByteOrder.LITTLE_ENDIAN);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isNull() {
        return this.count <= 0 || this.identifier == AIR;
    }

    final public String getName() {
        return this.hasCustomName() ? this.getCustomName() : this.name;
    }

    final public boolean canBePlaced() {
        return ((this.block != null) && this.block.canBePlaced());
    }

    public Block getBlock() {
        if (this.block != null) {
            return this.block.clone();
        } else {
            return Block.get(BlockID.AIR);
        }
    }

    @Since("1.4.0.0-PN")
    @API(definition = API.Definition.INTERNAL, usage = API.Usage.INCUBATING)
    public Block getBlockUnsafe() {
        return this.block;
    }
    
    @Deprecated
    public int getId() {
        return this.getRegistry().getLegacyIdFromIdentifier(this.identifier);
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public final int getNetworkFullId() throws UnknownNetworkIdException {
        try {
            return RuntimeItems.getRuntimeMapping().getNetworkFullId(this);
        } catch (IllegalArgumentException e) {
            throw new UnknownNetworkIdException(this, e);
        }
    }

    @Since("1.4.0.0-PN")
    public final int getNetworkId() throws UnknownNetworkIdException {
        return RuntimeItems.getNetworkId(getNetworkFullId());
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public String getNamespaceId() {
        return this.identifier.getFullString();
    }
    
    @Deprecated
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public int getBlockId() {
        if (block != null) {
            return block.getId();
        } else {
            return -1;
        }
    }

    public int getDamage() {
        return meta;
    }

    public void setDamage(Integer meta) {
        if (meta != null) {
            this.meta = meta & 0xffff;
        } else {
            this.hasMeta = false;
        }
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public Item createFuzzyCraftingRecipe() {
        Item item = this.clone();
        item.meta = 0;
        item.hasMeta = false;
        return item;
    }

    public int getMaxStackSize() {
        return block == null ? 64 : block.getItemMaxStackSize();
    }

    final public Short getFuelTime() {
        if (!Fuel.duration.containsKey(this.identifier)) {
            return null;
        }
        
        if (this.identifier != BUCKET || this.meta == 10) {
            return Fuel.duration.get(this.identifier);
        }
        
        return null;
    }

    public boolean useOn(Entity entity) {
        return false;
    }

    public boolean useOn(Block block) {
        return false;
    }

    public boolean isTool() {
        return false;
    }

    public int getMaxDurability() {
        return -1;
    }

    public int getTier() {
        return 0;
    }

    public boolean isPickaxe() {
        return false;
    }

    public boolean isAxe() {
        return false;
    }

    public boolean isSword() {
        return false;
    }

    public boolean isShovel() {
        return false;
    }

    public boolean isHoe() {
        return false;
    }

    public boolean isShears() {
        return false;
    }

    public boolean isArmor() {
        return false;
    }

    public boolean isHelmet() {
        return false;
    }

    public boolean isChestplate() {
        return false;
    }

    public boolean isLeggings() {
        return false;
    }

    public boolean isBoots() {
        return false;
    }

    public int getEnchantAbility() {
        return 0;
    }

    public int getAttackDamage() {
        return 1;
    }

    public int getArmorPoints() {
        return 0;
    }

    public int getToughness() {
        return 0;
    }

    public boolean isUnbreakable() {
        return false;
    }

    /**
     * If the item is resistant to lava and fire and can float on lava like if it was on water.
     * @since 1.4.0.0-PN
     */
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public boolean isLavaResistant() {
        return false;
    }
    
    public boolean onUse(Player player, int ticksUsed) {
        return false;
    }

    public boolean onRelease(Player player, int ticksUsed) {
        return false;
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public boolean damageWhenBreaking() {
        return true;
    }

    @Override
    final public String toString() {
        return "Item " + this.name + " (" + this.identifier.getFullString() + ":" + (!this.hasMeta ? "?" : this.meta) + ")x" + this.count + (this.hasCompoundTag() ? " tags:0x" + Binary.bytesToHexString(this.getCompoundTag()) : "");
    }

    public int getDestroySpeed(Block block, Player player) {
        return 1;
    }

    public boolean onActivate(Level level, Player player, Block block, Block target, BlockFace face, double fx, double fy, double fz) {
        return false;
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public final Item decrement(int amount) {
        return increment(-amount);
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public final Item increment(int amount) {
        if (count + amount <= 0) {
            return getBlock(BlockID.AIR);
        }
        Item cloned = this.clone();
        cloned.count += amount;
        return cloned;
    }

    /**
     * When true, this item can be used to reduce growing times like a bone meal.
     * @return {@code true} if it can act like a bone meal
     */
    @Since("1.4.0.0-PN")
    @PowerNukkitOnly
    public boolean isFertilizer() {
        return false;
    }

    /**
     * Called when a player uses the item on air, for example throwing a projectile.
     * Returns whether the item was changed, for example count decrease or durability change.
     *
     * @param player player
     * @param directionVector direction
     * @return item changed
     */
    public boolean onClickAir(Player player, Vector3 directionVector) {
        return false;
    }

    @Override
    public final boolean equals(Object item) {
        return item instanceof Item && this.equals((Item) item, true);
    }

    public final boolean equals(Item item, boolean checkDamage) {
        return equals(item, checkDamage, true);
    }

    public final boolean equals(Item item, boolean checkDamage, boolean checkCompound) {
        if (this.getIdentifier().equals(item.getIdentifier()) && (!checkDamage || this.getDamage() == item.getDamage())) {
            if (checkCompound) {
                if (Arrays.equals(this.getCompoundTag(), item.getCompoundTag())) {
                    return true;
                } else if (this.hasCompoundTag() && item.hasCompoundTag()) {
                    return this.getNamedTag().equals(item.getNamedTag());
                }
            } else {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns whether the specified item stack has the same ID, damage, NBT and count as this item stack.
     *
     * @param other item
     * @return equal
     */
    public final boolean equalsExact(Item other) {
        return this.equals(other, true, true) && this.count == other.count;
    }

    /**
     * Same as {@link #equals(Item, boolean)} but the enchantment order of the items does not affect the result.
     * @since 1.2.1.0-PN
     */
    public final boolean equalsIgnoringEnchantmentOrder(Item item, boolean checkDamage) {
        if (!this.equals(item, checkDamage, false)) {
            return false;
        }

        if (Arrays.equals(this.getCompoundTag(), item.getCompoundTag())) {
            return true;
        }

        if (!this.hasCompoundTag() || !item.hasCompoundTag()) {
            return false;
        }

        CompoundTag thisTags = this.getNamedTag();
        CompoundTag otherTags = item.getNamedTag();
        if (thisTags.equals(otherTags)) {
            return true;
        }

        if (!thisTags.contains("ench") || !otherTags.contains("ench")
                || !(thisTags.get("ench") instanceof ListTag)
                || !(otherTags.get("ench") instanceof ListTag)
                || thisTags.getList("ench").size() != otherTags.getList("ench").size()) {
            return false;
        }

        ListTag<CompoundTag> thisEnchantmentTags = thisTags.getList("ench", CompoundTag.class);
        ListTag<CompoundTag> otherEnchantmentTags = otherTags.getList("ench", CompoundTag.class);

        int size = thisEnchantmentTags.size();
        Int2IntMap enchantments = new Int2IntArrayMap(size);
        enchantments.defaultReturnValue(Integer.MIN_VALUE);

        for (int i = 0; i < size; i++) {
            CompoundTag tag = thisEnchantmentTags.get(i);
            enchantments.put(tag.getShort("id"), tag.getShort("lvl"));
        }

        for (int i = 0; i < size; i++) {
            CompoundTag tag = otherEnchantmentTags.get(i);
            if (enchantments.get(tag.getShort("id")) != tag.getShort("lvl")) {
                return false;
            }
        }

        return true;
    }

    @Deprecated
    public final boolean deepEquals(Item item) {
        return equals(item, true);
    }

    @Deprecated
    public final boolean deepEquals(Item item, boolean checkDamage) {
        return equals(item, checkDamage, true);
    }

    @Deprecated
    public final boolean deepEquals(Item item, boolean checkDamage, boolean checkCompound) {
        return equals(item, checkDamage, checkCompound);
    }

    @Override
    public Item clone() {
        try {
            Item item = (Item) super.clone();
            item.tags = this.tags.clone();
            item.cachedNBT = null;
            return item;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
