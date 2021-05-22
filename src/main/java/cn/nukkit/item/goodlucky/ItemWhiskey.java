package cn.nukkit.item;

/**
 * @author GoodLucky777
 */
public class ItemWhiskey extends ItemEdible {

    public ItemWhiskey() {
        this(0, 1);
    }
    
    public ItemWhiskey(Integer meta) {
        this(meta, 1);
    }
    
    public ItemWhiskey(Integer meta, int count) {
        super(WHISKEY, 0, count, "Whisky");
    }
}
