package nico.bloodframework.data;

import com.google.gson.JsonObject;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public record LoadedBloodTypeContainer(String translationKey, Identifier identifier, Identifier texture, int color) {

    public Text getName() {
        return Text.translatable(translationKey());
    }

    public static LoadedBloodTypeContainer fromJson(Identifier identifier, JsonObject json) {
        String translationKey = JsonHelper.getString(json, "name", identifier.toTranslationKey("blood_type", "name"));
        Identifier texture = new Identifier(json.get("texture").getAsString());
        int color = JsonHelper.getInt(json, "color", 0xFF_FF_FF_FF);

        return new LoadedBloodTypeContainer(translationKey, identifier, texture, color);
    }

    public void write(PacketByteBuf packetByteBuf) {
        packetByteBuf.writeIdentifier(identifier);
        packetByteBuf.writeIdentifier(texture);
        packetByteBuf.writeInt(color);
    }

    public static LoadedBloodTypeContainer fromByteBuf(PacketByteBuf packetByteBuf) {
        return new LoadedBloodTypeContainer(
                packetByteBuf.readString(),
                packetByteBuf.readIdentifier(),
                packetByteBuf.readIdentifier(),
                packetByteBuf.readInt()
        );
    }
}
