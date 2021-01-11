package cn.nukkit.level.generator.object.end;

import cn.nukkit.blockstate.BlockState;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.generator.object.BasicGenerator;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

import static cn.nukkit.block.BlockID.BEDROCK;

/**
 * @author GoodLucky777
 */
public class ObjectEndGateway extends BasicGenerator {

    private static final BlockState STATE_BEDROCK = BlockState.of(BEDROCK);
    
    public boolean generate(ChunkManager level, NukkitRandom rand, Vector3 position) {
        // TODO
        return true;
    }
}
