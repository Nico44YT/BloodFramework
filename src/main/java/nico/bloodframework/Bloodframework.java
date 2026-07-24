package nico.bloodframework;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import nico.bloodframework.command.ModCommands;
import nico.bloodframework.data.BloodTypeDataLoader;
import nico.bloodframework.network.SyncTypesS2C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bloodframework implements ModInitializer {

    public static final String MOD_ID = "blood_framework";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        BloodTypeDataLoader.init();
        ModCommands.init();

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayNetworking.send(handler.player, new SyncTypesS2C());
        });
    }

    public static Identifier id(String name) {
        return Identifier.of(MOD_ID, name);
    }
}
