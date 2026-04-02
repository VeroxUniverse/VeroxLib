package net.veroxuniverse.veroxlib.registry;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.veroxuniverse.veroxlib.VeroxLib;

public class ModTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(VeroxLib.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> ITEMS_TAB = TABS.register(
            "items_tab",
            () -> CreativeTabRegistry.create(
                    Component.translatable("itemGroup." + VeroxLib.MOD_ID + ".items"),
                    () -> new ItemStack(ModItems.UNLIT_TORCH.get())
            )
    );


    public static void register() {
        TABS.register();
    }
}