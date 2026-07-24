package nico.bloodframework.network;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import nico.bloodframework.Bloodframework;
import nico.bloodframework.data.BloodTypeDataLoader;
import nico.bloodframework.data.LoadedBloodTypeContainer;

public record SyncTypesS2C() implements FabricPacket {
    private static final Identifier ID = Bloodframework.id("sync_types");
    public static final PacketType<SyncTypesS2C> TYPE = PacketType.create(ID, SyncTypesS2C::fromByteBuf);

    private static SyncTypesS2C fromByteBuf(PacketByteBuf packetByteBuf) {
        int length = packetByteBuf.readVarInt();

        BloodTypeDataLoader.getData().clear();
        for(int i = 0; i < length; i++) {
            var type = LoadedBloodTypeContainer.fromByteBuf(packetByteBuf);
            BloodTypeDataLoader.getData().put(type.identifier(), type);
        }

        return new SyncTypesS2C();
    }

    @Override
    public void write(PacketByteBuf packetByteBuf) {
        int length = BloodTypeDataLoader.getData().size();
        packetByteBuf.writeVarInt(length);

        BloodTypeDataLoader.getData().values().forEach(data -> {
            data.write(packetByteBuf);
        });
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}
