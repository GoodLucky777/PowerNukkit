package cn.nukkit.item;

/**
 * @author GoodLucky777
 */
public class ItemBeer extends ItemEdible {

    public ItemBeer() {
        this(0, 1);
    }
    
    public ItemBeer(Integer meta) {
        this(meta, 1);
    }
    
    public ItemBeer(Integer meta, int count) {
        super(BEER, 0, count, "Beer");
    }
}
