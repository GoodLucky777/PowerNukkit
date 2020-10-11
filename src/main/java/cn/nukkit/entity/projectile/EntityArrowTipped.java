package cn.nukkit.entity.projectile;

import cn.nukkit.entity.Entity;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.potion.Effect;

import java.util.ArrayList;

/**
 * @author good777LUCKY
 */
public class EntityArrowTipped extends EntityArrow {

    ListTag<CompoundTag> mobEffects = new ArrayList<>();
    
    public EntityArrowTipped(FullChunk chunk, CompoundTag nbt) {
        this(chunk, nbt);
    }
    
    @Override
    protected void initEntity() {
        super.initEntity();
        
        if (this.namedTag.contains("mobEffects")) {
            this.mobEffects =  namedTag.getList("mobEffects", CompoundTag.class);
        }
    }
    
    @Override
    public void saveNBT() {
        super.saveNBT();

        this.namedTag.putList("mobEffects", this.mobEffects);
    }
    
    public void addMobEffect(Effect effect) {
        this.mobEffects.add(new CompoundTag("")
            .putByte("Ambient", (byte) (effect.isAmbient() ? 1 : 0))
            .putByte("Amplifier", (byte) effect.getAmplifier())
            .putByte("DisplayOnScreenTextureAnimation", (byte) 0) // TODO: What is this?
            .putInt("Duration", effect.getDuration())
            .putInt("DurationEasy", effect.getDuration())
            .putInt("DurationNormal", effect.getDuration())
            .putInt("DurationHard", effect.getDuration())
            .putByte("Id", (byte) effect.getId())
            .putByte("ShowParticles", (byte) (effect.isVisible() ? 1 : 0)));
    }
}
