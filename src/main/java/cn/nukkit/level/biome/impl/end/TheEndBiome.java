package cn.nukkit.level.biome.impl.end;

import cn.nukkit.level.biome.Biome;

/**
 * @author GoodLucky777
 */
public class TheEndBiome extends Biome {

    public TheEndBiome() {
        this.setBaseHeight(0.1f);
        this.setHeightVariation(0.2f);
    }
    
    @Override
    public String getName() {
        return "The End";
    }

    @Override
    public boolean canRain() {
        return false;
    }
}
