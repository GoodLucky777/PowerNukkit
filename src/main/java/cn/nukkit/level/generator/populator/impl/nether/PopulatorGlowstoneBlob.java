package cn.nukkit.level.generator.populator.impl.nether;

import cn.nukkit.blockstate.BlockState;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.NukkitRandom;

/**
 * @author GoodLucky777
 */
public class PopulatorGlowstoneBlob extends Populator {

    private static final BlockState STATE_GLOWSTONE = BlockState.of(GLOWSTONE);
    
    private final int amount;
    
    public PopulatorGlowstoneBlob() {
        this(10);
    }
    
    public PopulatorGlowstoneBlob(int amount) {
        this.amount = amount;
    }
    
    @Override
    public void populate(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random, FullChunk chunk) {
        for (int i = 0; i < amount; i++) {
            int x = (chunkX << 4) + random.nextBoundedInt(16);
            int z = (chunkZ << 4) + random.nextBoundedInt(16);
            int y = random.nextBoundedInt(128);
            
            if (level.getBlockId(x, y, z) == AIR) {
                int upId = level.getBlockIdAt(x, y + 1, z);
                if (upId == NETHERRACK || upId == BASALT || upId == BLACKSTONE) {
                    level.setBlockStateAt(x, y, z, STATE_GLOWSTONE);
                    
                    for (int j = 0; j < 1500; j++) {
                        int gX = x + random.nextBoundedInt(8) - random.nextBoundedInt(8);
                        int gZ = z + random.nextBoundedInt(8) - random.nextBoundedInt(8);
                        int gY = y - random.nextBoundedInt(12);
                        
                        int gCount = 0; // TODO: Find better way
                        if (level.getBlockIdAt(gX - 1, gY, gZ) == GLOWSTONE) {
                            gCount++;
                        }
                        if (level.getBlockIdAt(gX + 1, gY, gZ) == GLOWSTONE) {
                            gCount++;
                        }
                        if (level.getBlockIdAt(gX, gY - 1, gZ) == GLOWSTONE) {
                            gCount++;
                        }
                        if (level.getBlockIdAt(gX, gY + 1, gZ) == GLOWSTONE) {
                            gCount++;
                        }
                        if (level.getBlockIdAt(gX, gY, gZ - 1) == GLOWSTONE) {
                            gCount++;
                        }
                        if (level.getBlockIdAt(gX, gY, gZ + 1) == GLOWSTONE) {
                            gCount++;
                        }
                        
                        if (gCount == 1) {
                            level.setBlockStateAt(gX, gY, gZ, STATE_GLOWSTONE);
                        }
                    }
                }
            }
        }
    }
}
