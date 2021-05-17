package cn.nukkit.item;

/**
 * @author GoodLucky777
 */
public class ItemChocolate extends ItemEdible {

    public ItemChocolate() {
        this(0, 1);
    }
    
    public ItemChocolate(Integer meta) {
        this(meta, 1);
    }
    
    public ItemChocolate(Integer meta, int count) {
        super(CHOCOLATE, 0, count, "Chocolate");
    }
}
