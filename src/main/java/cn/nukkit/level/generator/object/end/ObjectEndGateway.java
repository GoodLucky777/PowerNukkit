package cn.nukkit.level.generator.object.end;

import cn.nukkit.blockstate.BlockState;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.generator.object.BasicGenerator;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

import static cn.nukkit.block.BlockID.BEDROCK;
import static cn.nukkit.block.BlockID.END_GATEWAY;

/**
 * @author GoodLucky777
 */
public class ObjectEndGateway extends BasicGenerator {

    private static final BlockState STATE_BEDROCK = BlockState.of(BEDROCK);
    private static final BlockState STATE_END_GATEWAY = BlockState.of(END_GATEWAY);
    
    public boolean generate(ChunkManager level, NukkitRandom rand, Vector3 position) {
        for (int x = position.getFloorX() - 1; x <= position.getFloorX() + 1; x++) {
            for (int z = position.getFloorZ() - 1; z <= position.getFloorZ() + 1; z++) {
                for (int y = position.getFloorY() - 2; y <= position.getFloorY() + 2; y++) {
                    boolean flagX = position.getFloorX() == x;
                    boolean flagY = position.getFloorY() == y;
                    boolean flagZ = position.getFloorZ() == z;
                    boolean flagFar = Math.abs(y - position.getFloorY()) == 2;
                    
                    if (flagX && flagY && flagZ) {
                        level.setBlockStateAt(x, y, z, STATE_END_GATEWAY);
                    } else if (flagX && flagZ && flagFar) {
                        level.setBlockStateAt(x, y, z, STATE_BEDROCK);
                    } else if (!flagFar && (flagX || flagZ)) {
                        level.setBlockStateAt(x, y, z, STATE_BEDROCK);
                    } else if (flagY) {
                        level.setBlockStateAt(x, y, z, BlockState.AIR);
                    } else {
                        level.setBlockStateAt(x, y, z, BlockState.AIR);
                    }
                }
            }
        }
        return true;
    }
}
