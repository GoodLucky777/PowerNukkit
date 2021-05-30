package cn.nukkit.item;

import cn.nukkit.utils.Identifier;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import javax.annotation.ParametersAreNonnullByDefault;

import lombok.extern.log4j.Log4j2;

/**
 * @author GoodLucky777
 */
@Log4j2
@ParametersAreNonnullByDefault
public class ItemRegistry {

    private static final ItemRegistry instance;
    
    private BiMap<Integer, Identifier> legacyIdRegistration = HashBiMap.create();
    private BiMap<Identifier, Item> itemRegisteration = HashBiMap.create();
    
    public ItemRegistry() {
        this.loadItemIds();
        this.registerVanilla();
        
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
                        legacyIdRegistration.put(Integer.parseInt(parts[0]), Identifier.fromString(parts[1]));
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
        return 
    }
    
    public int getLegacyIdFromIdentifier(Identifier identifier) {
        return 
    }
    
    public synchronized void registerItem(Identifier identifier, Item item) {
        Preconditions.checkArgument(item.getId() > 0, "Item ID should be larger than 0");
        
        itemRegisteration.put(identifier, item);
    }
    
    public void registerVanilla() {
        
    }
}
