package cn.nukkit.item;

/**
 * @author GoodLucky777
 */
public class ItemFriedChicken extends ItemEdible {

    public ItemFriedChicken() {
        this(0, 1);
    }
    
    public ItemFriedChicken(Integer meta) {
        this(meta, 1);
    }
    
    public ItemFriedChicken(Integer meta, int count) {
        super(FRIED_CHICKEN, 0, count, "Fried Chicken");
    }
}
