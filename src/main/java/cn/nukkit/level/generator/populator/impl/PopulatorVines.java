package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.block.Block;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.helper.EnsureBelow;
import cn.nukkit.level.generator.populator.helper.EnsureCover;
import cn.nukkit.level.generator.populator.helper.EnsureGrassBelow;
import cn.nukkit.level.generator.populator.type.PopulatorSurfaceBlock;
import cn.nukkit.math.NukkitRandom;

/**
 * @author GoodLucky777
 */
public class PopulatorVines extends PopulatorSurfaceBlock {

    @Override
    protected boolean canStay(int x, int y, int z, FullChunk chunk) {
        return EnsureCover.ensureCover(x, y, z, chunk) && (EnsureGrassBelow.ensureGrassBelow(x, y, z, chunk) || EnsureBelow.ensureBelow(x, y, z, DIRT, chunk) || EnsureBelow.ensureBelow(x, y, z, PODZOL, chunk));
    }
    
    @Override
    protected int getBlockId(int x, int z, NukkitRandom random, FullChunk chunk) {
        return VINE << Block.DATA_BITS;
    }
    
    @Override
    protected void placeBlock(int x, int y, int z, int id, FullChunk chunk, NukkitRandom random) {
        
    }
}
