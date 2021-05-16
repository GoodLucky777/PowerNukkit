package cn.nukkit.entity.projectile;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityExplosive;
import cn.nukkit.event.entity.EntityExplosionPrimeEvent;
import cn.nukkit.level.Explosion;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * @author GoodLucky777
 */
public class EntityGrenadeFrag extends EntityProjectile implements EntityExplosive {

    public static final int NETWORK_ID = 600;
    
    public EntityGrenadeFrag(FullChunk chunk, CompoundTag nbt) {
        this(chunk, nbt, null);
    }
    
    public EntityGrenadeFrag(FullChunk chunk, CompoundTag nbt, Entity shootingEntity) {
        super(chunk, nbt, shootingEntity);
    }
    
    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }
    
    @Override
    public float getWidth() {
        return 0.3f;
    }
    
    @Override
    public float getLength() {
        return 0.3f;
    }
    
    @Override
    public float getHeight() {
        return 0.3f;
    }
    
    @Override
    protected float getGravity() {
        return 0.04f;
    }
    
    @Override
    protected float getDrag() {
        return 0.015f;
    }
    
    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }
        
        this.timing.startTiming();
        
        boolean hasUpdate = super.onUpdate(currentTick);
        
        if (this.age > 1200 || this.isCollided) {
            this.close();
        }
        
        this.timing.stopTiming();
        
        return hasUpdate;
    }
    
    @Override
    protected void onCollideWithBlock(Position position, Vector3 motion) {
        this.explode();
        this.close();
    }
    
    @Override
    public void onCollideWithEntity(Entity entity) {
        this.explode();
        this.close();
    }
    
    @Override
    public void explode() {
        EntityExplosionPrimeEvent event = new EntityExplosionPrimeEvent(this, 5);
        this.server.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            Explosion explosion = new Explosion(this.getPosition(), event.getForce(), this);
            explosion.explodeB();
        }
    }
}
