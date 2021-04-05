package cn.nukkit.item;

/**
 * @author GoodLucky777
 */
public class ItemSapphire extends Item {

    public ItemSapphire() {
        this(0, 1);
    }
    
    public ItemSapphire(Integer meta) {
        this(meta, 1);
    }
    
    public ItemSapphire(Integer meta, int count) {
        super(SAPPHIRE, 0, count, "Sapphire");
    }
}
