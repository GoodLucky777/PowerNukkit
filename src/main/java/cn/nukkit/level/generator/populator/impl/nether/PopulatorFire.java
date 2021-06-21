package cn.nukkit.level.generator.populator.impl.nether;

import cn.nukkit.blockstate.BlockState;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;

import java.util.List;

/**
 * @author GoodLucky777
 */
public class PopulatorFire extends Populator {

    private final BlockState fire;
    private final int amount;
    private final List<BlockState> belowState;
    
    public PopulatorFire(BlockState fire, int amount, List<BlockState> belowState) {
        this.fire = fire;
        this.amount = amount;
        this.belowState = belowState;
    }
    
    @Override
    public void populate(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random, FullChunk chunk) {
        int x = (chunkX << 4) + random.nextBoundedInt(16);
        int z = (chunkZ << 4) + random.nextBoundedInt(16);
        int y = random.nextBoundedInt(128);
        
        for (int i = 0; i < amount; i++) {
            x += random.nextBoundedInt(8) - random.nextBoundedInt(8);
            z += random.nextBoundedInt(8) - random.nextBoundedInt(8);
            y += random.nextBoundedInt(4) - random.nextBoundedInt(4);
            
            if (y < 128 && level.getBlockIdAt(x, y, z) == AIR) {
                for (BlockState state : this.belowState) {
                    if (level.getBlockStateAt(x, y, z).equals(state)) {
                        level.setBlockStateAt(x, y, z, fire);
                    }
                }
            }
        }
    }
}
