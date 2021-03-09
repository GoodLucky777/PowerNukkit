package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.block.BlockVine;
import cn.nukkit.blockstate.BlockState;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.helper.PopulatorHelpers;
import cn.nukkit.level.generator.populator.type.PopulatorCount;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.NukkitRandom;

import java.util.HashSet;
import java.util.Set;

/**
 * @author GoodLucky777
 */
public class PopulatorVines extends PopulatorCount {

    private void generateVines(int x, int y, int z, FullChunk chunk, NukkitRandom random) {
        if (chunk.getBlockId(x, y, z) == AIR) {
            Set<BlockFace> attachFaces = new HashSet<>();
            
            if (chunk.getBlockState(x - 1, y, z).getBlock().isSolid()) {
                attachFaces.add(BlockFace.WEST);
            }
            
            if (chunk.getBlockState(x + 1, y, z).getBlock().isSolid()) {
                attachFaces.add(BlockFace.EAST);
            }
            
            if (chunk.getBlockState(x, y, z - 1).getBlock().isSolid()) {
                attachFaces.add(BlockFace.SOUTH);
            }
            
            if (chunk.getBlockState(x, y, z + 1).getBlock().isSolid()) {
                attachFaces.add(BlockFace.NORTH);
            }
            
            chunk.setBlockState(x, y, z, BlockState.of(VINE, BlockVine.getMetaFromFaces(attachFaces)));
        }
    }
    
    @Override
    protected int getHighestWorkableBlock(ChunkManager level, int x, int z, FullChunk chunk) {
        int y;
        for (y = 254; y >= 64; --y) { // Vines don't generate under Y 64
            if (!(PopulatorHelpers.isNonSolid(chunk.getBlockId(x - 1, y, z)) || PopulatorHelpers.isNonSolid(chunk.getBlockId(x + 1, y, z)) || PopulatorHelpers.isNonSolid(chunk.getBlockId(x, y, z - 1)) || PopulatorHelpers.isNonSolid(chunk.getBlockId(x, y, z + 1)))) {
                break;
            }
        }
        
        return y == 0 ? -1 : ++y;
    }
    
    @Override
    protected void populateCount(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random, FullChunk chunk) {
        int x = random.nextBoundedInt(16);
        int z = random.nextBoundedInt(16);
        int y = getHighestWorkableBlock(level, x, z, chunk);
        if (y >= 64) {
            generateVines(x, y, z, chunk, random);
        }
    }
}
