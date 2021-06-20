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

    private static final BlockState STATE_SOULSAND = BlockState.of(SOUL_SAND);
    private static final BlockState STATE_SOULSOIL = BlockState.of(SOULSOIL);
    
    @Override
    public String getName() {
        return "Soul Sand Valley";
    }
    
    @Override
    public BlockState getGroundState() {
        return STATE_SOULSAND;
    }
    
    @Override
    public BlockState getTopStateGravelNoise() {
        return STATE_SOULSOIL;
    }
    
    @Override
    public BlockState getTopStateSoulsandNoise() {
        return STATE_SOULSOIL;
    }
}
