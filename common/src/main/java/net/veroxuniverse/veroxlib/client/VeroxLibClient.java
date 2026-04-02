package net.veroxuniverse.veroxlib.client;

import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.minecraft.client.renderer.RenderType;
import net.veroxuniverse.veroxlib.network.SanityNetworking;
import net.veroxuniverse.veroxlib.registry.ModBlocks;

public class VeroxLibClient {

    public static void initClient() {
        SanityHudRenderer.init();
        SanityVignetteRenderer.init();

        RenderTypeRegistry.register(RenderType.cutout(),
                ModBlocks.UNLIT_TORCH.get(),
                ModBlocks.UNLIT_WALL_TORCH.get(),
                ModBlocks.UNLIT_LANTERN.get()
        );

        NetworkManager.registerReceiver(NetworkManager.Side.S2C, SanityNetworking.TYPE, SanityNetworking.CODEC, (payload, context) -> {
            context.queue(() -> {
                ClientSanityData.setClientData(payload.value(), payload.isCultist());
            });
        });
    }
}