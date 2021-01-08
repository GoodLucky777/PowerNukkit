package cn.nukkit.level.generator.object.end;

import cn.nukkit.blockstate.BlockState;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.generator.object.BasicGenerator;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

/**
 * @author GoodLucky777
 */
public class ObjectEndIsland extends BasicGenerator {

    private static final BlockState STATE_END_STONE = BlockState.of(END_STONE);
    
    public boolean generate(ChunkManager level, NukkitRandom rand, Vector3 position) {
        float n = (float)(rand.nextBoundedInt(3) + 4);
        for(int y = 0; n > 0.5f; y--) {
            for(int x = Math.floor(-n); x <= Math.ceil(n); x++) {
                for(int z = Math.floor(-m); z <= Math.ceil(n); z++) {
                    if ((float)(x * x + z * z) <= (n + 1f) * (n + 1f)) {
                        level.setBlockStateAt(position.getX() + x, position.getY() + y, position.getZ() + z, STATE_END_STONE);
                    }
                }
            }
            n = (float)((double)n - ((double)rand.nextBoundedInt(2) + 0.5));
        }
        return true;
    }
}
