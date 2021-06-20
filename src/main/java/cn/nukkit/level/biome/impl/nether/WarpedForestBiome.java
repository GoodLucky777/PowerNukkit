package cn.nukkit.level.biome.impl.nether;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.blockstate.BlockState;
import cn.nukkit.level.biome.impl.HellBiome;

/**
 * @author GoodLucky777
 */
@PowerNukkitOnly
@Since("FUTURE")
public class WarpedForestBiome extends HellBiome {

    private static final BlockState STATE_WARPED_NYLIUM = BlockState.of(WARPED_NYLIUM);
    
    @Override
    public String getName() {
        return "Warped Forest";
    }
    
    @Override
    public BlockState getGroundState() {
        return STATE_WARPED_NYLIUM;
    }
    
    @Override
    public BlockState getTopState() {
        return STATE_WARPED_NYLIUM;
    }
    
    @Override
    public BlockState getTopStateGravelNoise() {
        return STATE_WARPED_NYLIUM;
    }
    
    @Override
    public BlockState getTopStateSoulsandNoise() {
        return STATE_WARPED_NYLIUM;
    }
}
