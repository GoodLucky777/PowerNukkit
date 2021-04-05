package cn.nukkit.item;

/**
 * @author GoodLucky777
 */
public class ItemCheese extends ItemEdible {

    public ItemCheese() {
        this(0, 1);
    }
    
    public ItemCheese(Integer meta) {
        this(meta, 1);
    }
    
    public ItemCheese(Integer meta, int count) {
        super(CHEESE, 0, count, "Cheese");
    }
}
