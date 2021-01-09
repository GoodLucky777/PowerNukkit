package cn.nukkit.level.biome.impl.end;

import cn.nukkit.level.biome.Biome;
import cn.nukkit.level.generator.populator.impl.PopulatorEndIsland;

/**
 * @author GoodLucky777
 */
public class TheEndBiome extends Biome {

    public TheEndBiome() {
        PopulatorEndIsland populatorEndIsland = new PopulatorEndIsland();
        this.addPopulator(populatorEndIsland);
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
