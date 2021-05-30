package cn.nukkit.item;

import cn.nukkit.utils.Identifier;

/**
 * @author GoodLucky777
 */
public class ItemRegistry {

    private static final ItemRegistry instance;
    
    public ItemRegistry() {
        this,registerVanilla();
    }
    
    public static getInstance() {
        return instance;
    }
    
    public void registerItem(Item item, Identifier identifier) {
        
    }
    
    public void registerVanilla() {
        
    }
}
