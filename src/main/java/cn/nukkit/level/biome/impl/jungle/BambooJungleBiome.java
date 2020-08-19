package cn.nukkit.level.biome.impl.jungle;

import cn.nukkit.level.biome.type.GrassyBiome;
import cn.nukkit.level.generator.populator.impl.PopulatorMelon;
import cn.nukkit.level.generator.populator.impl.tree.JungleBigTreePopulator;

/**
 * @author good777LUCKY
 */
public class BambooJungleBiome extends GrassyBiome {
    public BambooJungleBiome() {
        super();
        
        JungleBigTreePopulator bigTrees = new JungleBigTreePopulator();
        bigTrees.setBaseAmount(3);
        this.addPopulator(bigTrees);
        
        PopulatorMelon melon = new PopulatorMelon();
        melon.setBaseAmount(-65);
        melon.setRandomAmount(70);
        this.addPopulator(melon);
    }
    
    @Override
    public String getName() {
        return "Bamboo Jungle";
    }
}
