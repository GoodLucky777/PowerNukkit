package cn.nukkit.level.generator;

import cn.nukkit.blockstate.BlockState;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.Level;
import cn.nukkit.level.biome.Biome;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.generator.noise.vanilla.d.NoiseGeneratorOctavesD;
import cn.nukkit.level.generator.noise.vanilla.d.NoiseGeneratorSimplexD;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

import java.util.*;

/**
 * @author GoodLucky777
 * <p>
 * This generator originally adapted from the Glowstone's TheEndGenerator (https://github.com/GlowstoneMC/Glowstone/blob/3de174426cd1e15e78050b9d691a3c1722c83181/src/main/java/net/glowstone/generator/TheEndGenerator.java)
 * 
 * Glowstone licensed under the following MIT license:
 * 
 * Glowstone Copyright (C) 2015-2020 The Glowstone Project.
 * Glowstone Copyright (C) 2011-2014 Tad Hardesty.
 * Lightstone Copyright (C) 2010-2011 Graham Edgecombe.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
public class TheEnd extends Generator {

    private ChunkManager level;
    private NukkitRandom nukkitRandom;
    private Random random;
    
    private static final BlockState STATE_END_STONE = BlockState.of(END_STONE);
    
    private static double coordinateScale = 684.412;
    private static double detailNoiseScaleX = 80;
    private static double detailNoiseScaleZ = 80;
    
    private NoiseGeneratorOctavesD lperlinNoise1;
    private NoiseGeneratorOctavesD lperlinNoise2;
    private NoiseGeneratorOctavesD perlinNoise1;
    private NoiseGeneratorSimplexD islandNoise;
    
    double[] pnr;
    double[] ar;
    double[] br;
    
    private final double[][][] density = new double[3][3][33];
    
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
        
        this.lperlinNoise1 = new NoiseGeneratorOctavesD(random, 16);
        this.lperlinNoise2 = new NoiseGeneratorOctavesD(random, 16);
        this.perlinNoise1 = new NoiseGeneratorOctavesD(random, 8);
        this.islandNoise = new NoiseGeneratorSimplexD(random);
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
        
        int densityX = chunkX << 1;
        int densityZ = chunkZ << 1;
        
        this.pnr = this.perlinNoise1.generateNoiseOctaves(this.pnr, densityX * 2, 0, densityZ * 2, 3, 33, 3, (coordinateScale * 2) / detailNoiseScaleX, 4.277575000000001, (coordinateScale * 2) / detailNoiseScaleZ);
        this.ar = this.lperlinNoise1.generateNoiseOctaves(this.ar, densityX * 2, 0, densityZ * 2, 3, 33, 3, coordinateScale * 2, coordinateScale, coordinateScale * 2);
        this.br = this.lperlinNoise2.generateNoiseOctaves(this.br, densityX * 2, 0, densityZ * 2, 3, 33, 3, coordinateScale * 2, coordinateScale, coordinateScale * 2);
        
        int index = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                double noiseHeight = NukkitMath.clamp(100d - Math.sqrt((densityX + i) * (densityX + i) + (densityZ + j) * (densityZ + j)) * 8d, -100d, 80d);
                for (int k = 0; k < 33; k++) {
                    double noiseR = this.ar[index] / 512;
                    double noiseR2 = this.br[index] / 512;
                    double noiseD = (this.pnr[index] / 10 + 1) / 2;
                    // linear interpolation
                    double dens = noiseD < 0 ? noiseR : noiseD > 1 ? noiseR2 : noiseR + (noiseR2 - noiseR) * noiseD;
                    dens = (dens - 8) + noiseHeight;
                    index++;
                    if (k < 8) {
                        double lowering = (double)((float)(8 - k) / 7);
                        dens = dens * (1d - lowering) + lowering * -30d;
                    } else if (k > (33 / 2) - 2) {
                        double lowering = (double)((float)(k - ((33 / 2) - 2)) / 64);
                        lowering = NukkitMath.clamp(lowering, 0, 1);
                        dens = dens * (1d - lowering) + lowering * -3000d;
                    }
                    density[i][j][k] = dens;
                }
            }
        }
        
        for (int i = 0; i < 3 - 1; i++) {
            for (int j = 0; j < 3 - 1; j++) {
                for (int k = 0; k < 33 - 1; k++) {
                    double d1 = density[i * 3][j * 33][k];
                    double d2 = density[(i + 1) * 3][j * 33][k];
                    double d3 = density[i * 3][(j + 1) * 33][k];
                    double d4 = density[(i + 1) * 3][(j + 1) * 33][k];
                    double d5 = (density[i * 3][j * 33][k + 1] - d1) / 4;
                    double d6 = (density[(i + 1) * 3][j * 33][k + 1] - d2) / 4;
                    double d7 = (density[i * 3][(j + 1) * 33][k + 1] - d3) / 4;
                    double d8 = (density[(i + 1) * 3][(j + 1) * 33][k + 1] - d4) / 4;
                    
                    for (int l = 0; l < 4; l++) {
                        double d9 = d1;
                        double d10 = d3;
                        for (int m = 0; m < 8; m++) {
                            double dens = d9;
                            for (int n = 0; n < 8; n++) {
                                // any density > 0 is ground, any density <= 0 is air.
                                if (dens > 0) {
                                    chunk.setBlockState(m + (i << 3), l + (k << 2), n + (j << 3), STATE_END_STONE);
                                }
                                // interpolation along z
                                dens += (d10 - d9) / 8;
                            }
                            // interpolation along x
                            d9 += (d2 - d1) / 8;
                            // interpolate along z
                            d10 += (d4 - d3) / 8;
                        }
                        // interpolation along y
                        d1 += d5;
                        d3 += d7;
                        d2 += d6;
                        d4 += d8;
                    }
                }
            }
        }
        
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
    
    private float getIslandHeight(int densityX, int densityZ, int chunkX, int chunkZ) {
        float x1 = (float) (densityX * 2 + chunkX);
        float z1 = (float) (densityZ * 2 + chunkZ);
        float islandHeight1 = NukkitMath.clamp(100f - (float) Math.sqrt(((float) Math.pow(x1, 2)) + ((float) Math.pow(z1, 2))) * 8f, -100f, 80f);
        
        for (int i = -12; i <= 12; i++) {
            for (int j = -12; j <= 12; j++) {
                long x2 = (long) (densityX + i);
                long z2 = (long) (densityZ + j);
                if (Math.pow(x2, 2) + Math.pow(z2, 2) > 4096L && this.islandNoise.getValue((double) x2, (double) z2) < -0.8999999761581421D) {
                    x1 = (float) (chunkX - i * 2);
                    z1 = (float) (chunkZ - j * 2);
                    float islandHeight2 = NukkitMath.clamp(100f - (float) Math.sqrt(((float) Math.pow(x1, 2)) + ((float) Math.pow(z1, 2))) * ((Math.abs((float) x2) * 3439f + Math.abs((float) z2) * 147f) % 13f + 9f), -80f, 100f);
                    
                    if (islandHeight2 > islandHeight1) {
                        islandHeight1 = islandHeight2;
                    }
                }
            }
        }
        
        return islandHeight1;
    }
}
