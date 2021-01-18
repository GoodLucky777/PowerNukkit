package cn.nukkit.level.generator.structure.Structure;

import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.IntTag;
import cn.nukkit.nbt.tag.ListTag;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GoodLucky777
 */
public class StructureTemplate {

    private BlockVector3 size = new BlockVector3(0, 0, 0); // This is not a position, this is for a structure size
    private List<StructureTemplate.BlockEntry> blocks = new ArrayList<StructureTemplate.BlockEntry>();
    private List<StructureTemplate.EntityEntry> entities = new ArrayList<StructureTemplate.EntityEntry>();
    
    public void loadStructure(CompoundTag compoundTag) {
        // Clear previous data
        this.blocks.clear();
        this.entities.clear();
        
        // Load size
        ListTag<IntTag> sizeList = compoundTag.getList("size", IntTag.class);
        this.size = new BlockVector3(sizeList.get(0).data, sizeList.get(1).data, sizeList.get(2).data);
        
        // Load palette
        ListTag<CompoundTag> paletteList = compoundTag.getList("palette", CompoundTag.class);
        for (int i = 0; i < paletteList.size(); i++) {
            CompoundTag paletteTag = paletteList.get(i);
            
        }
        
        // Load blocks
        ListTag<CompoundTag> blocksList = compoundTag.getList("blocks", CompoundTag.class);
        for (int i = 0; i < blocksList.size(); i++) {
            CompoundTag blockTag = blocksList.get(i);
            if (blockTag.contains("pos") && blockTag.contains("state")) {
                ListTag<IntTag> posTag = blockTag.getList("pos", IntTag.class);
                BlockVector3 pos = new BlockVector3(posTag.get(0).data, posTag.get(1).data, posTag.get(2).data);
                int state = blockTag.getInt("state");
                CompoundTag nbt = blockTag.contains("nbt") ? blockTag.getCompound("nbt") : null;
                this.blocks.add(new StructureTemplate.BlockEntry(pos, state, nbt));
            }
        }
        
        // Load entities
        ListTag<CompoundTag> entitiesList = compoundTag.getList("entities", CompoundTag.class);
        for (int i = 0; i < entitiesList.size(); i++) {
            CompoundTag entityTag = entitiesList.get(i);
            if (entityTag.contains("pos") && entityTag.contains("blockPos") && entityTag.contains("nbt")) {
                ListTag<DoubleTag> posTag = entityTag.getList("pos", DoubleTag.class);
                Vector3 pos = new Vector3(posTag.get(0).data, posTag.get(1).data, posTag.get(2).data);
                ListTag<IntTag> blockPosTag = entityTag.getList("blockPos", IntTag.class);
                BlockVector3 blockPos = new BlockVector3(blockPosTag.get(0).data, blockPosTag.get(1).data, blockPosTag.get(2).data);
                CompoundTag nbt = entityTag.getCompound("nbt");
                this.entities.add(new StructureTemplate.EntityEntry(pos, blockPos, nbt));
            }
        }
    }
    
    private static class BlockEntry {
    
        private final BlockVector3 pos;
        private final int state;
        private final CompoundTag nbt;
        
        public BlockEntry(BlockVector3 pos, int state, CompoundTag nbt) {
            this.pos = pos;
            this.state = state;
            this.nbt = nbt;
        }
    }
    
    private static class EntityEntry {
    
        private final Vector3 pos;
        private final BlockVector3 blockPos;
        private final CompoundTag nbt;
        
        public EntityEntry(Vector3 pos, BlockVector3 blockPos, CompoundTag nbt) {
            this.pos = pos;
            this.blockPos = blockPos;
            this.nbt = nbt;
        }
    }
}
