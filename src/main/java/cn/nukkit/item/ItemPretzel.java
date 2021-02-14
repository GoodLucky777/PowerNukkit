package cn.nukkit.item;

/**
 * @author GoodLucky777
 */
public class ItemPretzel extends ItemEdible {

    public ItemPretzel() {
        this(0, 1);
    }
    
    public ItemPretzel(Integer meta) {
        this(meta, 1);
    }
    
    public ItemPretzel(Integer meta, int count) {
        super(PRETZEL, 0, count, "Pretzel");
    }
}
