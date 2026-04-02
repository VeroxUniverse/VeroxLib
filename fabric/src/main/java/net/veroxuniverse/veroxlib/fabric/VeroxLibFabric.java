package net.veroxuniverse.veroxlib.fabric;

import net.fabricmc.api.ModInitializer;
import net.veroxuniverse.veroxlib.VeroxLib;

public final class VeroxLibFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        VeroxLib.init();
    }
}
