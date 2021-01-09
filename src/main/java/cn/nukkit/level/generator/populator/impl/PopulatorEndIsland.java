package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.blockstate.BlockState;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.TheEnd;
import cn.nukkit.level.generator.object.end.ObjectEndIsland;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

/**
 * @author GoodLucky777
 */
public class PopulatorEndIsland extends Populator {

    private final TheEnd theEnd;
    
    private final ObjectEndIsland objectEndIsland;
    
    private static final BlockState STATE_END_STONE = BlockState.of(END_STONE);
    
    public PopulatorEndIsland(TheEnd theEnd) {
        this.theEnd = theEnd;
        this.objectEndIsland = new ObjectEndIsland();
    }
    
    @Override
    public void populate(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random, FullChunk chunk) {
        if (chunkX * chunkX + chunkZ * chunkZ <= 4096) {
            return;
        }
        
        if (random.nextBoundedInt(14) == 0) {
            float height = theEnd.getIslandHeight(chunkX, chunkZ, 1, 1);
            
            if (height < -20f) {
                Vector3 position = new Vector3(chunkX << 4, 0, chunkZ << 4);
                
                objectEndIsland.generate(level, random, position.add(8 + random.nextBoundedInt(16), 55 + random.nextBoundedInt(16), 8 + random.nextBoundedInt(16)));
                if (random.nextBoundedInt(4) == 0) {
                    objectEndIsland.generate(level, random, position.add(8 + random.nextBoundedInt(16), 55 + random.nextBoundedInt(16), 8 + random.nextBoundedInt(16)));
                }
            }
        }
    }
}
