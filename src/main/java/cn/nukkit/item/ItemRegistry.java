package cn.nukkit.item;

import cn.nukkit.Server;
import cn.nukkit.utils.BinaryStream;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.Identifier;
import cn.nukkit.utils.Utils;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.ParametersAreNonnullByDefault;

import io.netty.util.internal.EmptyArrays;

import lombok.extern.log4j.Log4j2;

/**
 * @author GoodLucky777
 */
@Log4j2
@ParametersAreNonnullByDefault
public class ItemRegistry {

    private static ItemRegistry instance;
    
    private BiMap<Integer, Identifier> legacyIdRegistration = HashBiMap.create();
    private BiMap<Identifier, Item> itemRegistration = HashBiMap.create();
    private final AtomicInteger runtimeIdAllocator = new AtomicInteger(256);
    private BiMap<Integer, Identifier> runtimeIdRegistration = HashBiMap.create();
    private byte[] itemPalette;
    private ArrayList<Item> creativeItemRegisteration = new ArrayList<>();
    
    public ItemRegistry() {
        this.loadItemIds();
        this.registerVanillaItems();
        this.loadCreativeItems();
        this.loadItemPalette();
        
        instance = this;
    }
    
    public static ItemRegistry getInstance() {
        return instance;
    }
    
    private void loadItemIds() {
        try (InputStream stream = Server.class.getClassLoader().getResourceAsStream("item_ids.csv")) { 
            if (stream == null) {
                throw new AssertionError("Unable to find item_ids.csv");
            }
            
            int count = 0;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    count++;
                    line = line.trim();
                    if (line.isEmpty()) {
                        continue;
                    }
                    String[] parts = line.split(",");
                    Preconditions.checkArgument(parts.length == 2 || parts[0].matches("^[0-9]+$"));
                    if (parts.length > 1) {
                        legacyIdRegistration.put(Integer.parseInt(parts[0]), Identifier.fromFullString(parts[1]));
                    }
                }
            } catch (Exception e) {
                throw new IOException("Error reading the line " + count + " of the item_ids.csv", e);
            }
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
    
    public void loadCreativeItems() {
        Config config = new Config(Config.JSON);
        config.load(Server.class.getClassLoader().getResourceAsStream("creativeitems.json"));
        List<Map> list = config.getMapList("items");
        for (Map map : list) {
            try {
                registerCreativeItem(this.getItemfromJsonStringId(map));
            } catch (Exception e) {
                log.error("Error while registering a creative item", e);
            }
        }
    }
    
    private void loadItemPalette() {
        BinaryStream paletteBuffer = new BinaryStream();
        
        paletteBuffer.putUnsignedVarInt(runtimeIdRegistration.size());
        for (Integer runtimeId : runtimeIdRegistration.keySet()) {
            paletteBuffer.putString(this.getIdentifierFromRuntimeId(runtimeId).getFullString());
            paletteBuffer.putLShort(runtimeId);
            paletteBuffer.putBoolean(false);
        }
        
        itemPalette = paletteBuffer.getBuffer();
    }
    
    public void clearCreativeItems() {
        this.creativeItemRegisteration.clear();
    }
    
    public Item getCreativeItem(int index) {
        return (index >= 0 && index < this.creativeItemRegisteration.size()) ? this.creativeItemRegisteration.get(index) : null;
    }
    
    public int getCreativeItemIndex(Item item) {
        for (int i = 0; i < this.creativeItemRegisteration.size(); i++) {
            if (item.equals(this.creativeItemRegisteration.get(i), !item.isTool())) {
                return i;
            }
        }
        return -1;
    }
    
    public ArrayList<Item> getCreativeItems() {
        return new ArrayList<>(this.creativeItemRegisteration);
    }
    
    public boolean isCreativeItem(Item item) {
        for (Item creative : this.creativeItemRegisteration) {
            if (item.equals(creative, !item.isTool())) {
                return true;
            }
        }
        return false;
    }
    
    public Identifier getIdentifierFromItem(Item item) {
        return itemRegistration.inverse().get(item);
    }
    
    public Identifier getIdentifierFromLegacyId(int legacyId) {
        return legacyIdRegistration.get(legacyId);
    }
    
    public Identifier getIdentifierFromRuntimeId(int runtimeId) {
        return runtimeIdRegistration.get(runtimeId);
    }
    
    public Item getItemFromIdentifer(Identifier identifier) {
        return itemRegistration.get(identifier);
    }
    
    public Item getItemFromJsonStringId(Map<String, Object> data) {
        String nbt = (String) data.get("nbt_b64");
        byte[] nbtBytes = nbt != null ? Base64.getDecoder().decode(nbt) : EmptyArrays.EMPTY_BYTES;
        Identifier identifier = Identifier.fromFullString(data.get("id").toString());
        Item item;
        if (data.containsKey("damage")) {
            int meta = Utils.toInt(data.get("damage"));
            item = this.getItemFromIdentifer(identifier).setDamage(meta);
        } else {
            item = this.getItemFromIdentifer(identifier);
        }
        item.setCompoundTag(nbtBytes);
        return item;
    }
    
    public int getLegacyIdFromIdentifier(Identifier identifier) {
        return legacyIdRegistration.inverse().get(identifier);
    }
    
    public int getRuntimeIdFromIdentifier(Identifier identifier) {
        return runtimeIdRegistration.inverse().get(identifier);
    }
    
    public synchronized void registerItem(Identifier identifier, Item item) {
        Preconditions.checkArgument(item.getId() > 0, "Item ID should be larger than 0");
        
        itemRegistration.put(identifier, item);
        runtimeIdRegistration.put(this.runtimeIdAllocator.getAndIncrement(), identifier);
    }
    
    public synchronized void registerVanillaItem(Item item) {
        Preconditions.checkArgument(item.getId() > 0, "Item ID should be larger than 0");
        
        Identifier identifier = this.getIdentifierFromLegacyId(item.getId());
        itemRegistration.put(identifier, item);
        runtimeIdRegistration.put(this.runtimeIdAllocator.getAndIncrement(), identifier);
    }
    
    private void registerVanillaItems() {
        this.registerVanillaItem(new ItemShovelIron()); // 256
        this.registerVanillaItem(new ItemPickaxeIron()); // 257
        this.registerVanillaItem(new ItemAxeIron()); // 258
        this.registerVanillaItem(new ItemFlintSteel()); // 259
        
    }
    
    public void registerCreativeItem(Item item) {
        this.creativeItemRegisteration.add(item);
    }
    
    public byte[] getItemPalette() {
        return itemPalette;
    }
    
    public static void unregisterCreativeItem(Item item) {
        int index = this.getCreativeItemIndex(item);
        if (index != -1) {
            this.creativeItemRegisteration.remove(index);
        }
    }
}
