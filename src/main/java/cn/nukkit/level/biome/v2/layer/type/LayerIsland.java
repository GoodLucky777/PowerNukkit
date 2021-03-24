package cn.nukkit.level.biome.v2.layer.type;

import cn.nukkit.level.biome.v2.layer.Layer;
import cn.nukkit.level.biome.v2.layer.LayerHelper;

/**
 * @author GoodLucky777
 */
public class LayerIsland extends Layer {

    @Override
    public int[] generateBiomeValues(int x, int z, int width, int height) {
        int[] values;
        if (this.getParent() == null) {
            values = new int[width * height];
        } else {
            values = this.getParent().generateBiomeValues(x, z, width, height);
        }
        
        long startSeed = this.getStartSeed();
        long chunkSeed;
        
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                chunkSeed = LayerHelper.getChunkSeed(startSeed, i + x, j + z);
                values[i + (j * width)] = LayerHelper.mcFirstIsZero(chunkSeed, 10) ? 1 : 0;
            }
        }
        
        if (x > -width && x <= 0 && z > -height && z <= 0) {
            values[-x + (-z * width)] = 1;
        }
        
        return values;
    }
}
