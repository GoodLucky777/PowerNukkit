package cn.nukkit.network.protocol;

import cn.nukkit.math.Vector3f;

import lombok.ToString;

/**
 * @author GoodLucky777
 */
@ToString
public class PlayerAuthInputPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.PLAYER_AUTH_INPUT_PACKET;
    
    public float pitch;
    public float yaw;
    public float headYaw;
    public float positionX;
    public float positionY;
    public float positionZ;
    public float motionX;
    public float motionZ;
    public long inputData;
    public InputMode inputMode;
    public PlayMode playMode;
    public float vrGazeDirectionX;
    public float vrGazeDirectionY;
    public float vrGazeDirectionZ;
    public long tick;
    public float deltaX;
    public float deltaY;
    public float deltaZ;
    
    @Override
    public byte pid() {
        return NETWORK_ID;
    }
    
    @Override
    public void decode() {
        this.pitch = this.getLFloat();
        this.yaw = this.getLFloat();
        Vector3f v = this.getVector3f();
        this.positionX = v.x;
        this.positionY = v.y;
        this.positionZ = v.z;
        this.motionX = this.getLFloat();
        this.motionZ = this.getLFloat();
        this.headYaw = this.getLFloat();
        this.inputData = this.getUnsignedVarLong();
        this.inputMode = InputMode.values()[this.getUnsignedVarInt()];
        this.playMode = PlayMode.values()[this.getUnsignedVarInt()];
        if (this.playMode == PlayMode.REALITY) {
            v = this.getVector3f();
            this.vrGazeDirectionX = v.x;
            this.vrGazeDirectionY = v.y;
            this.vrGazeDirectionZ = v.z;
        }
        this.tick = this.getUnsignedVarLong();
        v = this.getVector3f();
        this.deltaX = v.x;
        this.deltaY = v.y;
        this.deltaZ = v.z;
    }
    
    @Override
    public void encode() {
        this.reset();
        this.putLFloat(this.pitch);
        this.putLFloat(this.yaw);
        this.putVector3f(this.positionX, this.positionY, this.positionZ);
        this.putVector2f(this.motionX, this.motionZ);
        this.putLFloat(this.headYaw);
        this.putUnsignedVarLong(this.inputData);
        this.putUnsignedVarInt(this.inputMode.ordinal());
        this.putUnsignedVarInt(this.playerMode.ordinal());
        if (this.playerMode == PlayMode.REALITY) {
            this.putVector3f(this.vrGazeDirectionX, this.vrGazeDirectionY, this.vrGazeDirectionZ);
        }
        this.putUnsignedVarLong(this.tick);
        this.putVector3f(this.deltaX, this.deltaY, this.deltaZ);
    }
    
    public enum InputMode {
        UNDEFINED,
        MOUSE,
        TOUCH,
        GAMEPAD,
        MOTION_CONTROLLER
    }
    
    public enum PlayMode {
        NORMAL,
        TEASER,
        SCREEN,
        VIEWER,
        REALITY,
        PLACEMENT,
        LIVING_ROOM,
        EXIT_LEVEL,
        EXIT_LEVEL_LIVING_ROOM
    }
}
