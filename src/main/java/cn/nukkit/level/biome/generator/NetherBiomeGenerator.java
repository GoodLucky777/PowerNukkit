package cn.nukkit.level.biome.generator;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.utils.Cubiomes;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

/**
 * @author GoodLucky777
 */
@PowerNukkitOnly
@Since("FUTURE")
public class NetherBiomeGenerator {

    private static final int CURRENT_VERSION = 17;
    
    private static final Int2IntMap BIOME_ID_MAP = new Int2IntOpenHashMap()
        .put(8, 8)
        .put(170, 178)
        .put(171, 179)
        .put(172, 180)
        .put(173, 181);
    
    private long seed;
    
    public NetherBiomeGenerator(long seed) {
        this.seed = seed;
    }
    
    public int[] generateBiomeIds(int chunkX, int chunkZ) {
        int[] ids = new int[16 * 16];
        Cubiomes.getInstance().genNetherScaled(CURRENT_VERSION, seed, 1, ids, chunkX, chunkZ, 16, 16, 0, 0);
        return ids;
    }
    
    public static int convertBiomeId(int javaId) {
        return BIOME_ID_MAP.get(javaId);
    }
}
