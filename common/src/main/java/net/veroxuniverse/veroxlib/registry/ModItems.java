package net.veroxuniverse.veroxlib.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.veroxuniverse.veroxlib.VeroxLib;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(VeroxLib.MOD_ID, Registries.ITEM);

    public static final RegistrySupplier<Item> UNLIT_TORCH = ITEMS.register("unlit_torch",
            () -> new StandingAndWallBlockItem(
                    ModBlocks.UNLIT_TORCH.get(),
                    ModBlocks.UNLIT_WALL_TORCH.get(),
                    new Item.Properties().arch$tab(ModTabs.ITEMS_TAB),
                    Direction.DOWN
            ));

    public static void register() {
        ITEMS.register();
    }
}