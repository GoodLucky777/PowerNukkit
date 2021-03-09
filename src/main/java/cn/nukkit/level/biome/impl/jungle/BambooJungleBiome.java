package cn.nukkit.level.biome.impl.jungle;

import cn.nukkit.level.generator.populator.impl.PopulatorBamboo;
import cn.nukkit.level.generator.populator.impl.PopulatorMelon;
import cn.nukkit.level.generator.populator.impl.PopulatorPumpkin;
import cn.nukkit.level.generator.populator.impl.PopulatorTree;
import cn.nukkit.level.generator.populator.impl.tree.JungleBigTreePopulator;

/**
 * @author GoodLucky777
 */
public class BambooJungleBiome extends JungleBiome {

    public BambooJungleBiome() {
        PopulatorBamboo bamboo = new PopulatorBamboo();
        bamboo.setBaseAmount(20);
        bamboo.setRandomAmount(15);
        this.addPopulator(bamboo);
        
        // TODO: Vine Populator
        
        JungleBigTreePopulator jungleBigTrees = new JungleBigTreePopulator();
        jungleBigTrees.setBaseAmount(2);
        jungleBigTrees.setRandomAmount(1);
        this.addPopulator(jungleBigTrees);
        
        PopulatorTree oakBigTree = new PopulatorTree(BlockSapling.OAK); // TODO: Should be only Fancy (Big) Oak Tree
        oakBigTree.setBaseAmount(-1);
        oakBigTree.setRandomAmount(2);
        this.addPopulator(oakBigTree);
        
        PopulatorMelon melon = new PopulatorMelon();
        melon.setBaseAmount(-65);
        melon.setRandomAmount(70);
        this.addPopulator(melon);
        
        PopulatorPumpkin pumpkin = new PopulatorPumpkin();
        pumpkin.setBaseAmount(-65);
        pumpkin.setRandomAmount(70);
        this.addPopulator(pumpkin);
    }
    
    @Override
    public String getName() {
        return "Bamboo Jungle";
    }
}
