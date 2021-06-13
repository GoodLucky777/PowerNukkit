package cn.nukkit.level.biome.impl.extremehills;

import cn.nukkit.block.BlockSapling;
import cn.nukkit.level.biome.type.GrassyBiome;
import cn.nukkit.level.generator.populator.impl.PopulatorOreV2;
import cn.nukkit.level.generator.populator.impl.PopulatorTree;

/**
 * @author DaPorkchop_ (Nukkit Project)
 * <p>
 * make sure this is touching another extreme hills type or it'll look dumb
 *
 * steep mountains with flat areas between
 */
public class ExtremeHillsBiome extends GrassyBiome {
    public ExtremeHillsBiome() {
        this(true);
    }

    public ExtremeHillsBiome(boolean tree) {
        super();
        
        PopulatorOreV2 oreEmerald = new PopulatorOreV2(new OreV2(8, ImmutableList.of(new OreV2.ReplaceRule(BlockState.of(BlockID.EMERALD_ORE), ImmutableList.of(STATE_STONE)))), 1, 0, 16, 0, 32, 0, 16); // Emerald Ore               
        this.addPopulator(oreEmerald);
        
        if (tree) {
            PopulatorTree trees = new PopulatorTree(BlockSapling.SPRUCE);
            trees.setBaseAmount(2);
            trees.setRandomAmount(2);
            this.addPopulator(trees);
        }

        this.setBaseHeight(1f);
        this.setHeightVariation(0.5f);
    }

    @Override
    public String getName() {
        return "Extreme Hills";
    }

    @Override
    public boolean doesOverhang() {
        return true;
    }
}
