package cn.nukkit.level.generator.noise.nether;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NetherNoise {

    private final DoublePerlinNoise temperature;
    private final DoublePerlinNoise humidity;
    private final PerlinNoise[8] oct;
}
