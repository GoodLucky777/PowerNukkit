package cn.nukkit.level.biome.impl;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.blockstate.BlockState;
import cn.nukkit.level.biome.Biome;

public class HellBiome extends Biome {

    private static final BlockState STATE_GRAVEL = BlockState.of(GRAVEL);
    private static final BlockState STATE_NETHERRACK = BlockState.of(NETHERRACK);
    private static final BlockState STATE_SOULSAND = BlockState.of(SOUL_SAND);
    
    @Override
    public String getName() {
        return "Hell";
    }

    @Override
    public boolean canRain() {
        return false;
    }

    @Since("1.4.0.0-PN")
    @PowerNukkitOnly
    @Override
    public boolean isDry() {
        return true;
    }
    
    @PowerNukkitOnly
    @Since("FUTURE")
    public BlockState getGroundState() {
        return STATE_NETHERRACK;
    }
    
    @PowerNukkitOnly
    @Since("FUTURE")
    public BlockState getTopState() {
        return STATE_NETHERRACK;
    }
    
    @PowerNukkitOnly
    @Since("FUTURE")
    public BlockState getTopStateGravelNoise() {
        return STATE_GRAVEL;
    }
    
    @PowerNukkitOnly
    @Since("FUTURE")
    public BlockState getTopStateSoulsandNoise() {
        return STATE_SOULSAND;
    }
    
    @PowerNukkitOnly
    @Since("FUTURE")
    public boolean isForceSurface() {
        return false;
    }
}
