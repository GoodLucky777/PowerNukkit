package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityEndGateway;
import cn.nukkit.blockstate.BlockState;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.TheEnd;
import cn.nukkit.level.generator.object.end.ObjectEndGateway;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

/**
 * @author GoodLucky777
 */
public class PopulatorEndGateway extends Populator {

    private final TheEnd theEnd;
    
    private final ObjectEndGateway objectEndGateway;
    
    public PopulatorEndGateway(TheEnd theEnd) {
        this.theEnd = theEnd;
        this.objectEndGateway = new ObjectEndGateway();
    }
    
    @Override
    public void populate(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random, FullChunk chunk) {
        if ((long) chunkX * (long) chunkX + (long) chunkZ * (long) chunkZ <= 4096L) {
            return;
        }
        
        if (theEnd.getIslandHeight(chunkX, chunkZ, 1, 1) > 40f) {
            if (random.nextBoundedInt(/*700*/10) == 0) {
                int x = (chunkX << 4) + random.nextBoundedInt(16);
                int z = (chunkZ << 4) + random.nextBoundedInt(16);
                int y = this.getHighestWorkableBlock(level, x, z, chunk) + random.nextBoundedInt(7) + 3;
                
                if (y > 1 && y < 254) {
                    if (objectEndGateway.generate(level, random, new Vector3(x, y, z))) {
                        BlockEntity blockEntity = level.getChunk(chunkX, chunkZ).getTile(x & 0x0f, y & 0xff, z & 0x0f);
                        if (blockEntity != null && blockEntity instanceof BlockEntityEndGateway) {
                            ((BlockEntityEndGateway) blockEntity).setExitPortal(theEnd.getSpawn().asBlockVector3());
                        }
                    }
                }
            }
        }
    }
}
