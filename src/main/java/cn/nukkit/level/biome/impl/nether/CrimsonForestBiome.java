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
public class CrimsonForestBiome extends HellBiome {

    private static final BlockState STATE_CRIMSON_NYLIUM = BlockState.of(CRIMSON_NYLIUM);
    
    @Override
    public String getName() {
        return "Crimson Forest";
    }
    
    @Override
    public BlockState getTopState() {
        return STATE_CRIMSON_NYLIUM;
    }
    
    @Override
    public BlockState getTopGravelNoiseState() {
        return STATE_CRIMSON_NYLIUM;
    }
    
    @Override
    public BlockState getTopSoulsandNoiseState() {
        return STATE_CRIMSON_NYLIUM;
    }
    
    @Override
    public boolean isForceSurface() {
        return true;
    }
}
