package cn.nukkit.item;

/**
 * @author good777LUCKY
 */
public class ItemCompassLodestone extends Item {

    public ItemCompassLodestone() {
        this(0, 1);
    }
    
    public ItemCompassLodestone(Integer meta) {
        this(meta, 1);
    }
    
    public ItemCompassLodestone(Integer meta, int count) {
        super(LODESTONECOMPASS, meta, count, "Lodestone Compass");
    }
    
    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
