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
    
    private static final Int2IntMap BIOME_ID_MAP = new Int2IntOpenHashMap();
    static {
        BIOME_ID_MAP.put(8, 8);
        BIOME_ID_MAP.put(170, 178);
        BIOME_ID_MAP.put(171, 179);
        BIOME_ID_MAP.put(172, 180);
        BIOME_ID_MAP.put(173, 181);
    }
    
    private long seed;
    
    public NetherBiomeGenerator(long seed) {
        this.seed = seed;
    }
    
    public int[] generateBiomeIds(int x, int z) {
        int[] ids = new int[16 * 16];
        Cubiomes.getInstance().genNetherScaled(CURRENT_VERSION, seed, 1, ids, x, z, 16, 16, 0, 0);
        return ids;
    }
    
    public static int convertBiomeId(int javaId) {
        return BIOME_ID_MAP.get(javaId);
    }
}
