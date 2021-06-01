package cn.nukkit.block;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.utils.Identifier;

/**
 * @author GoodLucky777
 */
public interface BlockIdentifier {
    Identifier AIR = Identifier.fromString("air");
    Identifier STONE = Identifier.fromString("stone");
    Identifier GRASS = Identifier.fromString("grass");
    Identifier DIRT = Identifier.fromString("dirt");
    Identifier COBBLESTONE = Identifier.fromString("cobblestone");
    Identifier PLANKS = Identifier.fromString("planks");
    
}
