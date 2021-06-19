package cn.nukkit.level.generator;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.blockstate.BlockState;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.Level;
import cn.nukkit.level.biome.Biome;
import cn.nukkit.level.biome.generator.NetherBiomeGenerator;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.generator.noise.vanilla.d.NoiseGeneratorOctavesD;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author GoodLucky777
 * <p>
 * This generator originally adapted from the Glowstone's NetherGenerator (https://github.com/GlowstoneMC/Glowstone/blob/5b89f945b49e314399f0b8cd1b8e5e52bee62e80/src/main/java/net/glowstone/generator/NetherGenerator.java)
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
@PowerNukkitOnly
@Since("FUTURE")
public class PowerNukkitNether extends Generator {

    private static final BlockState STATE_BEDROCK = BlockState.of(BEDROCK);
    private static final BlockState STATE_LAVA = BlockState.of(STILL_LAVA);
    private static final BlockState STATE_GRAVEL = BlockState.of(GRAVEL);
    private static final BlockState STATE_NETHERRACK = BlockState.of(NETHERRACK);
    private static final BlockState STATE_SOULSAND = BlockState.of(SOUL_SAND);

    private int LAVA_HEIGHT = 32;
    private int BEDROCK_DEPTH = 5;

    private static double coordinateScale = 684.412D;
    private static double heightScale = 2053.236D;
    private static double surfaceScale = 0.0625D;

    private NoiseGeneratorOctavesD roughness;
    private NoiseGeneratorOctavesD roughness2;
    private NoiseGeneratorOctavesD detail;
    private NoiseGeneratorOctavesD soulsandGravelNoiseGen;
    private NoiseGeneratorOctavesD netherrackExculsivityNoiseGen;
    private NoiseGeneratorOctavesD scaleNoise;
    private NoiseGeneratorOctavesD depthNoise;

    private double[] soulsandNoise = new double[256];
    private double[] gravelNoise = new double[256];
    private double[] surfaceNoise = new double[256];
    private final double[][][] density = new double[5][5][17];

    double[] detailNoise;
    double[] roughnessNoise;
    double[] roughnessNoise2;

    private ChunkManager level;
    private NukkitRandom nukkitRandom;
    private Random random;
    private final List<Populator> populators = new ArrayList<>();
    private List<Populator> generationPopulators = new ArrayList<>();

    private long localSeed1;
    private long localSeed2;

    private NetherBiomeGenerator netherBiomeGenerator;

    public PowerNukkitNether() {
        this(Collections.emptyMap());
    }

    public PowerNukkitNether(Map<String, Object> options) {
        // Just used for future update.
    }

    @Override
    public int getId() {
        return Generator.TYPE_NETHER;
    }

    @Override
    public int getDimension() {
        return Level.DIMENSION_NETHER;
    }

    @Override
    public String getName() {
        return "PowerNukkitNether";
    }

    @Override
    public Map <String, Object> getSettings() {
        return Collections.emptyMap();
    }

    @Override
    public ChunkManager getChunkManager() {
        return level;
    }

    public Vector3 getSpawn() {
        return new Vector3(0.5, 64, 0.5);
    }

    @Override
    public void init(ChunkManager level, NukkitRandom random) {
        this.level = level;
        this.nukkitRandom = random;
        this.random = new Random();
        this.nukkitRandom.setSeed(this.level.getSeed());
        this.localSeed1 = this.random.nextLong();
        this.localSeed2 = this.random.nextLong();

        this.roughness = new NoiseGeneratorOctavesD(this.nukkitRandom, 16);
        this.roughness2 = new NoiseGeneratorOctavesD(this.nukkitRandom, 16);
        this.detail = new NoiseGeneratorOctavesD(this.nukkitRandom, 8);
        this.soulsandGravelNoiseGen = new NoiseGeneratorOctavesD(this.nukkitRandom, 4);
        this.netherrackExculsivityNoiseGen = new NoiseGeneratorOctavesD(this.nukkitRandom, 4);
        this.scaleNoise = new NoiseGeneratorOctavesD(this.nukkitRandom, 10);
        this.depthNoise = new NoiseGeneratorOctavesD(this.nukkitRandom, 16);

        this.netherBiomeGenerator = new NetherBiomeGenerator(this.level.getSeed());
    }

    @Override
    public void generateChunk(int chunkX, int chunkZ) {
        int baseX = chunkX << 4;
        int baseZ = chunkZ << 4;

        int densityX = chunkX << 2;
        int densityZ = chunkZ << 2;

        this.nukkitRandom.setSeed(chunkX * localSeed1 ^ chunkZ * localSeed2 ^ this.level.getSeed());

        BaseFullChunk chunk = level.getChunk(chunkX, chunkZ);

        this.detailNoise = this.detail.generateNoiseOctaves(this.detailNoise, densityX, 0, densityZ, 5, 17, 5, 8.555150000000001D, 34.2206D, 8.555150000000001D);
        this.roughnessNoise = this.roughness.generateNoiseOctaves(this.roughnessNoise, densityX, 0, densityZ, 5, 17, 5, coordinateScale, heightScale, coordinateScale);
        this.roughnessNoise2 = this.roughness2.generateNoiseOctaves(this.roughnessNoise2, densityX, 0, densityZ, 5, 17, 5, coordinateScale, heightScale, coordinateScale);

        double[] nv = new double[17];
        for (int i = 0; i < 17; i++) {
            nv[i] = Math.cos((double) i * Math.PI * 6.0D / (double) 17) * 2.0D;
            double nh = i > 17 / 2 ? 17 - 1 - i : i;
            if (nh < 4.0D) {
                nh = 4.0D - nh;
                nv[i] -= nh * nh * nh * 10.0D;
            }
        }

        int index = 0;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 17; k++) {
                    double noiseR = this.roughnessNoise[index] / 512.0D;
                    double noiseR2 = this.roughnessNoise2[index] / 512.0D;
                    double noiseD = (this.detailNoise[index] / 10.0D + 1.0D) / 2.0D;
                    double nh = nv[k];

                    // linear interpolation
                    double dens = noiseD < 0 ? noiseR :
                        noiseD > 1 ? noiseR2 : noiseR + (noiseR2 - noiseR) * noiseD;
                    dens -= nh;
                    index++;
                    if (k > 13) {
                        double lowering = (double)((float)(k - 13) / 3.0F);
                        dens = dens * (1.0D - lowering) + lowering * -10.0D;
                    }

                    /*if ((double) k < 0) {
                        double d10 = (0 - (double) k) / 4.0D;
                        d10 = Math.clamp(d10, 0.0D, 1.0D);
                        dens = dens * (1.0D - d10) + -10.0D * d10;
                    }*/

                    density[i][j][k] = dens;
                }
            }
        }

        for (int i = 0; i < 5 - 1; i++) {
            for (int j = 0; j < 5 - 1; j++) {
                for (int k = 0; k < 17 - 1; k++) {
                    double d1 = this.density[i][j][k];
                    double d2 = this.density[i + 1][j][k];
                    double d3 = this.density[i][j + 1][k];
                    double d4 = this.density[i + 1][j + 1][k];
                    double d5 = (this.density[i][j][k + 1] - d1) / 8;
                    double d6 = (this.density[i + 1][j][k + 1] - d2) / 8;
                    double d7 = (this.density[i][j + 1][k + 1] - d3) / 8;
                    double d8 = (this.density[i + 1][j + 1][k + 1] - d4) / 8;

                    for (int l = 0; l < 8; l++) {
                        double d9 = d1;
                        double d10 = d3;
                        for (int m = 0; m < 4; m++) {
                            double dens = d9;
                            for (int n = 0; n < 4; n++) {
                                // any density higher than 0 is ground, any density lower or equal
                                // to 0 is air (or lava if under the lava level).
                                if (dens > 0) {
                                    chunk.setBlockState(m + (i << 2), l + (k << 3), n + (j << 2), STATE_NETHERRACK);
                                } else if (l + (k << 3) < LAVA_HEIGHT) {
                                    chunk.setBlockState(m + (i << 2), l + (k << 3), n + (j << 2), STATE_LAVA);
                                }
                                // interpolation along z
                                dens += (d10 - d9) / 4;
                            }
                            // interpolation along x
                            d9 += (d2 - d1) / 4;
                            // interpolate along z
                            d10 += (d4 - d3) / 4;
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

        this.surfaceNoise = this.netherrackExculsivityNoiseGen.generateNoiseOctaves(this.surfaceNoise, baseX, baseZ, 0, 16, 16, 1, surfaceScale, surfaceScale, surfaceScale);
        this.soulsandNoise = this.soulsandGravelNoiseGen.generateNoiseOctaves(this.soulsandNoise, baseX, baseZ, 0, 16, 16, 1, 0.03125D, 0.03125D, 1.0D);
        this.gravelNoise = this.soulsandGravelNoiseGen.generateNoiseOctaves(this.gravelNoise, baseX, 109, baseZ, 16, 1, 16, 0.03125D, 1.0D, 0.03125D);

        int[] biomeIds = netherBiomeGenerator.generateBiomeIds(chunkX, chunkZ);

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                BlockState topState = STATE_NETHERRACK;
                BlockState groundState = STATE_NETHERRACK;

                boolean soulSand = this.soulsandNoise[x | z << 4] + this.nukkitRandom.nextDouble() * 0.2D > 0.0D;
                boolean gravel = this.gravelNoise[x | z << 4] + this.nukkitRandom.nextDouble() * 0.2D > 0.0D;
                int surfaceHeight = (int)(this.surfaceNoise[x | z << 4] / 3.0D + 3.0D + this.nukkitRandom.nextDouble() / 4);
                int deep = -1;
                for (int y = 127; y >= 0; y--) {
                    if (y <= this.nukkitRandom.nextBoundedInt(BEDROCK_DEPTH) || y >= 127 - this.nukkitRandom.nextBoundedInt(BEDROCK_DEPTH)) {
                        chunk.setBlockState(z, y, x, STATE_BEDROCK);
                        continue;
                    } else {
                        BlockState state = chunk.getBlockState(z, y, x);
                        if (state.equals(BlockState.AIR)) {
                            deep = -1;
                        } else if (state.equals(STATE_NETHERRACK)) {
                            if (deep == -1) {
                                if (surfaceHeight <= 0) {
                                    topState = BlockState.AIR;
                                    groundState = STATE_NETHERRACK;
                                } else if (y >= 60 && y <= 65) {
                                    topState = STATE_NETHERRACK;
                                    groundState = STATE_NETHERRACK;
                                    if (gravel) {
                                        topState = STATE_GRAVEL;
                                        groundState = STATE_NETHERRACK;
                                    }
                                    if (soulSand) {
                                        topState = STATE_SOULSAND;
                                        groundState = STATE_SOULSAND;
                                    }
                                }

                                /*if (y < 64 && (topState == null || topState.equals(BlockState.AIR))) {
                                    topState = STATE_LAVA;
                                }*/

                                deep = surfaceHeight;
                                if (y >= 63) {
                                    chunk.setBlockState(z, y, x, topState);
                                } else {
                                    chunk.setBlockState(z, y, x, groundState);
                                }
                            } else if (deep > 0) {
                                deep--;
                                chunk.setBlockState(z, y, x, groundState);
                            }
                        }
                    }
                }

                chunk.setBiomeId(x, z, NetherBiomeGenerator.convertBiomeId(biomeIds[x | z << 4]));
            }
        }

        for (Populator populator: this.generationPopulators) {
            populator.populate(this.level, chunkX, chunkZ, this.nukkitRandom, chunk);
        }
    }

    @Override
    public void populateChunk(int chunkX, int chunkZ) {
        BaseFullChunk chunk = level.getChunk(chunkX, chunkZ);
        this.nukkitRandom.setSeed(0xdeadbeef ^ (chunkX << 8) ^ chunkZ ^ this.level.getSeed());
        for (Populator populator: this.populators) {
            populator.populate(this.level, chunkX, chunkZ, this.nukkitRandom, chunk);
        }

        Biome biome = Biome.getBiome(chunk.getBiomeId(7, 7));
        biome.populateChunk(this.level, chunkX, chunkZ, this.nukkitRandom);
    }
}
