package nico.bloodframework.data;

import com.google.gson.JsonObject;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public record LoadedBloodTypeContainer(Identifier identifier, Identifier texture) {

    public static LoadedBloodTypeContainer fromJson(Identifier identifier, JsonObject json) {
        Identifier texture = new Identifier(json.get("texture").getAsString());

        return new LoadedBloodTypeContainer(identifier, texture);
    }

    public void write(PacketByteBuf packetByteBuf) {
        packetByteBuf.writeIdentifier(identifier);
        packetByteBuf.writeIdentifier(texture);
    }

    public static LoadedBloodTypeContainer fromByteBuf(PacketByteBuf packetByteBuf) {
        return new LoadedBloodTypeContainer(packetByteBuf.readIdentifier(), packetByteBuf.readIdentifier());
    }
}
