package cn.nukkit.utils;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * @author GoodLucky777
 */
@PowerNukkitOnly
@Since("FUTURE")
public class Cubiomes {

    public interface CLibrary extends Library {
        CLibrary INSTANCE = (CLibrary) Native.loadLibrary("lib/libcubiomes.so", CLibrary.class);
        
        int genNetherScaled(int mc, long seed, int scale, int[] out, int x, int z, int w, int h, int y0, int y1);
    }
    
    public static CLibrary getInstance() {
        return CLibrary.INSTANCE;
    }
}
