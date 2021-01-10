package cn.nukkit.level.generator.object.end;

import cn.nukkit.blockstate.BlockState;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.generator.object.BasicGenerator;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

import static cn.nukkit.block.BlockID.CHORUS_FLOWER;

/**
 * @author GoodLucky777
 */
public class ObjectChorusTree extends BasicGenerator {

    private static final BlockState STATE_CHORUS_FLOWER = BlockState.of(CHORUS_FLOWER);
    
    public boolean generate(ChunkManager level, NukkitRandom rand, Vector3 position) {
        return this.generate(level, rand, position, 8);
    }
    
    public boolean generate(ChunkManager level, NukkitRandom rand, Vector3 position, int maxDistance) {
        level.setBlockStateAt(position.getFloorX(), position.getFloorY(), position.getFloorZ(), STATE_CHORUS_FLOWER);
        this.growImmediately(level, rand, position, maxDistance, 0);
        return true;
    }
    
    public void growImmediately(ChunkManager level, NukkitRandom random, Vector3 position, int maxDistance, int age) {
        // TODO
    }
}
