package net.veroxuniverse.veroxlib.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.veroxuniverse.veroxlib.client.VeroxLibClient;

public final class VeroxLibFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        VeroxLibClient.initClient();
    }
}
