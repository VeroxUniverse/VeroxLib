package net.veroxuniverse.veroxlib.neoforge.client;

import me.shedaniel.autoconfig.AutoConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.veroxuniverse.veroxlib.VeroxLib;
import net.veroxuniverse.veroxlib.client.VeroxLibClient;
import net.veroxuniverse.veroxlib.config.SanityConfig;

@EventBusSubscriber(modid = VeroxLib.MOD_ID, value = Dist.CLIENT)
public class VeroxLibNeoForgeClient {

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        VeroxLibClient.initClient();

        ModLoadingContext.get()
                .registerExtensionPoint(
                        IConfigScreenFactory.class,
                        () -> (modContainer, screen) -> AutoConfig.getConfigScreen(SanityConfig.class, screen).get()
                );
    }
}