package cn.nukkit.network.protocol;

import cn.nukkit.api.Since;
import cn.nukkit.inventory.stack.request.ItemStackRequest;
import cn.nukkit.inventory.stack.request.action.*;
import cn.nukkit.item.Item;

import lombok.ToString;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;


@Since("1.4.0.0-PN")
@ToString
public class ItemStackRequestPacket extends DataPacket {

    @Since("1.4.0.0-PN")
    public final List<ItemStackRequest> requests = new ArrayList<>();

    @Override
    public byte pid() {
        return ProtocolInfo.ITEM_STACK_REQUEST_PACKET;
    }

    @Override
    public void decode() {
        for (int i = 0; i < this.getUnsignedVarInt(); i++) {
            ItemStackRequest request = new ItemStackRequest();
            request.requestId = this.getVarInt();
            List<StackRequestAction> actions = new ArrayList<>();
            for (int i = 0; i < this.getUnsignedVarInt(); i++) {
                switch (StackRequestActionType.values()[this.getByte()]) {
                    case TAKE:
                        TakeStackRequestAction takeStackRequestAction = new TakeStackRequestAction();
                        takeStackRequestAction.count = this.getByte();
                        takeStackRequestAction.source = this.getStackRequestSlotInfo();
                        takeStackRequestAction.destination = this.getStackRequestSlotInfo();
                        actions.add(takeStackRequestAction);
                        break;
                    case PLACE:
                        
                        break;
                    default:
                        // Unknown action id
                        break;
                }
            }
            request.actions = actions;
            List<String> filterStrings = new ArrayList<>();
            for (int i = 0; i < this.getUnsignedVarInt(); i++) {
                filterStrings.add(this.getString());
            }
            request.filterStrings = filterStrings;
            
            requests.add(request);
        }
    }

    @Override
    public void encode() {
        this.reset();
        this.putUnsignedVarInt(this.requests.size());
        for (ItemStackRequest request : this.requests) {
            
        }
    }
    
    @Deprecated
    @Since("1.4.0.0-PN")
    @Value
    public static class Request {
        private final int requestId;
        private final List<ItemStackAction> actions;
    }
    
    @Deprecated
    @Since("1.4.0.0-PN")
    @Value
    public static class ItemStackAction {
        private final byte type;
        private final boolean bool0;
        private final byte byte0;
        private final int varInt0;
        private final int varInt1;
        private final byte baseByte0;
        private final byte baseByte1;
        private final byte baseByte2;
        private final int baseVarInt0;
        private final byte flagsByte0;
        private final byte flagsByte1;
        private final int flagsVarInt0;
        private final List<Item> items;

        @Override
        public String toString() {
            StringJoiner joiner = new StringJoiner(", ");
            joiner.add("type=" + type);

            switch (type) {
                case 0:
                case 1:
                case 2:
                    joiner.add("baseByte0=" + baseByte0)
                            .add("baseByte1=" + baseByte1)
                            .add("baseByte2=" + baseByte2)
                            .add("baseVarInt0=" + baseVarInt0)
                            .add("flagsByte0=" + flagsByte0)
                            .add("flagsByte1=" + flagsByte1)
                            .add("flagsVarInt0=" + flagsVarInt0);
                    break;
                case 3:
                    joiner.add("bool0=" + bool0)
                            .add("baseByte0=" + baseByte0)
                            .add("baseByte1=" + baseByte1)
                            .add("baseByte2=" + baseByte2)
                            .add("baseVarInt0=" + baseVarInt0);
                    break;
                case 4:
                case 5:
                    joiner.add("baseByte0=" + baseByte0)
                            .add("baseByte1=" + baseByte1)
                            .add("baseByte2=" + baseByte2)
                            .add("baseVarInt0=" + baseVarInt0);
                    break;
                case 6:
                    joiner.add("byte0=" + byte0);
                    break;
                case 8:
                    joiner.add("varInt0=" + varInt0)
                            .add("varInt1=" + varInt1);
                    break;
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                    joiner.add("varInt0=" + varInt0);
                    break;
                case 17:
                    joiner.add("items=" + items);
                    break;
            }
            return "ItemStackAction(" + joiner.toString() + ")";
        }
    }
}
