package cn.nukkit.item;

/**
 * @author GoodLucky777
 */
public class ItemFriedChickenLeg extends ItemEdible {

    public ItemFriedChickenLeg() {
        this(0, 1);
    }
    
    public ItemFriedChickenLeg(Integer meta) {
        this(meta, 1);
    }
    
    public ItemFriedChickenLeg(Integer meta, int count) {
        super(FRIED_CHICKEN_LEG, 0, count, "Fried Chicken Leg");
    }
}
