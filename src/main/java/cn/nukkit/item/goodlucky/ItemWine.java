package cn.nukkit.item;

/**
 * @author GoodLucky777
 */
public class ItemWine extends ItemEdible {

    public ItemWine() {
        this(0, 1);
    }
    
    public ItemWine(Integer meta) {
        this(meta, 1);
    }
    
    public ItemWine(Integer meta, int count) {
        super(WINE, 0, count, "Wine");
    }
}
