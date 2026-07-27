package nico.bloodframework.data;

import com.google.gson.JsonObject;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public record LoadedBloodTypeContainer(Identifier identifier, Identifier texture, int color) {

    public static LoadedBloodTypeContainer fromJson(Identifier identifier, JsonObject json) {
        Identifier texture = new Identifier(json.get("texture").getAsString());
        int color = JsonHelper.getInt(json, "color", 0xFF_FF_FF_FF);

        return new LoadedBloodTypeContainer(identifier, texture, color);
    }

    public void write(PacketByteBuf packetByteBuf) {
        packetByteBuf.writeIdentifier(identifier);
        packetByteBuf.writeIdentifier(texture);
        packetByteBuf.writeInt(color);
    }

    public static LoadedBloodTypeContainer fromByteBuf(PacketByteBuf packetByteBuf) {
        return new LoadedBloodTypeContainer(packetByteBuf.readIdentifier(), packetByteBuf.readIdentifier(), packetByteBuf.readInt());
    }
}
