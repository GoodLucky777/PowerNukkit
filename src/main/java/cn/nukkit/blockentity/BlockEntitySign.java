package cn.nukkit.blockentity;

import cn.nukkit.Player;
import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSignPost;
import cn.nukkit.event.block.SignChangeEvent;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.TextFormat;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author MagicDroidX (Nukkit Project)
 */
public class BlockEntitySign extends BlockEntitySpawnable {

    private String[] text;
    private String textOwner;
    private int signTextColor;
    private boolean ignoreLighting;

    public BlockEntitySign(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected void initBlockEntity() {
        text = new String[4];

        if (!namedTag.contains("Text")) {

            for (int i = 1; i <= 4; i++) {
                String key = "Text" + i;

                if (namedTag.contains(key)) {
                    String line = namedTag.getString(key);

                    this.text[i - 1] = line;

                    this.namedTag.remove(key);
                }
            }
        } else {
            String[] lines = namedTag.getString("Text").split("\n", 4);

            for (int i = 0; i < text.length; i++) {
                if (i < lines.length)
                    text[i] = lines[i];
                else
                    text[i] = "";
            }
        }

        // Check old text to sanitize
        if (text != null) {
            sanitizeText(text);
        }
        
        if (!namedTag.contains("TextOwner")) {
            nbt.putString("TextOwner", "");
        }
        this.textOwner = nbt.getString("TextOwner");
        
        if (!namedTag.contains("SignTextColor")) {
            nbt.putInt("SignTextColor", -16777216);
        }
        this.signTextColor = nbt.getInt("SignTextColor");
        
        if (!namedTag.contains("IgnoreLighting")) {
            nbt.putBoolean("IgnoreLighting", false);
        }
        this.ignoreLighting = this.getBoolean("IgnoreLighting");
        
        super.initBlockEntity();
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.remove("Creator");
    }

    @Override
    public boolean isBlockEntityValid() {
        Block block = getBlock();
        return block instanceof BlockSignPost;
    }

    public boolean setText(String... lines) {
        for (int i = 0; i < 4; i++) {
            if (i < lines.length)
                text[i] = lines[i];
            else
                text[i] = "";
        }

        this.namedTag.putString("Text", String.join("\n", text));
        this.spawnToAll();

        if (this.chunk != null) {
            setDirty();
        }

        return true;
    }

    public String[] getText() {
        return text;
    }

    @Override
    public boolean updateCompoundTag(CompoundTag nbt, Player player) {
        if (!nbt.getString("id").equals(BlockEntity.SIGN)) {
            return false;
        }
        String[] lines = new String[4];
        Arrays.fill(lines, "");
        String[] splitLines = nbt.getString("Text").split("\n", 4);
        System.arraycopy(splitLines, 0, lines, 0, splitLines.length);

        sanitizeText(lines);

        SignChangeEvent signChangeEvent = new SignChangeEvent(this.getBlock(), player, lines);

        if (!this.namedTag.contains("Creator") || !Objects.equals(player.getUniqueId().toString(), this.namedTag.getString("Creator"))) {
            signChangeEvent.setCancelled();
        }

        if (player.getRemoveFormat()) {
            for (int i = 0; i < lines.length; i++) {
                lines[i] = TextFormat.clean(lines[i]);
            }
        }

        this.server.getPluginManager().callEvent(signChangeEvent);

        if (!signChangeEvent.isCancelled()) {
            this.setText(signChangeEvent.getLines());
            return true;
        }

        return false;
    }

    @Override
    public CompoundTag getSpawnCompound() {
        return new CompoundTag()
                .putString("id", BlockEntity.SIGN)
                .putString("Text", this.namedTag.getString("Text"))
                .putString("TextOwner", this.namedTag.getString("TextOwner"))
                .putInt("SignTextColor", this.namedTag.getInt("SignTextColor"))
                .putBoolean("IgnoreLighting", this.namedTag.getBoolean("IgnoreLighting"))
                .putInt("x", (int) this.x)
                .putInt("y", (int) this.y)
                .putInt("z", (int) this.z);

    }

    private static void sanitizeText(String[] lines) {
        for (int i = 0; i < lines.length; i++) {
            // Don't allow excessive text per line.
            if (lines[i] != null) {
                lines[i] = lines[i].substring(0, Math.min(255, lines[i].length()));
            }
        }
    }
    
    @PowerNukkitOnly
    @Since("FUTURE")
    public String getTextOwner() {
        return this.textOwner;
    }
    
    @PowerNukkitOnly
    @Since("FUTURE")
    public int getSignTextColor() {
        return this.signTextColor;
    }
    
    @PowerNukkitOnly
    @Since("FUTURE")
    public boolean isIgnoreLighting() {
        return this.ignoreLighting;
    }
}
