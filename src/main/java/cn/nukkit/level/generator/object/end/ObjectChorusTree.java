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
        // TODO
        return true;
    }
}
