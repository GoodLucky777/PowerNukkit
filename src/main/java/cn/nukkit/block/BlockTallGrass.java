package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemSeedsWheat;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Level;
import cn.nukkit.level.particle.BoneMealParticle;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;

import javax.annotation.Nonnull;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Random;

/**
 * @author Angelic47 (Nukkit Project)
 */
public class BlockTallGrass extends BlockFlowable {

    public BlockTallGrass() {
        this(1);
    }

    public BlockTallGrass(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return TALL_GRASS;
    }

    @Override
    public String getName() {
        String[] names = new String[]{
                "Grass",
                "Grass",
                "Fern",
                "Fern"
        };
        return names[this.getDamage() & 0x03];
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean canBeReplaced() {
        return true;
    }

    @Override
    public int getBurnChance() {
        return 60;
    }

    @Override
    public int getBurnAbility() {
        return 100;
    }

    @Override
    public boolean place(@Nonnull Item item, @Nonnull Block block, @Nonnull Block target, @Nonnull BlockFace face, double fx, double fy, double fz, Player player) {
        Block down = this.down();
        if (down.getId() == Block.GRASS || down.getId() == Block.DIRT || down.getId() == Block.PODZOL) {
            this.getLevel().setBlock(block, this, true);
            return true;
        }
        return false;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (this.down().isTransparent()) {
                this.getLevel().useBreakOn(this);
                return Level.BLOCK_UPDATE_NORMAL;
            }
        }
        return 0;
    }

    @Override
    public boolean onActivate(@Nonnull Item item, Player player) {
        if (item.getId() == Item.DYE && item.getDamage() == 0x0f) {
            Block up = this.up();

            if (up.getId() == AIR) {
                int meta;

                switch (this.getDamage()) {
                    case 0:
                    case 1:
                        meta = BlockDoublePlant.TALL_GRASS;
                        break;
                    case 2:
                    case 3:
                        meta = BlockDoublePlant.LARGE_FERN;
                        break;
                    default:
                        meta = -1;
                }

                if (meta != -1) {
                    if (player != null && (player.gamemode & 0x01) == 0) {
                        item.count--;
                    }

                    this.level.addParticle(new BoneMealParticle(this));
                    this.level.setBlock(this, get(DOUBLE_PLANT, meta), true, false);
                    this.level.setBlock(up, get(DOUBLE_PLANT, meta ^ BlockDoublePlant.TOP_HALF_BITMASK), true);
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isShears()) {
            boolean dropSeeds = ThreadLocalRandom.current().nextInt(10) == 0;
            //todo enchantment
            if (dropSeeds) {
                return new Item[]{
                        new ItemSeedsWheat(),
                        Item.get(Item.TALL_GRASS, this.getDamage(), 1)
                };
            } else {
                return new Item[]{
                        Item.get(Item.TALL_GRASS, this.getDamage(), 1)
                };
            }
        }
        
        Random random = new Random();
        if (random.nextInt(7) == 0) {
            Enchantment fortune = item.getEnchantment(Enchantment.ID_FORTUNE_DIGGING);
            if (fortune != null && fortune.getLevel() >= 1) {
                int maxCount = 1 + (fortune.getLevel() * 2);
                return new Item[]{ new ItemSeedsWheat(0, 1 + random.nextInt(maxCount - 1)) };
            }
            return new Item[]{ new ItemSeedsWheat() };
        } else {
            return new Item[0];
        }
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_SHEARS;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.FOLIAGE_BLOCK_COLOR;
    }
}
