package cn.nukkit.item;

/**
 * @author GoodLucky777
 */
public class ItemBaguette extends ItemEdible {

    public ItemBaguette() {
        this(0, 1);
    }
    
    public ItemBaguette(Integer meta) {
        this(meta, 1);
    }
    
    public ItemBaguette(Integer meta, int count) {
        super(BAGUETTE, 0, count, "Baguette");
    }
}
