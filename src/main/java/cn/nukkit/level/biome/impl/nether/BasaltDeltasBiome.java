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
public class BasaltDeltasBiome extends HellBiome {

    private static final BlockState STATE_BASALT = BlockState.of(BASALT);
    private static final BlockState STATE_BLACKSTONE = BlockState.of(BLACKSTONE);
    
    @Override
    public String getName() {
        return "Basalt Deltas";
    }
    
    @Override
    public BlockState getGroundState() {
        return STATE_BASALT;
    }
    
    @Override
    public BlockState getTopState() {
        return STATE_BASALT;
    }
    
    @Override
    public BlockState getTopGravelNoiseState() {
        return STATE_BLACKSTONE;
    }
    
    @Override
    public BlockState getTopSoulsandNoiseState() {
        return STATE_BLACKSTONE;
    }
}
