package cn.nukkit.entity.item;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.inventory.CommandBlockInventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.MinecartType;

/**
 * @author GoodLucky777
 */
public class EntityMinecartCommandBlock extends EntityMinecartAbstract {

    public static final int NETWORK_ID = 100;
    
    public EntityMinecartCommandBlock(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        setDisplayBlock(Block.get(Block.REPEATING_COMMAND_BLOCK), false);
    }
    
    @Override
    public String getName() {
        return this.getType().getName();
    }
    
    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }
    
    @Override
    public MinecartType getType() {
        return MinecartType.valueOf(6);
    }
    
    @Override
    public void initEntity() {
        super.initEntity();

        // TODO
    }
    
    @Override
    public boolean isRideable(){
        return false;
    }
    
    @Override
    public boolean mountEntity(Entity entity, byte mode) {
        return false;
    }
    
    @Override
    public boolean onInteract(Player player, Item item, Vector3 clickedPos) {
        if (player.canUseCommandBlock()) {
            player.addWindow(new CommandBlockInventory(player.getUIInventory(), this));
        }
        
        return false;
    }
    
    @Override
    public void saveNBT() {
        super.saveNBT();

        // TODO
    }
}
