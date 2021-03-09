package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.block.BlockVine;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.helper.PopulatorHelpers;
import cn.nukkit.level.generator.populator.type.PopulatorCount;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.NukkitRandom;

import java.util.Set;

/**
 * @author GoodLucky777
 */
public class PopulatorVines extends PopulatorCount {

    @Override
    protected int getHighestWorkableBlock(ChunkManager level, int x, int z, FullChunk chunk) {
        int y;
        for (y = 254; y >= 64; --y) { // Vines don't generate under Y 64
            if (!(PopulatorHelpers.isNonSolid(chunk.getBlockId(x - 1, y, z)) || PopulatorHelpers.isNonSolid(chunk.getBlockId(x + 1, y, z)) || PopulatorHelpers.isNonSolid(chunk.getBlockId(x, y, z - 1)) || PopulatorHelpers.isNonSolid(chunk.getBlockId(x, y, z + 1)) || PopulatorHelpers.isNonSolid(chunk.getBlockId(x, y + 1, z)))) {
                break;
            }
        }
        
        return y == 0 ? -1 : ++y;
    }
    
    @Override
    protected void placeBlock(int x, int y, int z, int id, FullChunk chunk, NukkitRandom random) {
        if (chunk.getBlockId(x, y, z) == AIR) {
            Set<BlockFace> attachFaces = new Set<BlockFace>;
            
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
            
            chunk.setBlockState(BlockState.of(VINE, BlockVine.getMetaFromFaces(attachFaces)));
        }
    }
}
