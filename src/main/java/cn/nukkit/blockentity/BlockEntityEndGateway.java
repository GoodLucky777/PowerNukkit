package cn.nukkit.blockentity;

import cn.nukkit.block.Block;
import cn.nukkit.blockstate.BlockState;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityEnderPearl;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.IntTag;
import cn.nukkit.nbt.tag.ListTag;

import static cn.nukkit.block.BlockID.BEDROCK;

/**
 * @author GoodLucky777
 */
public class BlockEntityEndGateway extends BlockEntitySpawnable {

    // NBT data
    private int age;
    private BlockVector3 exitPortal;
    
    // Default value
    private static final BlockVector3 defaultExitPortal = new BlockVector3(0, 0, 0);
    
    // Others
    public int teleportCooldown;
    
    private static final BlockState STATE_BEDROCK = BlockState.of(BEDROCK);
    
    public BlockEntityEndGateway(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }
    
    @Override
    protected void initBlockEntity() {
        if (this.namedTag.contains("Age")) {
            this.age = this.namedTag.getInt("Age");
        } else {
            this.age = 0;
        }
        
        if (this.namedTag.contains("ExitPortal")) {
            ListTag<IntTag> exitPortalList = this.namedTag.getList("ExitPortal", IntTag.class);
            this.exitPortal = new BlockVector3(exitPortalList.get(0).data, exitPortalList.get(1).data, exitPortalList.get(2).data);
        } else {
            this.exitPortal = this.defaultExitPortal.clone();
        }
        
        this.teleportCooldown = 0;
    }
    
    @Override
    public boolean isBlockEntityValid() {
        return this.getLevel().getBlockIdAt(getFloorX(), getFloorY(), getFloorZ()) == Block.END_GATEWAY;
    }
    
    @Override
    public void saveNBT() {
        super.saveNBT();
        
        this.namedTag.putInt("Age", this.age);
        this.namedTag.putList(new ListTag<IntTag>("ExitPortal")
            .add(new IntTag("0", this.exitPortal.x))
            .add(new IntTag("1", this.exitPortal.y))
            .add(new IntTag("2", this.exitPortal.z))
        );
    }
    
    @Override
    public boolean onUpdate() {
        if (this.closed) {
            return false;
        }
        
        this.timing.startTiming();
        
        boolean isGenerated = isGenerating();
        
        this.age++;
        
        if (teleportCooldown > 0) {
            teleportCooldown--;
            if (teleportCooldown == 0) {
                setDirty();
            }
        } else {
            if (this.age % 2400 == 0) {
                this.setTeleportCooldown();
            }
        }
        
        if (isGenerated != isGenerating()) {
            setDirty();
        }
        
        this.timing.stopTiming();
        
        return true;
    }
    
    public void teleportEntity(Entity entity) {
        if (isTeleportCooldown()) {
            return;
        }
        
        if (exitPortal != null) {
            if (entity instanceof EntityEnderPearl) {
                if (((EntityProjectile) entity).shootingEntity != null) {
                    ((EntityProjectile) entity).shootingEntity.teleport(getSafeExitPortal().asVector3(), TeleportCause.END_GATEWAY);
                } else {
                    entity.teleport(getSafeExitPortal().asVector3(), TeleportCause.END_GATEWAY);
                }
            } else {
                entity.teleport(getSafeExitPortal().asVector3(), TeleportCause.END_GATEWAY);
            }
        }
        
        setTeleportCooldown();
    }
    
    public BlockVector3 getSafeExitPortal() {
        for (int x = this.getFloorX() - 5; x <= this.getFloorX() + 5; x++) {
            for (int z = this.getFloorZ() - 5; z <= this.getFloorZ() + 5; z++) {
                for (int y = 255; y > Math.max(0, exitPortal.getY() + 2); y--) {
                    if (!this.getLevel().getBlockStateAt(x, y, z).equals(BlockState.AIR)) {
                        if (!this.getLevel().getBlockStateAt(x, y, z).equals(STATE_BEDROCK)) {
                            return new BlockVector3(x, y + 1, z);
                        }
                    }
                }
            }
        }
        return this.exitPortal.up(2);
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public BlockVector3 getExitPortal() {
        return exitPortal;
    }
    
    public void setExitPortal(BlockVector3 exitPortal) {
        this.exitPortal = exitPortal;
    }
    
    public boolean isGenerating() {
        return age < 200;
    }
    
    public boolean isTeleportCooldown() {
        return teleportCooldown > 0;
    }
    
    public void setTeleportCooldown() {
        this.setTeleportCooldown(40);
    }
    
    public void setTeleportCooldown(int teleportCooldown) {
        this.teleportCooldown = teleportCooldown;
        setDirty();
    }
}