package cn.nukkit.level.biome.impl;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.blockstate.BlockState;
import cn.nukkit.level.biome.Biome;

public class HellBiome extends Biome {

    private static final BlockState STATE_LAVA = BlockState.of(STILL_LAVA);
    private static final BlockState STATE_NETHERRACK = BlockState.of(NETHERRACK);
    
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
    public BlockState getSurfaceTopState() {
        return STATE_NETHERRACK;
    }
    
    @PowerNukkitOnly
    @Since("FUTURE")
    public BlockState getGroundState() {
        return STATE_NETHERRACK;
    }
    
    @PowerNukkitOnly
    @Since("FUTURE")
    public BlockState getSeaState() {
        return STATE_LAVA;
    }
}
