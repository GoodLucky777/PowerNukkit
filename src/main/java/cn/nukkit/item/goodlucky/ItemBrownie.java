package cn.nukkit.item;

/**
 * @author GoodLucky777
 */
public class ItemBrownie extends ItemEdible {

    public ItemBrownie() {
        this(0, 1);
    }
    
    public ItemBrownie(Integer meta) {
        this(meta, 1);
    }
    
    public ItemBrownie(Integer meta, int count) {
        super(BROWNIE, 0, count, "Brownie");
    }
}
