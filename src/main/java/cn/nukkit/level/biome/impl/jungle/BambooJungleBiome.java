package cn.nukkit.level.biome.impl.jungle;

import cn.nukkit.block.BlockSapling;
import cn.nukkit.level.generator.populator.impl.PopulatorBamboo;
import cn.nukkit.level.generator.populator.impl.PopulatorTree;
import cn.nukkit.level.generator.populator.impl.PopulatorVines;
import cn.nukkit.level.generator.populator.impl.tree.JungleBigTreePopulator;

/**
 * @author GoodLucky777
 */
public class BambooJungleBiome extends JungleBiome {

    public BambooJungleBiome() {
        PopulatorBamboo bamboo = new PopulatorBamboo();
        bamboo.setBaseAmount(20);
        bamboo.setRandomAmount(25);
        this.addPopulator(bamboo);
        
        JungleBigTreePopulator jungleBigTrees = new JungleBigTreePopulator();
        jungleBigTrees.setBaseAmount(1);
        jungleBigTrees.setRandomAmount(2);
        this.addPopulator(jungleBigTrees);
        
        /*PopulatorTree oakBigTree = new PopulatorTree(BlockSapling.OAK); // TODO: Should be only Fancy (Big) Oak Tree
        oakBigTree.setBaseAmount(-1);
        oakBigTree.setRandomAmount(2);
        this.addPopulator(oakBigTree);*/
        
        PopulatorVines vines = new PopulatorVines();
        vines.setBaseAmount(3);
        vines.setRandomAmount(2);
        this.addPopulator(vines);
    }
    
    @Override
    public String getName() {
        return "Bamboo Jungle";
    }
}
