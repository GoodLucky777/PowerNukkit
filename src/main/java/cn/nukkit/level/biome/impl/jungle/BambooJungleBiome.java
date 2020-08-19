package cn.nukkit.level.biome.impl.jungle;

import cn.nukkit.level.biome.type.GrassyBiome;
import cn.nukkit.level.generator.noise.nukkit.f.SimplexF;
import cn.nukkit.level.generator.populator.impl.PopulatorBamboo;
import cn.nukkit.level.generator.populator.impl.PopulatorMelon;
import cn.nukkit.level.generator.populator.impl.tree.JungleBigTreePopulator;

/**
 * @author good777LUCKY
 */
public class BambooJungleBiome extends GrassyBiome {

    private static final SimplexF gravelNoise = new SimplexF(new NukkitRandom(0), 1f, 1 / 4f, 1 / 64f);
    
    public BambooJungleBiome() {
        super();
        
        BambooPopulator bigTrees = new BambooPopulator();
        bamboo.setBaseAmount(8);
        this.addPopulator(bamboo);
        
        JungleBigTreePopulator bigTrees = new JungleBigTreePopulator();
        bigTrees.setBaseAmount(3);
        this.addPopulator(bigTrees);
        
        PopulatorMelon melon = new PopulatorMelon();
        melon.setBaseAmount(-65);
        melon.setRandomAmount(70);
        this.addPopulator(melon);
    }
    
    @Override
    public int getSurfaceId(int x, int y, int z) {
        return gravelNoise.noise2D(x, z, true) < -0.75f ? PODZOL << Block.DATA_BITS : super.getSurfaceId(x, y, z);
    }
    
    @Override
    public String getName() {
        return "Bamboo Jungle";
    }
}
