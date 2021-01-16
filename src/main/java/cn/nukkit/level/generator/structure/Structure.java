package cn.nukkit.level.generator.structure.Structure;

import cn.nukkit.math.BlockVector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.IntTag;
import cn.nukkit.nbt.tag.ListTag;

/**
 * @author GoodLucky777
 */
public class Structure {

    private BlockVector3 size = new BlockVector3(0, 0, 0);
    
    public void loadStructure(CompoundTag compoundTag) {
        // Load size
        ListTag<IntTag> sizeList = compoundTag.getList("size", IntTag.class);
        this.size = new BlockVector3(sizeList.get(0).data, sizeList.get(1).data, sizeList.get(2).data);
        
        
    }
}
