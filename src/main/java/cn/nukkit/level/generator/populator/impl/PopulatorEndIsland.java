package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.blockstate.BlockState;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;

/**
 * @author GoodLucky777
 */
public class PopulatorEndIsland extends Populator {

    private static final BlockState STATE_END_STONE = BlockState.of(END_STONE);
    
    @Override
    public void populate(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random, FullChunk chunk) {
        if (chunkX * chunkX + chunkZ * chunkZ <= 4096) {
            return;
        }
        
        if (random.nextBoundedInt(14) == 0) {
            float height = getIslandHeight(chunkX, chunkZ, 1, 1);
            if (height < -20f) {
                //
                if (random.nextBoundedInt(4) == 0) {
                    //
                }
            }
        }
    }
}
