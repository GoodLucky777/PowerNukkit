package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockBamboo;
import cn.nukkit.blockstate.BlockState;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.helper.EnsureBelow;
import cn.nukkit.level.generator.populator.helper.EnsureCover;
import cn.nukkit.level.generator.populator.helper.EnsureGrassBelow;
import cn.nukkit.level.generator.populator.type.PopulatorSurfaceBlock;
import cn.nukkit.math.NukkitRandom;

/**
 * @author GoodLucky777
 */
public class PopulatorBamboo extends PopulatorSurfaceBlock {

    private static final BlockState STATE_PODZOL = BlockState.of(PODZOL);
    // TODO: Use BlockState if BlockBamboo implement BlockState
    private static final BlockBamboo BLOCK_BAMBOO = new BlockBamboo();
    private static final BlockBamboo BLOCK_BAMBOO_DEFAULT = (BlockBamboo) BLOCK_BAMBOO.clone();
    static { BLOCK_BAMBOO_DEFAULT.setThick(true); }
    private static final BlockBamboo BLOCK_BAMBOO_LEAF_SMALL = (BlockBamboo) BLOCK_BAMBOO_DEFAULT.clone();
    private static final BlockBamboo BLOCK_BAMBOO_LEAF_LARGE = (BlockBamboo) BLOCK_BAMBOO_DEFAULT.clone();
    static {
        BLOCK_BAMBOO_LEAF_SMALL.setLeafSize(BlockBamboo.LEAF_SIZE_SMALL);
        BLOCK_BAMBOO_LEAF_LARGE.setLeafSize(BlockBamboo.LEAF_SIZE_LARGE);
    }
    private static final BlockBamboo BLOCK_BAMBOO_LEAF_LARGE_AGED = (BlockBamboo) BLOCK_BAMBOO_LEAF_LARGE.clone();
    static { BLOCK_BAMBOO_LEAF_LARGE_AGED.setAge(1); }
    
    private double podzolProbability = 0.2;
    
    @Override
    protected boolean canStay(int x, int y, int z, FullChunk chunk) {
        return EnsureCover.ensureCover(x, y, z, chunk) && (EnsureGrassBelow.ensureGrassBelow(x, y, z, chunk) || EnsureBelow.ensureBelow(x, y, z, DIRT, chunk) || EnsureBelow.ensureBelow(x, y, z, PODZOL, chunk));
    }
    
    private void generateBamboo(int x, int y, int z, FullChunk chunk, NukkitRandom random) {
        final int height = getMaxHeight(x, y, z, chunk, random.nextBoundedInt(12) + 5);
        
        for (int i = y; i < height; i++) {
            if (i > (height - 3)) {
                if (i > (height - 2)) {
                    if (i == (height - 1)) {
                        chunk.setBlock(x, y + i, z, BLOCK_BAMBOO_LEAF_LARGE_AGED);
                    } else {
                        chunk.setBlock(x, y + i, z, BLOCK_BAMBOO_LEAF_LARGE);
                    }
                } else {
                    chunk.setBlock(x, y + i, z, BLOCK_BAMBOO_LEAF_SMALL);
                }
            } else {
                chunk.setBlock(x, y + i, z, BLOCK_BAMBOO_DEFAULT);
            }
        }
    }
    
    private void generatePodzol(int x, int y, int z, FullChunk chunk, NukkitRandom random) {
        int radius = random.nextBoundedInt(4) + 1;
        
        for (int podzolX = x - radius; podzolX <= x + radius; podzolX++) {
            for (int podzolZ = z - radius; podzolZ <= z + radius; podzolZ++) {
                if ((podzolX - x) * (podzolX - x) + (podzolZ - z) * (podzolZ - z) <= radius * radius) {
                    int checkId = chunk.getBlockId(x, y - 1, z);
                    
                    if (checkId == GRASS || checkId == DIRT) {
                        chunk.setBlockState(x, y - 1, z, STATE_PODZOL);
                    }
                }
            }
        }
    }
    
    @Override
    protected int getBlockId(int x, int z, NukkitRandom random, FullChunk chunk) {
        return BAMBOO << Block.DATA_BITS;
    }
    
    private int getMaxHeight(int x, int y, int z, FullChunk chunk, int height) {
        int maxHeight = 0;
        for (int i = 0; i < height; i++) {
            if (chunk.getBlockId(x, (y + i), z) == AIR) {
                maxHeight++;
            } else {
                break;
            }
        }
        
        return maxHeight;
    }
    
    @Override
    protected void placeBlock(int x, int y, int z, int id, FullChunk chunk, NukkitRandom random) {
        if (random.nextDouble() < this.podzolProbability) {
            generatePodzol(x, y, z, chunk, random);
        }
        
        generateBamboo(x, y, z, chunk, random);
    }
    
    public void setPodzolProbability(double podzolProbability) {
        this.podzolProbability = podzolProbability;
    }
}
