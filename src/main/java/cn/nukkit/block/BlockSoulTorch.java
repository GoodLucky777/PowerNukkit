package cn.nukkit.block;

public class BlockSoulTorch extends BlockTorch {
    //private static final BlockProperty<SoulTorchFace> SOUL_TORCH_FACING_DIRECTION = new ArrayBlockProperty<>("torch_facing_direction", false, SoulTorchFace.class);
    //public static final BlockProperties PROPERTIES = new BlockProperties(SOUL_TORCH_FACING_DIRECTION);

    public BlockSoulTorch() {
        this(0);
    }

    public BlockSoulTorch(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return "Soul Torch";
    }

    @Override
    public int getId() {
        return SOUL_TORCH;
    }

    @Override
    public int getLightLevel() {
        return 10;
    }
/*
    @Override
    public TorchFace getTorchFace() {
        switch (getPropertyValue(SOUL_TORCH_FACING_DIRECTION)) {
            default:
            case UNKNOWN:
                return TorchFace.UNKNOWN;
            case WEST:
                return TorchFace.EAST;
            case EAST:
                return TorchFace.WEST;
            case NORTH:
                return TorchFace.SOUTH;
            case SOUTH:
                return TorchFace.NORTH;
            case TOP:
                return TorchFace.TOP;
        }
    }

    @Override
    public void setTorchFace(TorchFace face) {
        SoulTorchFace soulTorchFace;
        switch (face) {
            default:
            case UNKNOWN:
                soulTorchFace = SoulTorchFace.UNKNOWN;
                break;
            case EAST:
                soulTorchFace = SoulTorchFace.WEST;
                break;
            case WEST:
                soulTorchFace = SoulTorchFace.EAST;
                break;
            case SOUTH:
                soulTorchFace = SoulTorchFace.NORTH;
                break;
            case NORTH:
                soulTorchFace = SoulTorchFace.SOUTH;
                break;
            case TOP:
                soulTorchFace = SoulTorchFace.TOP;
                break;
        }
        setPropertyValue(SOUL_TORCH_FACING_DIRECTION, soulTorchFace);
    }

    *//**
     * Mojang messed up.
     *//*
    private enum SoulTorchFace {
        UNKNOWN, WEST, EAST, NORTH, SOUTH, TOP;
    }*/
    
}
