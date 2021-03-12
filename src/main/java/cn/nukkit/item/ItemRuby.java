package cn.nukkit.item;

/**
 * @author GoodLucky777
 */
public class ItemRuby extends Item {

    public ItemRuby() {
        this(0, 1);
    }
    
    public ItemRuby(Integer meta) {
        this(meta, 1);
    }
    
    public ItemRuby(Integer meta, int count) {
        super(RUBY, 0, count, "Ruby");
    }
}
