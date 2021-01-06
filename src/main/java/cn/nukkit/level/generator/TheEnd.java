package cn.nukkit.level.generator;

import cn.nukkit.block.*;
import cn.nukkit.blockstate.BlockState;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.Level;
import cn.nukkit.level.biome.Biome;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

import java.util.*;

public class TheEnd extends Generator {

    private ChunkManager level;
    private NukkitRandom nukkitRandom;
    private Random random;
    
    private static final BlockState STATE_END_STONE = BlockState.of(END_STONE);
    
    private final List<Populator> populators = new ArrayList<>();
    private List<Populator> generationPopulators = new ArrayList<>();
    
    private long localSeed1;
    private long localSeed2;
    
    public TheEnd() {
        this(Collections.emptyMap());
    }
    
    public TheEnd(Map<String, Object> options) {
        // Nothing here. Just used for future update.
    }
    
    @Override
    public int getId() {
        return Generator.TYPE_THE_END;
    }
    
    @Override
    public int getDimension() {
        return Level.DIMENSION_THE_END;
    }
    
    @Override
    public String getName() {
        return "the_end";
    }
    
    @Override
    public Map<String, Object> getSettings() {
        return new HashMap<>();
    }
    
    @Override
    public ChunkManager getChunkManager() {
        return this.level;
    }
    
    @Override
    public void init(ChunkManager level, NukkitRandom random) {
        this.level = level;
        this.nukkitRandom = random;
        this.random = new Random();
        this.nukkitRandom.setSeed(this.level.getSeed());
        this.localSeed1 = this.random.nextLong();
        this.localSeed2 = this.random.nextLong();

    }
    
    @Override
    public void generateChunk(int chunkX, int chunkZ) {
        int baseX = chunkX << 4;
        int baseZ = chunkZ << 4;
        this.nukkitRandom.setSeed(chunkX * localSeed1 ^ chunkZ * localSeed2 ^ this.level.getSeed());
        
        BaseFullChunk chunk = level.getChunk(chunkX, chunkZ);
        
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                Biome biome = EnumBiome.THE_END.biome;
                chunk.setBiomeId(x, z, biome.getId());
            }
        }
        
        // TODO
        
        for (Populator populator : this.generationPopulators) {
            populator.populate(this.level, chunkX, chunkZ, this.nukkitRandom, chunk);
        }
    }
    
    @Override
    public void populateChunk(int chunkX, int chunkZ) {
        BaseFullChunk chunk = level.getChunk(chunkX, chunkZ);
        this.nukkitRandom.setSeed(0xdeadbeef ^ (chunkX << 8) ^ chunkZ ^ this.level.getSeed());
        for (Populator populator : this.populators) {
            populator.populate(this.level, chunkX, chunkZ, this.nukkitRandom, chunk);
        }
        
        Biome biome = EnumBiome.getBiome(chunk.getBiomeId(7, 7));
        biome.populateChunk(this.level, chunkX, chunkZ, this.nukkitRandom);
    }
    
    public Vector3 getSpawn() {
        return new Vector3(100.5, 49, 0.5);
    }
}
