package cn.nukkit.item;

/**
 * @author GoodLucky777
 */
public class ItemBacon extends ItemEdible {

    public ItemBacon() {
        this(0, 1);
    }
    
    public ItemBacon(Integer meta) {
        this(meta, 1);
    }
    
    public ItemBacon(Integer meta, int count) {
        super(BACON, 0, count, "Bacon");
    }
}
