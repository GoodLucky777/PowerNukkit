package cn.nukkit.level.biome.generator;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.utils.Cubiomes;

/**
 * @author GoodLucky777
 */
@PowerNukkitOnly
@Since("FUTURE")
public class NetherBiomeGenerator {

    private static final int CURRENT_VERSION = 17;
    
    private long seed;
    
    public NetherBiomeGenerator(long seed) {
        this.seed = seed;
    }
    
    public int[] generateBiomeIds(int chunkX, int chunkZ) {
        int[] ids = new int[16 * 16];
        Cubiomes.getInstance().genNetherScaled(CURRENT_VERSION, seed, 1, ids, chunkX, chunkZ, 16, 16, 0, 0);
        return ids;
    }
}
