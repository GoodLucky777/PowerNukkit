package cn.nukkit.item;

/**
 * @author GoodLucky777
 */
public class ItemOpal extends Item {

    public ItemOpal() {
        this(0, 1);
    }
    
    public ItemOpal(Integer meta) {
        this(meta, 1);
    }
    
    public ItemOpal(Integer meta, int count) {
        super(OPAL, 0, count, "Opal");
    }
}
