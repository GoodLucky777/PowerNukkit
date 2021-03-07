package cn.nukkit.item;

/**
 * @author GoodLucky777
 */
public class ItemGrenadeFrag extends ProjectileItem {

    public ItemGrenadeFrag() {
        this(0, 1);
    }
    
    public ItemGrenadeFrag(Integer meta) {
        this(meta, 1);
    }
    
    public ItemGrenadeFrag(Integer meta, int count) {
        super(GRENADE_FRAG, 0, count, "Frag Grenade");
    }
    
    @Override
    public int getMaxStackSize() {
        return 1;
    }
    
    @Override
    public String getProjectileEntityType() {
        return "GrenadeFrag";
    }
    
    @Override
    public float getThrowForce() {
        return 1.6f;
    }
}
