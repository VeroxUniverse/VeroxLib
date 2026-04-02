package net.veroxuniverse.veroxlib.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.veroxuniverse.veroxlib.VeroxLib;
import net.veroxuniverse.veroxlib.registry.ModAttributes;

@Mod(VeroxLib.MOD_ID)
public final class VeroxLibNeoForge {
    public VeroxLibNeoForge(IEventBus modEventBus) {
        ModAttributes.register();
        VeroxLib.init();
    }

}
