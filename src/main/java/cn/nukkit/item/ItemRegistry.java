package cn.nukkit.item;

import cn.nukkit.Server;
import cn.nukkit.utils.Identifier;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.ParametersAreNonnullByDefault;

import lombok.extern.log4j.Log4j2;

/**
 * @author GoodLucky777
 */
@Log4j2
@ParametersAreNonnullByDefault
public class ItemRegistry {

    private static ItemRegistry instance;
    
    private BiMap<Integer, Identifier> legacyIdRegistration = HashBiMap.create();
    private BiMap<Identifier, Item> itemRegisteration = HashBiMap.create();
    private final AtomicInteger runtimeIdAllocator = new AtomicInteger(256);
    private BiMap<Integer, Identifier> runtimeIdRegistration = HashBiMap.create();
    
    public ItemRegistry() {
        this.loadItemIds();
        this.registerVanillaItems();
        
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
    
    public Identifier getIdentifierFromLegacyId(int legacyId) {
        return legacyIdRegistration.get(legacyId);
    }
    
    public Identifier getIdentifierFromRuntimeId(int runtimeId) {
        return runtimeIdRegistration.get(runtimeId);
    }
    
    public int getLegacyIdFromIdentifier(Identifier identifier) {
        return legacyIdRegistration.inverse().get(identifier);
    }
    
    public int getRuntimeIdFromIdentifier(Identifier identifier) {
        return runtimeIdRegistration.inverse().get(identifier);
    }
    
    public synchronized void registerItem(Identifier identifier, Item item) {
        Preconditions.checkArgument(item.getId() > 0, "Item ID should be larger than 0");
        
        itemRegisteration.put(identifier, item);
        runtimeIdRegistration.put(this.runtimeIdAllocator.getAndIncrement(), identifier);
    }
    
    public synchronized void registerVanillaItem(Item item) {
        Preconditions.checkArgument(item.getId() > 0, "Item ID should be larger than 0");
        
        itemRegisteration.put(this.getIdentifierFromLegacyId(item.getId()), item);
        runtimeIdRegistration.put(this.runtimeIdAllocator.getAndIncrement(), identifier);
    }
    
    private void registerVanillaItems() {
        this.registerVanillaItem(new ItemShovelIron()); // 256
        this.registerVanillaItem(new ItemPickaxeIron()); // 257
        this.registerVanillaItem(new ItemAxeIron()); // 258
        this.registerVanillaItem(new ItemFlintSteel()); // 259
        
    }
}
