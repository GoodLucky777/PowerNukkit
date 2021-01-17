package cn.nukkit.level.generator.structure.Structure;

import cn.nukkit.math.BlockVector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.IntTag;
import cn.nukkit.nbt.tag.ListTag;

/**
 * @author GoodLucky777
 */
public class StructureTemplate {

    private BlockVector3 size = new BlockVector3(0, 0, 0);
    
    public void loadStructure(CompoundTag compoundTag) {
        // Load size
        ListTag<IntTag> sizeList = compoundTag.getList("size", IntTag.class);
        this.size = new BlockVector3(sizeList.get(0).data, sizeList.get(1).data, sizeList.get(2).data);
        
        // Load palette
        
        // Load blocks
        
        // Load entities
        ListTag<CompoundTag> entitiesList = compoundTag.getList("entities", CompoundTag.class);
        for (int i = 0; entitiesList.size(); i++) {
            CompoundTag entityTag = entitiesList.get(i);
            if (entityTag.contains("pos") && entityTag.contains("blockPos") && entityTag.contains("nbt")) {
                ListTag<DoubleTag> pos = entityTag.getList("pos", DoubleTag.class);
                ListTag<IntTag> blockPos = entityTag.getList("blockPos", IntTag.class);
                
            }
        }
    }
}
