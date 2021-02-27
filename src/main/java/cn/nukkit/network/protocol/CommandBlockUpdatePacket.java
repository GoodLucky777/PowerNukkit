package cn.nukkit.network.protocol;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.math.BlockVector3;
import lombok.ToString;

@ToString
public class CommandBlockUpdatePacket extends DataPacket {

    public static final int MODE_NORMAL = 0;
    public static final int MODE_REPEATING = 1;
    public static final int MODE_CHAIN = 2;
    
    public boolean isBlock;
    public int x;
    public int y;
    public int z;
    public int commandBlockMode;
    public boolean isRedstoneMode;
    public boolean isConditional;
    public long minecartEid;
    public String command;
    public String lastOutput;
    public String name;
    public boolean shouldTrackOutput;
    @PowerNukkitOnly @Since("1.4.0.0-PN") public long tickDelay;
    @PowerNukkitOnly @Since("1.4.0.0-PN") public boolean executingOnFirstTick;

    @Override
    public byte pid() {
        return ProtocolInfo.COMMAND_BLOCK_UPDATE_PACKET;
    }

    @Override
    public void decode() {
        this.isBlock = this.getBoolean();
        if (this.isBlock) {
            BlockVector3 v = this.getBlockVector3();
            this.x = v.x;
            this.y = v.y;
            this.z = v.z;
            this.commandBlockMode = (int) this.getUnsignedVarInt();
            this.isRedstoneMode = this.getBoolean();
            this.isConditional = this.getBoolean();
        } else {
            this.minecartEid = this.getEntityRuntimeId();
        }
        this.command = this.getString();
        this.lastOutput = this.getString();
        this.name = this.getString();
        this.shouldTrackOutput = this.getBoolean();
        this.tickDelay = (long) this.getLInt();
        this.executingOnFirstTick = this.getBoolean();
    }

    @Override
    public void encode() {
        this.reset();
        this.putBoolean(this.isBlock);
        if (this.isBlock) {
            this.putBlockVector3(this.x, this.y, this.z);
            this.putUnsignedVarInt(this.commandBlockMode);
            this.putBoolean(this.isRedstoneMode);
            this.putBoolean(this.isConditional);
        } else {
            this.putEntityRuntimeId(this.minecartEid);
        }
        this.putString(this.command);
        this.putString(this.lastOutput);
        this.putString(this.name);
        this.putBoolean(this.shouldTrackOutput);
        this.putLInt((int) this.tickDelay);
        this.putBoolean(this.executingOnFirstTick);
    }
}
