package net.veroxuniverse.veroxlib.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.gui.screens.Screen;
import net.veroxuniverse.veroxlib.config.SanityConfig;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (ConfigScreenFactory) parent -> {
            Screen parentScreen = parent;
            return AutoConfig.getConfigScreen(SanityConfig.class, parentScreen).get();
        };
    }
}