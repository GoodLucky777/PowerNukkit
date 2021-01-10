package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.blockstate.BlockState;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.TheEnd;
//import cn.nukkit.level.generator.object.end.ObjectChorusTree;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

import static cn.nukkit.block.BlockID.AIR;

/**
 * @author GoodLucky777
 */
public class PopulatorEndGateway extends Populator {

    private final TheEnd theEnd;
    
    //private final ObjectChorusTree objectChorusTree;
    
    public PopulatorEndGateway(TheEnd theEnd) {
        this.theEnd = theEnd;
        //this.objectChorusTree = new ObjectChorusTree();
    }
    
    @Override
    public void populate(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random, FullChunk chunk) {
        if ((long) chunkX * (long) chunkX + (long) chunkZ * (long) chunkZ <= 4096L) {
            return;
        }
        
        if (theEnd.getIslandHeight(chunkX, chunkZ, 1, 1) > 40f) {
            if (random.nextBoundedInt(700) == 0) {
                // TODO
            }
        }
    }
}
