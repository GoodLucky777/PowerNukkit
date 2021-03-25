package cn.nukkit.item;

/**
 * @author GoodLucky777
 */
public class ItemFriedEgg extends ItemEdible {

    public ItemFriedEgg() {
        this(0, 1);
    }
    
    public ItemFriedEgg(Integer meta) {
        this(meta, 1);
    }
    
    public ItemFriedEgg(Integer meta, int count) {
        super(FRIED_EGG, 0, count, "Fried Egg");
    }
}
