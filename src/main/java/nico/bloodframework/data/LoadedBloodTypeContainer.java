package nico.bloodframework.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public record LoadedBloodTypeContainer(
        Identifier identifier,
        // Properties
        String translationKey,
        Identifier texture,
        int textureTint,
        int color
) {
    private static final String KEY_NAME = "name";
    private static final String KEY_TEXTURE = "texture";
    private static final String KEY_FILE = "file";
    private static final String KEY_TINT = "tint";
    private static final String KEY_COLOR = "color";

    public Text getName() {
        return Text.translatable(translationKey());
    }

    public static LoadedBloodTypeContainer fromJson(Identifier identifier, JsonObject json) {
        String translationKey = JsonHelper.getString(
                json,
                KEY_NAME,
                identifier.toTranslationKey("blood_type", "name")
        );

        Identifier texture;
        int textureTint;
        JsonElement textureElement = json.get(KEY_TEXTURE);

        if (textureElement.isJsonPrimitive()) {
            texture = new Identifier(textureElement.getAsString());
            textureTint = 0xFF_FF_FF_FF;

        } else if (textureElement.isJsonObject()) {
            JsonObject textureObject = textureElement.getAsJsonObject();
            if (textureObject.has(KEY_TEXTURE)) {
                texture = new Identifier(textureObject.get(KEY_TEXTURE).getAsString());
            } else {
                texture = new Identifier(textureObject.get(KEY_FILE).getAsString());
            }
            textureTint = getIntFromJsonElement(textureObject.get(KEY_TINT), 0xFF_FF_FF_FF);

        } else {
            throw new RuntimeException(String.format(
                    "Texture element of %s is not an object nor a primitive!",
                    identifier
            ));
        }

        int color = getIntFromJsonElement(json.get(KEY_COLOR), 0xFF_FF_FF_FF);

        return new LoadedBloodTypeContainer(
                identifier,
                translationKey,
                texture,
                textureTint,
                color
        );
    }

    public void write(PacketByteBuf packetByteBuf) {
        packetByteBuf.writeIdentifier(identifier); // Identifier
        packetByteBuf.writeString(translationKey); // Translation key
        packetByteBuf.writeIdentifier(texture); // Texture Identifier
        packetByteBuf.writeInt(textureTint); // Texture Tint
        packetByteBuf.writeInt(color); // Color
    }

    public static LoadedBloodTypeContainer fromByteBuf(PacketByteBuf packetByteBuf) {
        return new LoadedBloodTypeContainer(
                packetByteBuf.readIdentifier(), // Identifier
                packetByteBuf.readString(), // Translation key
                packetByteBuf.readIdentifier(), // Texture Identifier
                packetByteBuf.readInt(), // Texture Tint
                packetByteBuf.readInt() // Color
        );
    }

    private static int getIntFromJsonElement(JsonElement jsonElement, int defaultValue) {
        if (jsonElement.isJsonPrimitive()) return getIntFromJsonElement(jsonElement.getAsJsonPrimitive(), defaultValue);
        throw new RuntimeException(jsonElement + " can only be a number or a string");
    }

    private static int getIntFromJsonElement(JsonPrimitive jsonPrimitive, int defaultValue) {
        if (jsonPrimitive == null) return defaultValue;

        if (jsonPrimitive.isNumber()) {
            return jsonPrimitive.getAsInt();
        }

        if (jsonPrimitive.isString()) {
            return Integer.decode(jsonPrimitive.getAsString().replace("_", ""));
        }

        throw new RuntimeException(jsonPrimitive + " can only be a number or a string");
    }
}