/*package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.block.BlockID;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.type.PopulatorCount;
import cn.nukkit.math.NukkitRandom;

/**
 * @author GoodLucky777
 * /
public class PopulatorGroupBlock extends PopulatorCount {

    private final BlockState block;
    private final int tries;
    
    public PopulatorGroupBlock(BlockState block, int tries) {
        this.block = block;
        this.tries = tries;
    }
    
    @Override
    public void populateCount(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random, FullChunk chunk) {
        int x = (chunkX << 4) | random.nextBoundedInt(16);
        int z = (chunkZ << 4) | random.nextBoundedInt(16);
        int y = this.getHighestWorkableBlock(level, x, z, chunk);
        if (y != -1) {
            this.generate(level, x, y, z);
        }
    }
    
    @Override
    protected int getHighestWorkableBlock(ChunkManager level, int x, int z, FullChunk chunk) {
        int y;
        x &= 0xF;
        z &= 0xF;
        
        for (y = 127; y > 0; --y) {
            int b = chunk.getBlockId(x, y, z);
            if (b == BlockID.NETHERRACK) {
                break;
            } else if (b != Block.AIR) {
                return -1;
            }
        }
        
        return ++y;
    }
    
    public void generate(ChunkManager level, int x, int y, int z) {
        
    }
}*/
