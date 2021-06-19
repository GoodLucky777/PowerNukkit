package cn.nukkit.utils;

import cn.nukkit.Server;
import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;

import com.sun.jna.Library;
import com.sun.jna.Native;

import java.io.File;

/**
 * @author GoodLucky777
 */
@PowerNukkitOnly
@Since("FUTURE")
public class Cubiomes {

    static {
        File file = new File(Server.class.getClassLoader().getResource("lib").getFile());
        System.setProperty("jna.library.path", file.getAbsolutePath());
    }
    
    public interface CLibrary extends Library {
        CLibrary INSTANCE = (CLibrary) Native.loadLibrary("cubiomes", CLibrary.class);
        
        int genNetherScaled(int mc, long seed, int scale, int[] out, int x, int z, int w, int h, int y0, int y1);
    }
    
    public static CLibrary getInstance() {
        return CLibrary.INSTANCE;
    }
}
