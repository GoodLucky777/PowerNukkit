package cn.nukkit.level.biome.impl.jungle;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSapling;
import cn.nukkit.level.biome.type.GrassyBiome;
import cn.nukkit.level.generator.noise.nukkit.f.SimplexF;
import cn.nukkit.level.generator.populator.impl.PopulatorBamboo;
import cn.nukkit.level.generator.populator.impl.PopulatorMelon;
import cn.nukkit.level.generator.populator.impl.PopulatorTree;
import cn.nukkit.level.generator.populator.impl.tree.JungleBigTreePopulator;
import cn.nukkit.math.NukkitRandom;

/**
 * @author good777LUCKY
 */
public class BambooJungleBiome extends GrassyBiome {

    private static final SimplexF podzolNoise = new SimplexF(new NukkitRandom(0), 1f, 1 / 4f, 1 / 64f);
    
    public BambooJungleBiome() {
        super();
        
        PopulatorBamboo bamboo = new PopulatorBamboo();
        bamboo.setBaseAmount(8);
        this.addPopulator(bamboo);
        
        JungleBigTreePopulator bigTrees = new JungleBigTreePopulator();
        bigTrees.setBaseAmount(3);
        this.addPopulator(bigTrees);
        
        PopulatorTree trees = new PopulatorTree(BlockSapling.OAK);
        trees.setBaseAmount(1);
        this.addPopulator(trees);
        
        PopulatorMelon melon = new PopulatorMelon();
        melon.setBaseAmount(-65);
        melon.setRandomAmount(70);
        this.addPopulator(melon);
    }
    
    @Override
    public int getSurfaceId(int x, int y, int z) {
        return podzolNoise.noise2D(x, z, true) < -0.75f ? PODZOL << Block.DATA_BITS : super.getSurfaceId(x, y, z);
    }
    
    @Override
    public String getName() {
        return "Bamboo Jungle";
    }
}
