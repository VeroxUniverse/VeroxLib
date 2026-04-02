package net.veroxuniverse.veroxlib.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.veroxuniverse.veroxlib.VeroxLib;
import net.veroxuniverse.veroxlib.block.UnlitLanternBlock;
import net.veroxuniverse.veroxlib.block.UnlitTorchBlock;
import net.veroxuniverse.veroxlib.block.UnlitWallTorchBlock;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(VeroxLib.MOD_ID, Registries.BLOCK);

    public static final RegistrySupplier<Block> UNLIT_LANTERN = registerBlockWithCustomTab("unlit_lantern",
            () -> new UnlitLanternBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LANTERN).lightLevel(state -> 0)), ModTabs.ITEMS_TAB);

    public static final RegistrySupplier<Block> UNLIT_TORCH = registerBlockWithoutItem("unlit_torch",
            () -> new UnlitTorchBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TORCH)
                    .lightLevel(state -> 0), ParticleTypes.SMOKE));

    public static final RegistrySupplier<Block> UNLIT_WALL_TORCH = registerBlockWithoutItem("unlit_wall_torch",
            () -> new UnlitWallTorchBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WALL_TORCH)
                    .lightLevel(state -> 0), ParticleTypes.SMOKE));


    private static <T extends Block> RegistrySupplier<T> registerBlockWithCustomTab(String name, Supplier<T> block, RegistrySupplier<net.minecraft.world.item.CreativeModeTab> tab) {
        RegistrySupplier<T> toReturn = BLOCKS.register(name, block);
        ModItems.ITEMS.register(name, () -> new BlockItem(toReturn.get(), new Item.Properties().arch$tab(tab)));
        return toReturn;
    }

    private static <T extends Block> RegistrySupplier<T> registerBlockWithoutItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    public static void register() {
        BLOCKS.register();
    }
}