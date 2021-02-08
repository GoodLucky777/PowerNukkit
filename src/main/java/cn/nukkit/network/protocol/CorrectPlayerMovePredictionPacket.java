package cn.nukkit.network.protocol;

import cn.nukkit.math.Vector3f;

import lombok.ToString;

/**
 * @author GoodLucky777
 */
@ToString
public class CorrectPlayerMovePredictionPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.CORRECT_PLAYER_MOVE_PREDICTION_PACKET;
    
    public float positionX;
    public float positionY;
    public float positionZ;
    public float deltaX;
    public float deltaY;
    public float deltaZ;
    public boolean onGround;
    public long tick;
    
    @Override
    public byte pid() {
        return NETWORK_ID;
    }
    
    @Override
    public void decode() {
        Vector3f v = this.getVector3f();
        this.positionX = v.x;
        this.positionY = v.y;
        this.positionZ = v.z;
        v = this.getVector3f();
        this.deltaX = v.x;
        this.deltaY = v.y;
        this.deltaZ = v.z;
        this.onGround = this.getBoolean();
        this.tick = this.getUnsignedVarLong();
    }
    
    @Override
    public void encode() {
        this.reset();
        this.putVector3f(this.positionX, this.positionY, this.positionZ);
        this.putVector3f(this.deltaX, this.deltaY, this.deltaZ);
        this.putBoolean(this.onGround);
        this.putUnsignedVarLong(this.tick);
    }
}
