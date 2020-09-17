package cn.nukkit.blockstate;

import cn.nukkit.api.DeprecationDetails;
import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.BlockProperty;
import cn.nukkit.blockproperty.exception.InvalidBlockPropertyValueException;
import cn.nukkit.blockstate.exception.InvalidBlockStateDataTypeException;
import cn.nukkit.blockstate.exception.InvalidBlockStateException;
import cn.nukkit.level.Level;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.utils.OptionalBoolean;
import lombok.Getter;
import lombok.ToString;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

@PowerNukkitOnly
@Since("1.4.0.0-PN")
@ToString
@ParametersAreNonnullByDefault
public final class BlockState implements Serializable, IBlockState {
    private static final long serialVersionUID = 623759888114628578L;
    private static final ConcurrentMap<String, BlockState> STATES = new ConcurrentHashMap<>();
    public static final BlockState AIR = BlockState.of(BlockID.AIR, 0);
    private static final BigInteger BYTE_MASK = BigInteger.valueOf(0xFF);
    private static final BigInteger INT_MASK = BigInteger.valueOf(0xFFFFFFFFL);
    private static final BigInteger LONG_MASK = new BigInteger("FFFFFFFFFFFFFFFF", 16);


    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @Nonnull
    public static BlockState of(int blockId) {
        return STATES.computeIfAbsent(blockId+":0", k-> new BlockState(blockId));
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @Nonnull
    public static BlockState of(int blockId, int blockData) {
        return STATES.computeIfAbsent(blockId+":"+blockData, k-> new BlockState(blockId, blockData));
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @Nonnull
    public static BlockState of(int blockId, long blockData) {
        return STATES.computeIfAbsent(blockId+":"+blockData, k-> new BlockState(blockId, blockData));
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @Nonnull
    public static BlockState of(int blockId, BigInteger blockData) {
        return STATES.computeIfAbsent(blockId+":"+blockData, k-> new BlockState(blockId, blockData));
    }

    /**
     * @throws InvalidBlockStateDataTypeException If the {@code blockData} param is not {@link Integer}, {@link Long},
     * or {@link BigInteger}.
     */
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @Nonnull
    public static BlockState of(int blockId, Number blockData) {
        return STATES.computeIfAbsent(blockId+":"+blockData, l-> {
            if (blockData instanceof Byte) {
                return new BlockState(blockId, blockData.byteValue()); 
            } else if (blockData instanceof Integer) {
                return new BlockState(blockId, blockData.intValue());
            } else if (blockData instanceof Long) {
                return new BlockState(blockId, blockData.longValue());
            } else if (blockData instanceof BigInteger) {
                return new BlockState(blockId, (BigInteger) blockData);
            } else {
                throw new InvalidBlockStateDataTypeException(blockData);
            }
        });
    }
    
    @Getter
    private final int blockId;
    @Nonnull
    private final Storage storage;
    @ToString.Exclude
    @Nonnull
    private OptionalBoolean valid = OptionalBoolean.empty();

    private BlockState(int blockId) {
        this.blockId = blockId;
        storage = new ZeroStorage();
    }

    private BlockState(int blockId, byte blockData) {
        this.blockId = blockId;
        storage = blockData == 0?   new ZeroStorage() : 
                                    new ByteStorage(blockData);
    }
    
    private BlockState(int blockId, int blockData) {
        this.blockId = blockId;
        storage = blockData == 0?   new ZeroStorage() : 
                blockData < 0?      new IntStorage(blockData) :
                blockData <= 0xFF?  new ByteStorage((byte)blockData) : 
                                    new IntStorage(blockData);
    }
    
    private BlockState(int blockId, long blockData) {
        this.blockId = blockId;
        storage = blockData == 0?   new ZeroStorage() : 
                blockData < 0?      new LongStorage(blockData) :
                blockData <= 0xFF?  new ByteStorage((byte)blockData) :
                blockData <= 0xFFFFFFFFL? new IntStorage((int)blockData) : 
                                    new LongStorage(blockData);
    }
    
    private BlockState(int blockId, BigInteger blockData) {
        this.blockId = blockId;
        int zeroCmp = BigInteger.ZERO.compareTo(blockData);
        if (zeroCmp == 0) {
            storage = new ZeroStorage();
        } else if (zeroCmp < 0) {
            storage = new BigIntegerStorage(blockData);
        } else if (blockData.compareTo(BYTE_MASK) < 0) {
            storage = new ByteStorage(blockData.byteValue());
        } else if (blockData.compareTo(INT_MASK) < 0) {
            storage = new IntStorage(blockData.intValue());
        } else if (blockData.compareTo(LONG_MASK) < 0) {
            storage = new LongStorage(blockData.longValue());
        } else {
            storage = new BigIntegerStorage(blockData);
        }
    }
    
    @Nonnull
    public BlockState withData(int data) {
        return of(blockId, data);
    }

    @Nonnull
    public BlockState withData(long data) {
        return of(blockId, data);
    }

    @Nonnull
    public BlockState withData(BigInteger data) {
        return of(blockId, data);
    }

    @Nonnull
    public BlockState withData(Number data) {
        return of(blockId, data);
    }

    @Nonnull
    public BlockState withBlockId(int blockId) {
        return storage.withBlockId(blockId);
    }

    @Nonnull
    public <E extends Serializable> BlockState withProperty(BlockProperty<E> property, @Nullable E value) {
        return withProperty(property.getName(), value);
    }

    /**
     * @throws NoSuchElementException If the property is not registered
     * @throws InvalidBlockPropertyValueException If the new value is not accepted by the property
     */
    @Nonnull
    public BlockState withProperty(String propertyName, @Nullable Serializable value) {
        return storage.withProperty(propertyName, value);
    }

    /**
     * @throws NoSuchElementException If any of the property is not registered
     */
    public BlockState onlyWithProperties(BlockProperty<?>... properties) {
        String[] names = new String[properties.length];
        for (int i = 0; i < properties.length; i++) {
            names[i] = properties[i].getName();
        }
        return onlyWithProperties(names);
    }

    /**
     * @throws NoSuchElementException If any of the given property names is not found
     */
    public BlockState onlyWithProperties(String... propertyNames) {
        BlockProperties properties = getProperties();
        List<String> list = Arrays.asList(propertyNames);
        if (!properties.getNames().containsAll(list)) {
            Set<String> missing = new LinkedHashSet<>(list);
            missing.removeAll(properties.getNames());
            throw new NoSuchElementException("Missing properties: " + String.join(", ", missing));
        }
        
        return storage.onlyWithProperties(list);
    }

    /**
     * @throws NoSuchElementException If the property was not found
     */
    public BlockState onlyWithProperty(String name) {
        return onlyWithProperties(name);
    }

    /**
     * @throws NoSuchElementException If the property was not found
     */
    public BlockState onlyWithProperty(BlockProperty<?> property) {
        return onlyWithProperties(property);
    }

    /**
     * @throws NoSuchElementException If the property is not registered
     * @throws InvalidBlockPropertyValueException If the new value is not accepted by the property
     */
    public BlockState onlyWithProperty(String name, Serializable value) {
        return storage.onlyWithProperty(name, value);
    }

    /**
     * @throws NoSuchElementException If the property is not registered
     * @throws InvalidBlockPropertyValueException If the new value is not accepted by the property
     */
    public <T extends Serializable> BlockState onlyWithProperty(BlockProperty<T> property, T value) {
        return onlyWithProperty(property.getName(), value);
    }

    public BlockState forItem() {
        BlockProperties properties = getProperties();
        Set<String> allNames = properties.getNames();
        List<String> itemNames = properties.getItemPropertyNames();
        if (allNames.size() == itemNames.size() && allNames.containsAll(itemNames)) {
            return this;
        }
        return storage.onlyWithProperties(itemNames);
    }

    @Nonnull
    @Override
    public Number getDataStorage() {
        return storage.getNumber();
    }

    @Nonnull
    @Override
    public BlockProperties getProperties() {
        return BlockStateRegistry.getProperties(blockId);
    }

    @Deprecated
    @DeprecationDetails(reason = "Can't store all data, exists for backward compatibility reasons", since = "1.4.0.0-PN", replaceWith = "getDataStorage()")
    @Override
    public int getLegacyDamage() {
        return storage.getLegacyDamage();
    }

    @Deprecated
    @DeprecationDetails(reason = "Can't store all data, exists for backward compatibility reasons", since = "1.4.0.0-PN", replaceWith = "getDataStorage()")
    @Override
    public int getBigDamage() {
        return storage.getBigDamage();
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @Nonnull
    @Override
    public BigInteger getHugeDamage() {
        return storage.getHugeDamage();
    }

    @Nonnull
    @Override
    public Object getPropertyValue(String propertyName) {
        return storage.getPropertyValue(propertyName);
    }

    @Override
    public int getIntValue(String propertyName) {
        return storage.getIntValue(propertyName);
    }

    @Override
    public boolean getBooleanValue(String propertyName) {
        return storage.getBooleanValue(propertyName);
    }

    @Nonnull
    @Override
    public String getPersistenceValue(String propertyName) {
        return storage.getPersistenceValue(propertyName);
    }

    @Nonnull
    @Override
    public BlockState getCurrentState() {
        return this;
    }

    @Override
    public int getBitSize() {
        return storage.getBitSize();
    }

    /**
     * @throws ArithmeticException If the storage have more than 32 bits
     */
    @Override
    public int getExactIntStorage() {
        Class<? extends Storage> storageClass = storage.getClass();
        if (storageClass != ZeroStorage.class && storageClass != ByteStorage.class && storageClass != IntStorage.class) {
            throw new ArithmeticException(getDataStorage()+" cant be stored in a 32 bits integer without losses. It has "+getBitSize()+" bits");
        }
        return getBigDamage();
    }

    @Since("1.4.0.0-PN")
    @PowerNukkitOnly
    @Override
    public boolean isDefaultState() {
        return storage.isDefaultState();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlockState that = (BlockState) o;

        if (blockId != that.blockId) return false;
        if (storage.getBitSize() != that.storage.getBitSize()) return false;
        return compareDataEquality(storage.getNumber(), that.storage.getNumber());
    }

    @Override
    public int hashCode() {
        int bitSize = storage.getBitSize();
        int result = blockId;
        result = 31 * result + bitSize;
        if (bitSize <= 32) {
            result = 31 * result + storage.getBigDamage();
        } else if (bitSize <= 64) {
            result = 31 * result + Long.hashCode(storage.getNumber().longValue());
        } else {
            result = 31 * result + storage.getHugeDamage().hashCode();
        }
        return result;
    }


    private static boolean compareDataEquality(Number a, Number b) {
        Class<? extends Number> aClass = a.getClass();
        Class<? extends Number> bClass = b.getClass();
        if (aClass == bClass) {
            return a.equals(b);
        }
        if (aClass != BigInteger.class && bClass != BigInteger.class) {
            return a.longValue() == b.longValue();
        }
        
        BigInteger aBig = aClass == BigInteger.class? (BigInteger) a : new BigInteger(a.toString());
        BigInteger bBig = bClass == BigInteger.class? (BigInteger) b : new BigInteger(b.toString());
        return aBig.equals(bBig);
    }

    /**
     * @throws InvalidBlockStateException If the stored state is invalid
     */
    public void validate() {
        if (valid == OptionalBoolean.TRUE) {
            return;
        }
        
        BlockProperties properties = getProperties();
        if (storage.getBitSize() > properties.getBitSize()) {
            throw new InvalidBlockStateException(this, 
                    "The stored data overflows the maximum properties bits. Stored bits: "+storage.getBitSize()+", " +
                            "Properties Bits: "+properties.getBitSize()+", Stored data: "+storage.getNumber()
            );
        }
        
        try {
            storage.validate(properties);
            valid = OptionalBoolean.TRUE;
        } catch (Exception e) {
            valid = OptionalBoolean.FALSE;
            throw new InvalidBlockStateException(this, e);
        }
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public boolean isCachedValidationValid() {
        return valid.orElse(false);
    }

    @Nonnull
    @Override
    public Block getBlock() {
        try {
            Block block = IBlockState.super.getBlock();
            valid = OptionalBoolean.TRUE;
            return block;
        } catch (InvalidBlockStateException e) {
            valid = OptionalBoolean.FALSE;
            throw e;
        }
    }

    @Nonnull
    @Override
    public Block getBlock(@Nullable Level level, int x, int y, int z, int layer, boolean repair, @Nullable Consumer<BlockStateRepair> callback) {
        if (valid == OptionalBoolean.TRUE) {
            Block block = IBlockState.super.getBlock();
            block.x = x;
            block.y = y;
            block.z = z;
            block.layer = layer;
            block.level = level;
            return block;
        }
        
        if (valid == OptionalBoolean.FALSE) {
            return IBlockState.super.getBlock(level, x, y, z, layer, repair, callback);
        }
        
        Consumer<BlockStateRepair> updater = r-> valid = OptionalBoolean.FALSE;
        
        if (repair && callback != null) {
            callback = updater.andThen(callback);
        } else {
            callback = updater.andThen(rep -> {
                throw new InvalidBlockStateException(this, "Attempted to repair when repair was false. "+rep.toString(), rep.getValidationException());
            });
        }
        
        try {
            Block block = IBlockState.super.getBlock(level, x, y, z, layer, true, callback);
            if (valid == OptionalBoolean.EMPTY) {
                valid = OptionalBoolean.TRUE;
            }
            return block;
        } catch (InvalidBlockStateException e) {
            valid = OptionalBoolean.FALSE;
            throw e;
        }
    }

    @ParametersAreNonnullByDefault
    private interface Storage extends Serializable {
        @Nonnull
        Number getNumber();

        int getLegacyDamage();

        int getBigDamage();

        @Nonnull
        Object getPropertyValue(String propertyName);

        int getIntValue(String propertyName);

        boolean getBooleanValue(String propertyName);

        @Nonnull
        BlockState withBlockId(int blockId);

        @Nonnull
        String getPersistenceValue(String propertyName);

        int getBitSize();

        @Nonnull
        BigInteger getHugeDamage();

        @Nonnull
        BlockState withProperty(String propertyName, @Nullable Serializable value);

        @Nonnull
        BlockState onlyWithProperties(List<String> propertyNames);

        @Nonnull
        BlockState onlyWithProperty(String name, Serializable value);

        void validate(BlockProperties properties);

        boolean isDefaultState();
    }

    @ParametersAreNonnullByDefault
    private class ZeroStorage implements Storage {
        private static final long serialVersionUID = -4199347838375711088L;

        @Override
        public int getBitSize() {
            return 1;
        }

        @Nonnull
        @Override
        public Integer getNumber() {
            return 0;
        }

        @Override
        public int getLegacyDamage() {
            return 0;
        }

        @Override
        public int getBigDamage() {
            return 0;
        }

        @Nonnull
        @Override
        public BigInteger getHugeDamage() {
            return BigInteger.ZERO;
        }

        @Nonnull
        @Override
        public Object getPropertyValue(String propertyName) {
            return getProperties().getValue(0, propertyName);
        }

        @Override
        public int getIntValue(String propertyName) {
            return getProperties().getIntValue(0, propertyName);
        }

        @Override
        public boolean getBooleanValue(String propertyName) {
            return getProperties().getBooleanValue(0, propertyName);
        }

        @Nonnull
        @Override
        public BlockState withBlockId(int blockId) {
            return BlockState.of(blockId);
        }

        @Nonnull
        @Override
        public BlockState withProperty(String propertyName, @Nullable Serializable value) {
            // TODO This can cause problems when setting a property that increases the bit size
            return BlockState.of(blockId, getProperties().setValue(0, propertyName, value));
        }

        @Nonnull
        @Override
        public BlockState onlyWithProperties(List<String> propertyNames) {
            return BlockState.this;
        }

        @Nonnull
        @Override
        public BlockState onlyWithProperty(String name, Serializable value) {
            BlockProperties properties = getProperties();
            if (!properties.contains(name)) {
                return BlockState.this;
            }
            return BlockState.of(blockId, properties.setValue(0, name, value));
        }

        @Override
        public void validate(BlockProperties properties) {
            // Meta 0 is always valid
        }

        @Override
        public boolean isDefaultState() {
            return true;
        }

        @Nonnull
        @Override
        public String getPersistenceValue(String propertyName) {
            return getProperties().getPersistenceValue(0, propertyName);
        }

        @Override
        public String toString() {
            return "0";
        }
    }
    
    private class ByteStorage implements Storage {
        private final byte data;

        @Getter
        private final int bitSize;

        public ByteStorage(byte data) {
            this.data = data;
            this.bitSize = NukkitMath.bitLength(data);
        }

        @Nonnull
        @Override
        public Number getNumber() {
            return data;
        }

        @Override
        public int getLegacyDamage() {
            return data & Block.DATA_MASK;
        }

        @Override
        public int getBigDamage() {
            return data;
        }

        @Nonnull
        @Override
        public BigInteger getHugeDamage() {
            return BigInteger.valueOf(data);
        }

        @Nonnull
        @Override
        public Object getPropertyValue(String propertyName) {
            return getProperties().getValue(data, propertyName);
        }

        @Override
        public int getIntValue(String propertyName) {
            return getProperties().getIntValue(data, propertyName);
        }

        @Override
        public boolean getBooleanValue(String propertyName) {
            return getProperties().getBooleanValue(data, propertyName);
        }

        @Nonnull
        @Override
        public BlockState withBlockId(int blockId) {
            return BlockState.of(blockId, data);
        }

        @Nonnull
        @Override
        public BlockState withProperty(String propertyName, @Nullable Serializable value) {
            // TODO This can cause problems when setting a property that increases the bit size
            return BlockState.of(blockId, getProperties().setValue(data, propertyName, value));
        }

        @Nonnull
        @Override
        public BlockState onlyWithProperties(List<String> propertyNames) {
            return BlockState.of(blockId,
                    getProperties().reduceInt(data, (property, offset, current) ->
                            propertyNames.contains(property.getName())? current : property.setValue(current, offset, null)
                    )
            );
        }

        @Nonnull
        @Override
        @SuppressWarnings({"unchecked", "java:S1905", "rawtypes"})
        public BlockState onlyWithProperty(String name, Serializable value) {
            // TODO This can cause problems when setting a property that increases the bit size
            return BlockState.of(blockId,
                    getProperties().reduceInt(data, (property, offset, current) ->
                            ((BlockProperty)property).setValue(current, offset, name.equals(property.getName())? value : null)
                    )
            );
        }

        @Override
        public void validate(BlockProperties properties) {
            properties.forEach((property, offset) -> property.validateMeta(data, offset));
        }

        @Override
        public boolean isDefaultState() {
            return data == 0;
        }

        @Nonnull
        @Override
        public String getPersistenceValue(String propertyName) {
            return getProperties().getPersistenceValue(data, propertyName);
        }

        @Override
        public String toString() {
            return Byte.toString(data);
        }
    }
    
    @ParametersAreNonnullByDefault
    private class IntStorage implements Storage {
        private static final long serialVersionUID = 4700387399339051513L;
        private final int data;
        
        @Getter
        private final int bitSize;

        public IntStorage(int data) {
            this.data = data;
            bitSize = NukkitMath.bitLength(data);
        }

        @Nonnull
        @Override
        public Number getNumber() {
            return getBigDamage();
        }

        @Override
        public int getLegacyDamage() {
            return (data & Block.DATA_MASK);
        }

        @Override
        public int getBigDamage() {
            return data;
        }

        @Nonnull
        @Override
        public Object getPropertyValue(String propertyName) {
            return getProperties().getValue(data, propertyName);
        }

        @Override
        public int getIntValue(String propertyName) {
            return getProperties().getIntValue(data, propertyName);
        }

        @Override
        public boolean getBooleanValue(String propertyName) {
            return getProperties().getBooleanValue(data, propertyName);
        }

        @Nonnull
        @Override
        public BlockState withBlockId(int blockId) {
            return BlockState.of(blockId, data);
        }

        @Nonnull
        @Override
        public BlockState withProperty(String propertyName, @Nullable Serializable value) {
            // TODO This can cause problems when setting a property that increases the bit size
            return BlockState.of(blockId, getProperties().setValue(data, propertyName, value));
        }

        @Nonnull
        @Override
        public BlockState onlyWithProperties(List<String> propertyNames) {
            return BlockState.of(blockId,
                getProperties().reduceInt(data, (property, offset, current) -> 
                        propertyNames.contains(property.getName())? current : property.setValue(current, offset, null)
                )
            );
        }

        @Nonnull
        @Override
        @SuppressWarnings({"unchecked", "java:S1905", "rawtypes"})
        public BlockState onlyWithProperty(String name, Serializable value) {
            // TODO This can cause problems when setting a property that increases the bit size
            return BlockState.of(blockId,
                    getProperties().reduceInt(data, (property, offset, current) ->
                            ((BlockProperty)property).setValue(current, offset, name.equals(property.getName())? value : null)
                    )
            );
        }

        @Override
        public void validate(BlockProperties properties) {
            properties.forEach((property, offset) -> property.validateMeta(data, offset));
        }

        @Override
        public boolean isDefaultState() {
            return data == 0;
        }

        @Nonnull
        @Override
        public String getPersistenceValue(String propertyName) {
            return getProperties().getPersistenceValue(data, propertyName);
        }

        @Nonnull
        @Override
        public BigInteger getHugeDamage() {
            return BigInteger.valueOf(data);
        }

        @Override
        public String toString() {
            return Integer.toString(data);
        }
    }
    
    @ParametersAreNonnullByDefault
    private class LongStorage implements Storage {
        private static final long serialVersionUID = -2633333569914851875L;
        private final long data;
        
        @Getter
        private final int bitSize;

        public LongStorage(long data) {
            this.data = data;
            bitSize = NukkitMath.bitLength(data);
        }

        @Nonnull
        @Override
        public Number getNumber() {
            return data;
        }

        @Override
        public int getLegacyDamage() {
            return (int)(data & Block.DATA_MASK);
        }

        @Override
        public int getBigDamage() {
            return (int)(data & BlockStateRegistry.BIG_META_MASK);
        }

        @Nonnull
        @Override
        public Object getPropertyValue(String propertyName) {
            return getProperties().getValue(data, propertyName);
        }

        @Override
        public int getIntValue(String propertyName) {
            return getProperties().getIntValue(data, propertyName);
        }

        @Override
        public boolean getBooleanValue(String propertyName) {
            return getProperties().getBooleanValue(data, propertyName);
        }

        @Nonnull
        @Override
        public BlockState withBlockId(int blockId) {
            return BlockState.of(blockId, data);
        }

        @Nonnull
        @Override
        public BlockState withProperty(String propertyName, @Nullable Serializable value) {
            return BlockState.of(blockId, getProperties().setValue(data, propertyName, value));
        }

        @Nonnull
        @Override
        public BlockState onlyWithProperties(List<String> propertyNames) {
            return BlockState.of(blockId,
                    getProperties().reduceLong(data, (property, offset, current) ->
                            propertyNames.contains(property.getName())? current : property.setValue(current, offset, null)
                    )
            );
        }

        @Nonnull
        @Override
        @SuppressWarnings({"unchecked", "java:S1905", "rawtypes"})
        public BlockState onlyWithProperty(String name, Serializable value) {
            // TODO This can cause problems when setting a property that increases the bit size
            return BlockState.of(blockId,
                    getProperties().reduceLong(data, (property, offset, current) ->
                            ((BlockProperty)property).setValue(current, offset, name.equals(property.getName())? value : null)
                    )
            );
        }

        @Override
        public void validate(BlockProperties properties) {
            properties.forEach((property, offset) -> property.validateMeta(data, offset));
        }

        @Override
        public boolean isDefaultState() {
            return data == 0;
        }

        @Nonnull
        @Override
        public String getPersistenceValue(String propertyName) {
            return getProperties().getPersistenceValue(data, propertyName);
        }

        @Nonnull
        @Override
        public BigInteger getHugeDamage() {
            return BigInteger.valueOf(data);
        }

        @Override
        public String toString() {
            return Long.toString(data);
        }
        
    }
    
    @ParametersAreNonnullByDefault
    private class BigIntegerStorage implements Storage {
        private static final long serialVersionUID = 2504213066240296662L;
        private final BigInteger data;

        @Getter
        private final int bitSize;

        public BigIntegerStorage(BigInteger data) {
            this.data = data;
            bitSize = NukkitMath.bitLength(data);
        }

        @Nonnull
        @Override
        public Number getNumber() {
            return getHugeDamage();
        }

        @Override
        public int getLegacyDamage() {
            return data.and(BigInteger.valueOf(Block.DATA_MASK)).intValue();
        }

        @Override
        public int getBigDamage() {
            return data.and(BigInteger.valueOf(BlockStateRegistry.BIG_META_MASK)).intValue();
        }

        @Nonnull
        @Override
        public Object getPropertyValue(String propertyName) {
            return getProperties().getValue(data, propertyName);
        }

        @Override
        public int getIntValue(String propertyName) {
            return getProperties().getIntValue(data, propertyName);
        }

        @Override
        public boolean getBooleanValue(String propertyName) {
            return getProperties().getBooleanValue(data, propertyName);
        }

        @Nonnull
        @Override
        public BlockState withBlockId(int blockId) {
            return BlockState.of(blockId, data);
        }

        @Nonnull
        @Override
        public BlockState withProperty(String propertyName, @Nullable Serializable value) {
            return BlockState.of(blockId, getProperties().setValue(data, propertyName, value));
        }

        @Nonnull
        @Override
        public BlockState onlyWithProperties(List<String> propertyNames) {
            return BlockState.of(blockId,
                    getProperties().reduce(data, (property, offset, current) ->
                            propertyNames.contains(property.getName())? current : property.setValue(current, offset, null)
                    )
            );
        }

        @Nonnull
        @Override
        @SuppressWarnings({"unchecked", "java:S1905", "rawtypes"})
        public BlockState onlyWithProperty(String name, Serializable value) {
            return BlockState.of(blockId,
                    getProperties().reduce(data, (property, offset, current) ->
                            ((BlockProperty)property).setValue(current, offset, name.equals(property.getName())? value : null)
                    )
            );
        }

        @Override
        public void validate(BlockProperties properties) {
            properties.forEach((property, offset) -> property.validateMeta(data, offset));
        }

        @Override
        public boolean isDefaultState() {
            return data.equals(BigInteger.ZERO);
        }

        @Nonnull
        @Override
        public String getPersistenceValue(String propertyName) {
            return getProperties().getPersistenceValue(data, propertyName);
        }

        @Nonnull
        @Override
        public BigInteger getHugeDamage() {
            return data;
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }
}
