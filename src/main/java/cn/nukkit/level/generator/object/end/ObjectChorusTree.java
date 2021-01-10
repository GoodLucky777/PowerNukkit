package cn.nukkit.level.generator.object.end;

import cn.nukkit.blockstate.BlockState;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.generator.object.BasicGenerator;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

import static cn.nukkit.block.BlockID.AIR;
import static cn.nukkit.block.BlockID.CHORUS_FLOWER;
import static cn.nukkit.block.BlockID.CHORUS_PLANT;

/**
 * @author GoodLucky777
 */
public class ObjectChorusTree extends BasicGenerator {

    private static final BlockState STATE_CHORUS_FLOWER = BlockState.of(CHORUS_FLOWER);
    private static final BlockState STATE_CHORUS_PLANT = BlockState.of(CHORUS_PLANT);
    
    public boolean generate(ChunkManager level, NukkitRandom rand, Vector3 position) {
        return this.generate(level, rand, position, 8);
    }
    
    public boolean generate(ChunkManager level, NukkitRandom rand, Vector3 position, int maxDistance) {
        level.setBlockStateAt(position.getFloorX(), position.getFloorY(), position.getFloorZ(), STATE_CHORUS_PLANT);
        this.growImmediately(level, rand, position, maxDistance, 0);
        return true;
    }
    
    public void growImmediately(ChunkManager level, NukkitRandom random, Vector3 position, int maxDistance, int age) {
        int height = 1 + random.nextBoundedInt(4);
        if (age == 0) {
            height++;
        }
        
        for (int y = 1; y <= height; y++) {
            if (!this.isHorizontalAir(level, position.add(0, y, 0))) {
                break;
            }
            level.setBlockStateAt(position.getFloorX(), position.getFloorY() + y, position.getFloorZ(), STATE_CHORUS_PLANT);
        }
        
        // TODO
    }
    
    private boolean isHorizontalAir(ChunkManager level, Vector3 vector3) {
        for (BlockFace face : BlockFace.Plane.HORIZONTAL) {
            Vector3 side = vector3.getSide(face);
            if (level.getBlockIdAt(side.getFloorX(), side.getFloorY(), side.getFloorZ()) != AIR) {
                return false;
            }
        }
        return true;
    }
}
