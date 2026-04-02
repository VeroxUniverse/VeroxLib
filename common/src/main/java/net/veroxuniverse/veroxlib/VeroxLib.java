package net.veroxuniverse.veroxlib;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.veroxuniverse.veroxlib.config.SanityConfig;
import net.veroxuniverse.veroxlib.network.SanityNetworking;
import net.veroxuniverse.veroxlib.registry.*;
import net.veroxuniverse.veroxlib.sanity.SanityEventHandler;

public final class VeroxLib {
    public static final String MOD_ID = "veroxlib";

    public static void init() {
        AutoConfig.register(SanityConfig.class, JanksonConfigSerializer::new);
        SanityConfig.INSTANCE = AutoConfig.getConfigHolder(SanityConfig.class).getConfig();
        ModMobEffects.register();
        //ModAttributes.register();
        ModBlocks.register();
        ModItems.register();
        ModTabs.register();
        SanityNetworking.register();
        SanityEventHandler.init();
    }
}
