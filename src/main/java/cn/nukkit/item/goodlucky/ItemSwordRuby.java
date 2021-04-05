package cn.nukkit.item;

public class ItemSwordRuby extends ItemTool {

    public ItemSwordRuby() {
        this(0, 1);
    }
    
    public ItemSwordRuby(Integer meta) {
        this(meta, 1);
    }
    
    public ItemSwordRuby(Integer meta, int count) {
        super(RUBY_SWORD, meta, count, "Ruby Sword");
    }
    
    @Override
    public int getMaxDurability() {
        return ItemTool.DURABILITY_DIAMOND;
    }
    
    @Override
    public boolean isSword() {
        return true;
    }
    
    @Override
    public int getTier() {
        return ItemTool.TIER_DIAMOND;
    }
    
    @Override
    public int getAttackDamage() {
        return 9;
    }
}
