package nico.bloodframework.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import nico.bloodframework.network.SyncTypesS2C;

public class BloodframeworkClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(SyncTypesS2C.TYPE, (packet, clientPlayer, sender) -> {});
    }
}
