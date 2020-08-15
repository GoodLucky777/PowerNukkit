package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemDiamond;
import cn.nukkit.item.ItemIngotIron;
import cn.nukkit.item.ItemTool;

/**
 * Created by good777LUCKY
 */
public class BlockNetherReactor extends BlockSolidMeta {

    public static final int NORMAL = 0;
    public static final int INITIALIZED = 1;
    public static final int FINISHED = 2;
    
    public BlockNetherReactor() {
        // Does Nothing
    }
    
    public BlockNetherReactor(int meta) {
        super(meta);
    }
    
    @Override
    public int getId() {
        return NETHER_REACTOR;
    }
    
    @Override
    public String getName() {
        String[] names = new String[]{
            "Nether Reactor Core",
            "Initialized Nether Reactor Core",
            "Finished Nether Reactor Core",
            "Nether Reactor Core"
        };
        return names[this.getDamage() & 0x03];
    }
    
    @Override
    public double getHardness() {
        return 10;
    }
    
    @Override
    public double getResistance() {
        return 30;
    }
    
    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
    
    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }
    
    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe()) {
            return new Item[]{
                new ItemDiamond(0, 3),
                new ItemIngotIron(0, 6)
            };
        } else {
            return new Item[0];
        }
    }
    
    //TODO: Add Block Color
}
