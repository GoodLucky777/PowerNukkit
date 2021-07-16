package cn.nukkit.block;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;

/**
 * @author GoodLucky777
 */
@PowerNukkitOnly
@Since("FUTURE")
public class BlockSnowPowder extends BlockFlowable {

    @PowerNukkitOnly
    @Since("FUTURE")
    public BlockSnowPowder() {
        // Does nothing
    }
    
    @Override
    public int getId() {
        return POWDER_SNOW;
    }
    
    @Override
    public String getName() {
        return "Powder Snow";
    }
    
    @Override
    public double getHardness() {
        return 0.25;
    }
    
    @Override
    public double getResistance() {
        return 0.1;
    }
    
    @Override
    public Item[] getDrops(Item item) {
        return Item.EMPTY_ARRAY;
    }
    
    @Override
    public BlockColor getColor() {
        return BlockColor.SNOW;
    }
}
