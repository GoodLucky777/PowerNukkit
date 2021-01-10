package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.IntBlockProperty;
import cn.nukkit.event.block.BlockGrowEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class BlockChorusFlower extends BlockTransparentMeta {

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public static final IntBlockProperty AGE = new IntBlockProperty("age", false, 5);
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public static final BlockProperties PROPERTIES = new BlockProperties(AGE);
    
    public BlockChorusFlower() {
        this(0);
    }
    
    public BlockChorusFlower(int meta) {
        super(meta);
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @Nonnull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }
    
    @Override
    public int getId() {
        return CHORUS_FLOWER;
    }

    @Override
    public String getName() {
        return "Chorus Flower";
    }

    @Override
    public double getHardness() {
        return 0.4;
    }

    @Override
    public double getResistance() {
        return 0.4;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_AXE;
    }

    private boolean isPositionValid() {
        // Chorus flowers must be above end stone or chorus plant, or be above air and horizontally adjacent to exactly one chorus plant.
        // If these conditions are not met, the block breaks without dropping anything.
        Block down = down();
        if (down.getId() == CHORUS_PLANT || down.getId() == END_STONE) {
            return true;
        }
        if (down.getId() != AIR) {
            return false;
        }
        boolean foundPlant = false;
        for (BlockFace face : BlockFace.Plane.HORIZONTAL) {
            Block side = getSide(face);
            if (side.getId() == CHORUS_PLANT) {
                if (foundPlant) {
                    return false;
                }
                foundPlant = true;
            }
        }

        return foundPlant;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (!isPositionValid()) {
                this.getLevel().useBreakOn(this);
                return Level.BLOCK_UPDATE_NORMAL;
            }
        } else if (type == Level.BLOCK_UPDATE_RANDOM) {
            if (this.up().getId() == AIR && this.up().getY() < 256) {
                if (!isFullyAged()) {
                    boolean grow = false;
                    boolean ground = false;
                    if (this.down().getId() == AIR || this.down().getId() == END_STONE) {
                        grow = true;
                    } else if (this.down().getId() == CHORUS_PLANT) {
                        int height = 1;
                        for (int y = -2; y >= -6; y--) {
                            if (this.down(y).getId() == CHORUS_PLANT) {
                                height++;
                            } else {
                                if (this.down(y).getId() == END_STONE) {
                                    ground = true;
                                }
                                break;
                            }
                        }
                        
                        if (height < 2 || height <= ThreadLocalRandom.current().nextInt(ground ? 5 : 4)) {
                            grow = true;
                        }
                    }
                    
                    if (grow && this.up(2).getId() == AIR && isHorizontalEmpty()) {
                        BlockChorusFlower block = (BlockChorusFlower) this.clone();
                        BlockGrowEvent ev = new BlockGrowEvent(this, block);
                        Server.getInstance().getPluginManager().callEvent(ev);
                        
                        if (!ev.isCancelled()) {
                            this.getLevel().setBlock(this, ev.getNewState(), false, true);
                        } else {
                            return Level.BLOCK_UPDATE_RANDOM;
                        }
                    } else if (!isFullyAged()) {
                        for (int i = 0; i < ThreadLocalRandom.current().nextInt(ground ? 5 : 4); i++) {
                            BlockFace face = BlockFace.Plane.HORIZONTAL.random();
                            Block check = this.getSide(face);
                            if (check.getId() == AIR && check.down().getId() == AIR && ((BlockChorusFlower) check).isHorizontalEmptyExcept(face.getOpposite())) {
                                BlockChorusFlower block = (BlockChorusFlower) this.clone();
                                block.setAge(getAge() + 1);
                                BlockGrowEvent ev = new BlockGrowEvent(this, block);
                                Server.getInstance().getPluginManager().callEvent(ev);
                                
                                if (!ev.isCancelled()) {
                                    this.getLevel().setBlock(this, ev.getNewState(), false, true);
                                } else {
                                    return Level.BLOCK_UPDATE_RANDOM;
                                }
                            }
                        }
                    } else {
                        BlockChorusFlower block = (BlockChorusFlower) this.clone();
                        block.setAge(getMaxAge());
                        BlockGrowEvent ev = new BlockGrowEvent(this, block);
                        Server.getInstance().getPluginManager().callEvent(ev);
                        
                        if (!ev.isCancelled()) {
                            this.getLevel().setBlock(this, ev.getNewState(), false, true);
                        } else {
                            return Level.BLOCK_UPDATE_RANDOM;
                        }
                    }
                }
            } else {
                return Level.BLOCK_UPDATE_RANDOM;
            }
        }
        
        return 0;
    }

    @Override
    public boolean place(@Nonnull Item item, @Nonnull Block block, @Nonnull Block target, @Nonnull BlockFace face, double fx, double fy, double fz, @Nullable Player player) {
        if (!isPositionValid()) {
            return false;
        }
        return super.place(item, block, target, face, fx, fy, fz, player);
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[]{ this.toItem() };
    }

    @Override
    public boolean breaksWhenMoved() {
        return true;
    }

    @Override
    public boolean sticksToPiston() {
        return false;
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public int getMaxAge() {
        return AGE.getMaxValue();
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public int getAge() {
        return getIntValue(AGE);
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public void setAge(int age) {
        setIntValue(AGE, age);
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public boolean isFullyAged() {
        return getAge() >= getMaxAge();
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    private boolean isHorizontalEmpty() {
        for (BlockFace face : BlockFace.Plane.HORIZONTAL) {
            Block side = this.getSide(face);
            if (side.getId() != AIR) {
                return false;
            }
        }
        return true;
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    private boolean isHorizontalEmptyExcept(BlockFace except) {
        for (BlockFace face : BlockFace.Plane.HORIZONTAL) {
            Block side = this.getSide(face);
            if (side.getId() != AIR) {
                if (face != except) {
                    return false;
                }
            }
        }
        return true;
    }
}
