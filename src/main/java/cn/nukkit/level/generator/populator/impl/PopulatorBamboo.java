package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.helper.EnsureBelow;
import cn.nukkit.level.generator.populator.helper.EnsureCover;
import cn.nukkit.level.generator.populator.helper.EnsureGrassBelow;
import cn.nukkit.level.generator.populator.type.PopulatorSurfaceBlock;
import cn.nukkit.math.NukkitRandom;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author good777LUCKY
 */
public class PopulatorBamboo extends PopulatorSurfaceBlock {

    private boolean findSpace(int x, int y, int z, Level level) {
        int count = 0;
        for (int i = x - 1; i < (x + 2); i++) {
            if (level.getBlockIdAt(i, y, z) == Block.AIR) {
                count++;
            }
        }
        for (int j = z - 1; j < (z + 2); j++) {
            if (level.getBlockIdAt(x, y, j) == Block.AIR) {
                count++;
            }
        }
        return (count == 4);
    }
    
    @Override
    protected boolean canStay(int x, int y, int z, FullChunk chunk) {
        return EnsureCover.ensureCover(x, y, z, chunk) && (EnsureGrassBelow.ensureGrassBelow(x, y, z, chunk) || EnsureBelow.ensureBelow(x, y, z, PODZOL, chunk)) && findSpace(x, y, z, chunk.getProvider().getLevel());
    }
    
    @Override
    protected int getBlockId(int x, int z, NukkitRandom random, FullChunk chunk) {
        return (BAMBOO << Block.DATA_BITS) | 1;
    }
    
    @Override
    protected void placeBlock(int x, int y, int z, int id, FullChunk chunk, NukkitRandom random) {
        int height = ThreadLocalRandom.current().nextInt(10) + 5;
        for (int i = 0; i < height; i++) {
            if (i >= (height - 3)) {
                if (i == (height - 1)) {
                    chunk.setFullBlockId(x, y + i, z, (BAMBOO << Block.DATA_BITS) | 5);
                }
                chunk.setFullBlockId(x, y + i, z, (BAMBOO << Block.DATA_BITS) | 3);
            }
            chunk.setFullBlockId(x, y + i, z, id);
        }
    }
}
