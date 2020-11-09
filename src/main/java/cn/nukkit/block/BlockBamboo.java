package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.api.DeprecationDetails;
import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.blockproperty.ArrayBlockProperty;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.BlockProperty;
import cn.nukkit.blockproperty.BooleanBlockProperty;
import cn.nukkit.blockproperty.value.BambooLeafSize;
import cn.nukkit.blockproperty.value.BambooStalkThickness;
import cn.nukkit.event.block.BlockGrowEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.item.ItemID;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Level;
import cn.nukkit.level.Sound;
import cn.nukkit.level.particle.BoneMealParticle;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.MathHelper;
import cn.nukkit.network.protocol.AnimatePacket;
import cn.nukkit.utils.BlockColor;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;

public class BlockBamboo extends BlockTransparentMeta {

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public static final BlockProperty<BambooLeafSize> BAMBOO_LEAF_SIZE = new ArrayBlockProperty<>("bamboo_leaf_size", false, BambooLeafSize.class);
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public static final BooleanBlockProperty AGE_BIT = new BooleanBlockProperty("age_bit", false);
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public static final BlockProperty<BambooStalkThickness> BAMBOO_STALK_THICKNESS = new ArrayBlockProperty<>("bamboo_stalk_thickness", false, BambooStalkThickness.class);
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public static final BlockProperties PROPERTIES = new BlockProperties(BAMBOO_LEAF_SIZE, AGE_BIT, BAMBOO_STALK_THICKNESS);
    
    @Deprecated
    @DeprecationDetails(since = "1.4.0.0-PN", replaceWith = "BambooLeafSize.NO_LEAVES", reason = "Use the new BlockProperty system instead")
    public static final int LEAF_SIZE_NONE = 0;
    
    @Deprecated
    @DeprecationDetails(since = "1.4.0.0-PN", replaceWith = "BambooLeafSize.SMALL_LEAVES", reason = "Use the new BlockProperty system instead")
    public static final int LEAF_SIZE_SMALL = 1;
    
    @Deprecated
    @DeprecationDetails(since = "1.4.0.0-PN", replaceWith = "BambooLeafSize.LARGE_LEAVES", reason = "Use the new BlockProperty system instead")
    public static final int LEAF_SIZE_LARGE = 2;

    public BlockBamboo() {
        this(0);
    }

    public BlockBamboo(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return BAMBOO;
    }

    @Override
    public String getName() {
        return "Bamboo";
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @Nonnull
    @Override
    public BlockProperties getProperties() {
        return BlockBamboo.PROPERTIES;
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public BambooLeafSize getBambooLeafSize() {
        return getPropertyValue(BAMBOO_LEAF_SIZE);
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public void setBambooLeafSize(BambooLeafSize bambooLeafSize) {
        setPropertyValue(BAMBOO_LEAF_SIZE, bambooLeafSize);
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public boolean getAgeBit() {
        return getBooleanValue(AGE_BIT);
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public void setAgeBit(boolean ageBit) {
        setBooleanValue(AGE_BIT, ageBit);
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public BambooStalkThickness getBambooStalkThickness() {
        return getPropertyValue(BAMBOO_STALK_THICKNESS);
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public boolean isBambooStalkThick() {
        return getPropertyValue(BAMBOO_STALK_THICKNESS) == BambooStalkThickness.THICK;
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public void setBambooStalkThickness(BambooStalkThickness bambooStalkThickness) {
        setPropertyValue(BAMBOO_STALK_THICKNESS, bambooStalkThickness);
    }
    
    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (isSupportInvalid()) {
                level.scheduleUpdate(this, 0);
            }
            return type;
        } else if (type == Level.BLOCK_UPDATE_SCHEDULED) {
            level.useBreakOn(this, null, null, true);
        } else if (type == Level.BLOCK_UPDATE_RANDOM) {
            Block up = up();
            if (getAge() == 0 && up.getId() == AIR && level.getFullLight(up) >= BlockCrops.MINIMUM_LIGHT_LEVEL && ThreadLocalRandom.current().nextInt(3) == 0) {
                grow(up);
            }
            return type;
        }
        return 0;
    }

    public boolean grow(Block up) {
        BlockBamboo newState = new BlockBamboo();
        if (isBambooStalkThick()) {
            newState.setBambooStalkThickness(BambooStalkThickness.THICK);
            newState.setBambooLeafSize(BambooLeafSize.LARGE_LEAVES);
        } else {
            newState.setBambooLeafSize(BambooLeafSize.SMALL_LEAVES);
        }
        BlockGrowEvent blockGrowEvent = new BlockGrowEvent(up, newState);
        level.getServer().getPluginManager().callEvent(blockGrowEvent);
        if (!blockGrowEvent.isCancelled()) {
            Block newState1 = blockGrowEvent.getNewState();
            newState1.x = x;
            newState1.y = up.y;
            newState1.z = z;
            newState1.level = level;
            newState1.place(toItem(), up, this, BlockFace.DOWN, 0.5, 0.5, 0.5, null);
            return true;
        }
        return false;
    }

    public int countHeight() {
        int count = 0;
        Optional<Block> opt;
        Block down = this;
        while ((opt = down.down().firstInLayers(b-> b.getId() == BAMBOO)).isPresent()) {
            down = opt.get();
            if (++count >= 16) {
                break;
            }
        }
        return count;
    }

    @Override
    public boolean place(@Nonnull Item item, @Nonnull Block block, @Nonnull Block target, @Nonnull BlockFace face, double fx, double fy, double fz, Player player) {
        Block down = down();
        int downId = down.getId();
        if (downId != BAMBOO && downId != BAMBOO_SAPLING) {
            BlockBambooSapling sampling = new BlockBambooSapling();
            sampling.x = x;
            sampling.y = y;
            sampling.z = z;
            sampling.level = level;
            return sampling.place(item, block, target, face, fx, fy, fz, player);
        }

        boolean canGrow = true;

        if (downId == BAMBOO_SAPLING) {
            if (player != null) {
                AnimatePacket animatePacket = new AnimatePacket();
                animatePacket.action = AnimatePacket.Action.SWING_ARM;
                animatePacket.eid = player.getId();
                this.getLevel().addChunkPacket(player.getChunkX(), player.getChunkZ(), animatePacket);
            }
            setBambooLeafSize(BambooLeafSize.SMALL_LEAVES);
        } if (down instanceof BlockBamboo) {
            BlockBamboo bambooDown = (BlockBamboo) down;
            canGrow = bambooDown.getAge() == 0;
            boolean thick = bambooDown.isBambooStalkThick();
            if (!thick) {
                boolean setThick = true;
                for (int i = 2; i <= 3; i++) {
                    if (getSide(BlockFace.DOWN, i).getId() != BAMBOO) {
                        setThick = false;
                    }
                }
                if (setThick) {
                    setBambooStalkThickness(BambooStalkThickness.THICK);
                    setBambooLeafSize(BambooLeafSize.LARGE_LEAVES);
                    bambooDown.setBambooLeafSize(BambooLeafSize.SMALL_LEAVES);
                    bambooDown.setBambooStalkThickness(BambooStalkThickness.THICK);
                    bambooDown.setAgeBit(true);
                    this.level.setBlock(bambooDown, bambooDown, false, true);
                    while ((down = down.down()) instanceof BlockBamboo) {
                        bambooDown = (BlockBamboo) down;
                        bambooDown.setBambooStalkThickness(BambooStalkThickness.THICK);
                        bambooDown.setBambooLeafSize(BambooLeafSize.NO_LEAVES);
                        bambooDown.setAgeBit(true);
                        this.level.setBlock(bambooDown, bambooDown, false, true);
                    }
                } else {
                    setBambooLeafSize(BambooLeafSize.SMALL_LEAVES);
                    bambooDown.setAgeBit(true);
                    this.level.setBlock(bambooDown, bambooDown, false, true);
                }
            } else {
                setBambooStalkThickness(BambooStalkThickness.THICK);
                setBambooLeafSize(BambooLeafSize.LARGE_LEAVES);
                setAgeBit(false);
                bambooDown.setBambooLeafSize(BambooLeafSize.LARGE_LEAVES);
                bambooDown.setAgeBit(true);
                this.level.setBlock(bambooDown, bambooDown, false, true);
                down = bambooDown.down();
                if (down instanceof BlockBamboo) {
                    bambooDown = (BlockBamboo) down;
                    bambooDown.setBambooLeafSize(BambooLeafSize.SMALL_LEAVES);
                    bambooDown.setAgeBit(true);
                    this.level.setBlock(bambooDown, bambooDown, false, true);
                    down = bambooDown.down();
                    if (down instanceof BlockBamboo) {
                        bambooDown = (BlockBamboo) down;
                        bambooDown.setBambooLeafSize(BambooLeafSize.NO_LEAVES);
                        bambooDown.setAgeBit(true);
                        this.level.setBlock(bambooDown, bambooDown, false, true);
                    }
                }
            }
        } else if (isSupportInvalid()) {
            return false;
        }

        int height = canGrow? countHeight() : 0;
        if (!canGrow || height >= 15 || height >= 11 && ThreadLocalRandom.current().nextFloat() < 0.25F) {
            setAgeBit(true);
        }

        this.level.setBlock(this, this, false, true);
        return true;
    }

    @Override
    public boolean onBreak(Item item) {
        Optional<Block> down = down().firstInLayers(b-> b instanceof BlockBamboo);
        if (down.isPresent()) {
            BlockBamboo bambooDown = (BlockBamboo) down.get();
            int height = bambooDown.countHeight();
            if (height < 15 && (height < 11 || !(ThreadLocalRandom.current().nextFloat() < 0.25F))) {
                bambooDown.setAgeBit(false);
                this.level.setBlock(bambooDown, bambooDown.layer, bambooDown, false, true);
            }
        }
        return super.onBreak(item);
    }

    @Override
    public boolean canPassThrough() {
        return true;
    }

    private boolean isSupportInvalid() {
        int downId = down().getId();
        return downId != BAMBOO && downId != DIRT && downId != GRASS && downId != SAND && downId != GRAVEL && downId != PODZOL && downId != BAMBOO_SAPLING;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(new BlockBamboo());
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.FOLIAGE_BLOCK_COLOR;
    }

    @Override
    public double getHardness() {
        return 2;
    }

    @Override
    public double getResistance() {
        return 5;
    }
    
    @Deprecated
    @DeprecationDetails(since = "1.4.0.0-PN", reason = "Checking magic value directly is depreacated")
    public boolean isThick() {
        return (getDamage() & 0x1) == 0x1;
    }
    
    @Deprecated
    @DeprecationDetails(since = "1.4.0.0-PN", reason = "Checking magic value directly is depreacated")
    public void setThick(boolean thick) {
        setDamage(getDamage() & (DATA_MASK ^ 0x1) | (thick? 0x1 : 0x0));
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_AXE;
    }
    
    @Deprecated
    @DeprecationDetails(since = "1.4.0.0-PN", reason = "Checking magic value directly is depreacated")
    public int getLeafSize() {
        return (getDamage() >> 1) & 0x3;
    }
    
    @Deprecated
    @DeprecationDetails(since = "1.4.0.0-PN", reason = "Checking magic value directly is depreacated")
    public void setLeafSize(int leafSize) {
        leafSize = MathHelper.clamp(leafSize, LEAF_SIZE_NONE, LEAF_SIZE_LARGE) & 0b11;
        setDamage(getDamage() & (DATA_MASK ^ 0b110) | (leafSize << 1));
    }

    @Override
    public double getBreakTime(Item item, Player player) {
        double breakTime = super.getBreakTime(item, player);

        if (item.isSword()) {
            breakTime /= 30;
        }

        return breakTime;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(@Nonnull Item item, Player player) {
        boolean itemIsBoneMeal = item.getId() == ItemID.DYE && item.getDamage() == 0x0F; //Bonemeal
        if (itemIsBoneMeal || item.getBlock() != null && item.getBlockId() == BlockID.BAMBOO) {
            int top = (int) y;
            int count = 1;

            for (int i = 1; i <= 16; i++) {
                int id = this.level.getBlockIdAt(this.getFloorX(), this.getFloorY() - i, this.getFloorZ());
                if (id == BAMBOO) {
                    count++;
                } else {
                    break;
                }
            }

            for (int i = 1; i <= 16; i++) {
                int id = this.level.getBlockIdAt(this.getFloorX(), this.getFloorY() + i, this.getFloorZ());
                if (id == BAMBOO) {
                    top++;
                    count++;
                } else {
                    break;
                }
            }

            if (itemIsBoneMeal && count >= 15) {
                return false;
            }

            boolean success = false;

            Block block = this.up(top - (int)y + 1);
            if (block.getId() == BlockID.AIR) {
                success = grow(block);
            }

            if (success) {
                if (player != null && player.isSurvival()) {
                    item.count--;
                }
                if (itemIsBoneMeal) {
                    level.addParticle(new BoneMealParticle(this));
                } else {
                    level.addSound(block, Sound.BLOCK_BAMBOO_PLACE, 0.8F, 1.0F);
                }
            }

            return true;
        }
        return false;
    }
    
    @Deprecated
    @DeprecationDetails(since = "1.4.0.0-PN", reason = "Checking magic value directly is depreacated")
    public int getAge() {
        return (getDamage() & 0x8) >> 3;
    }
    
    @Deprecated
    @DeprecationDetails(since = "1.4.0.0-PN", reason = "Checking magic value directly is depreacated")
    public void setAge(int age) {
        age = MathHelper.clamp(age, 0, 1) << 3;
        setDamage(getDamage() & (DATA_MASK ^ 0b1000) | age);
    }
}
