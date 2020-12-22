package cn.nukkit.entity.item;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.inventory.InventoryMoveItemEvent;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.MinecartHopperInventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.utils.MinecartType;

public class EntityMinecartHopper extends EntityMinecartAbstract implements InventoryHolder {

    public static final int NETWORK_ID = 96;

    protected MinecartHopperInventory inventory;
    
    private int transferCooldown;
    
    private boolean enabled; // TODO: Use Definitions

    public EntityMinecartHopper(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        setDisplayBlock(Block.get(Block.HOPPER_BLOCK), false);
    }

    @Override
    public String getName() {
        return getType().getName();
    }

    @Override
    public MinecartType getType() {
        return MinecartType.valueOf(5);
    }

    @Override
    public boolean isRideable(){
        return false;
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public void dropItem() {
        super.dropItem();

        this.level.dropItem(this, Item.get(Item.HOPPER));
        for (Item item : this.inventory.getContents().values()) {
            this.level.dropItem(this, item);
        }
        this.inventory.clearAll();
    }

    @Override
    public boolean mountEntity(Entity entity, byte mode) {
        return false;
    }

    @Override
    public boolean onInteract(Player player, Item item, Vector3 clickedPos) {
        player.addWindow(this.inventory);
        return false; // If true, the count of items player has in hand decreases
    }

    @Override
    public MinecartHopperInventory getInventory() {
        return inventory;
    }

    @Override
    public void initEntity() {
        super.initEntity();
        
        this.inventory = new MinecartHopperInventory(this);
        if (this.namedTag.contains("Items") && this.namedTag.get("Items") instanceof ListTag) {
            ListTag<CompoundTag> inventoryList = this.namedTag.getList("Items", CompoundTag.class);
            for (CompoundTag item : inventoryList.getAll()) {
                this.inventory.setItem(item.getByte("Slot"), NBTIO.getItemHelper(item));
            }
        }
        
        this.dataProperties
                .putByte(DATA_CONTAINER_TYPE, 11)
                .putInt(DATA_CONTAINER_BASE_SIZE, this.inventory.getSize())
                .putInt(DATA_CONTAINER_EXTRA_SLOTS_PER_STRENGTH, 0);
        
        if (this.namedTag.contains("TransferCooldown")) {
            this.transferCooldown = this.namedTag.getInt("TransferCooldown");
        } else {
            this.transferCooldown = 8;
        }
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        this.namedTag.putList(new ListTag<CompoundTag>("Items"));
        if (this.inventory != null) {
            for (int slot = 0; slot < 5; ++slot) {
                Item item = this.inventory.getItem(slot);
                if (item != null && item.getId() != Item.AIR) {
                    this.namedTag.getList("Items", CompoundTag.class)
                            .add(NBTIO.putItemHelper(item, slot));
                }
            }
        }
        
        this.namedTag.putInt("TransferCooldown", this.transferCooldown);
    }
    
    @Since("1.4.0.0-PN")
    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }
        
        boolean update = super.onUpdate(currentTick);
        
        if (this.isAlive() && this.enabled) {
            if (!isOnTransferCooldown()) {
                if (pickupItems()) {
                    this.transferCooldown = 4;
                } else {
                    this.transferCooldown = 0;
                }
            } else {
                this.transferCooldown--;
            }
        }
        
        return update;
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public boolean isEnabled() {
        return enabled;
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public int getTransferCooldown() {
        return transferCooldown;
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public boolean isOnTransferCooldown() {
        return this.transferCooldown > 0;
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public void setTransferCooldown(int transferCooldown) {
        this.transferCooldown = transferCooldown;
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public boolean pickupItems() {
        if (this.inventory.isFull()) {
            return false;
        }

        boolean pickedUpItem = false;

        for (Entity entity : this.level.getCollidingEntities(this.getBoundingBox().grow(0.25, 0, 0.25))) {
            if (entity.isClosed() || !(entity instanceof EntityItem)) {
                continue;
            }

            EntityItem itemEntity = (EntityItem) entity;
            Item item = itemEntity.getItem();

            if (item.isNull()) {
                continue;
            }
            
            int originalCount = item.getCount();

            if (!this.inventory.canAddItem(item)) {
                continue;
            }

            InventoryMoveItemEvent ev = new InventoryMoveItemEvent(null, this.inventory, this, item, InventoryMoveItemEvent.Action.PICKUP);
            this.server.getPluginManager().callEvent(ev);

            if (ev.isCancelled()) {
                continue;
            }

            Item[] items = this.inventory.addItem(item);

            if (items.length == 0) {
                entity.close();
                pickedUpItem = true;
                continue;
            }

            if (items[0].getCount() != originalCount) {
                pickedUpItem = true;
                item.setCount(items[0].getCount());
            }
        }
        
        return pickedUpItem;
    }
}
