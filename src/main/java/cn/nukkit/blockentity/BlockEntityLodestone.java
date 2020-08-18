package cn.nukkit.blockentity;

import cn.nukkit.block.Block;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * @author good777LUCKY
 */
public class BlockEntityLodestone extends BlockEntitySpawnable {

    public Integer trackingHandle;
    
    public BlockEntityLodestone(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }
    
    @Override
    protected void initBlockEntity() {
        this.trackingHandle = null;
        super.initBlockEntity();
    }
    
    @Override
    public boolean isBlockEntityValid() {
        return this.getBlock().getId() == Block.LODESTONE;
    }
    
    @Override
    public void saveNBT() {
        super.saveNBT();
        if (this.trackingHandle != null) {
            this.namedTag.putInt("trackingHandle", this.trackingHandle);
        }
    }
    
    @Override
    public String getName() {
        return "Lodestone";
    }
    
    public Integer getTrackingHandle() {
        if (this.trackingHandle == null) {
            return null;
        }
        return this.namedTag.getInt("trackingHandle");
    }
    
    public void setTrackingHandle(int trackingHandle) {
        this.trackingHandle = trackingHandle;
        this.namedTag.putInt("trackingHandle", trackingHandle);
    }
    
    @Override
    public CompoundTag getSpawnCompound() {
        CompoundTag compound = new CompoundTag()
            .putString("id", BlockEntity.LODESTONE)
            .putInt("x", (int) this.x)
            .putInt("y", (int) this.y)
            .putInt("z", (int) this.z);
        if (trackingHandle != null) {
            compound.putInt("trackingHandle", getTrackingHandle());
        }
        return compound;
    }
}
