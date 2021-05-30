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
    
    private BiMap<Identifier, Item> itemRegisteration = HashBiMap.create();
    
    public ItemRegistry() {
        this.registerVanilla();
        
        instance = this;
    }
    
    public static ItemRegistry getInstance() {
        return instance;
    }
    
    public synchronized void registerItem(Identifier identifier, Item item) {
        Preconditions.checkArgument(item.getId() > 0, "Item ID should be larger than 0");
        
        itemRegisteration.put(identifier, item);
    }
    
    public void registerVanilla() {
        
    }
}
