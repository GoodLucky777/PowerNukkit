package cn.nukkit.block;

import cn.nukkit.utils.BlockColor;

/**
 * @author good777LUCKY
 */
public class BlockReserved6 extends BlockSolid {

    public BlockReserved6() {
        // Does Nothing
    }
    
    @Override
    public String getName() {
        return "reserved6";
    }
    
    @Override
    public int getId() {
        return RESERVED6;
    }
    
    @Override
    public double getHardness() {
        return 0;
    }
    
    @Override
    public double getResistance() {
        return 0;
    }
    
    @Override
    public BlockColor getColor() {
        return BlockColor.DIRT_BLOCK_COLOR;
    }
}
