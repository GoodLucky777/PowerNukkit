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
public class SoulSandValleyBiome extends HellBiome {

    private static final BlockState STATE_GRAVEL = BlockState.of(GRAVEL);
    private static final BlockState STATE_SOULSAND = BlockState.of(SOUL_SAND);
    private static final BlockState STATE_SOUL_SOIL = BlockState.of(SOUL_SOIL);
    
    @Override
    public String getName() {
        return "Soul Sand Valley";
    }
    
    @Override
    public BlockState getGroundState() {
        return STATE_SOULSAND;
    }
    
    @Override
    public BlockState getTopState() {
        return STATE_SOULSAND;
    }
    
    @Override
    public BlockState getTopGravelNoiseState() {
        return STATE_SOUL_SOIL;
    }
    
    @Override
    public BlockState getTopSoulsandNoiseState() {
        return STATE_SOUL_SOIL;
    }
    
    @Override
    public BlockState getCeilingState() {
        return STATE_SOULSAND;
    }
    
    @Override
    public BlockState getBeachState() {
        return STATE_GRAVEL;
    }
}
