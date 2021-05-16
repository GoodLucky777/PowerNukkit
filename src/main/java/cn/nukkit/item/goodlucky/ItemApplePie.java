package cn.nukkit.item;

/**
 * @author GoodLucky777
 */
public class ItemApplePie extends ItemEdible {

    public ItemApplePie() {
        this(0, 1);
    }
    
    public ItemApplePie(Integer meta) {
        this(meta, 1);
    }
    
    public ItemApplePie(Integer meta, int count) {
        super(APPLE_PIE, 0, count, "Apple Pie");
    }
}
