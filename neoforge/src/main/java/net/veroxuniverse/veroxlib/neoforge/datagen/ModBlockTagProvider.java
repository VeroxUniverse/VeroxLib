package net.veroxuniverse.veroxlib.neoforge.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.veroxuniverse.veroxlib.VeroxLib;
import net.veroxuniverse.veroxlib.registry.ModBlocks;
import net.veroxuniverse.veroxlib.registry.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, VeroxLib.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModTags.LIGHT_SOURCES)
                .add(Blocks.TORCH)
                .add(Blocks.WALL_TORCH)
                .add(Blocks.LANTERN)
                .add(Blocks.CAMPFIRE)
                .add(Blocks.FIRE)
                .add(Blocks.REDSTONE_LAMP);

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.UNLIT_LANTERN.get());

        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.UNLIT_LANTERN.get());

        tag(BlockTags.WALL_POST_OVERRIDE)
                .add(ModBlocks.UNLIT_TORCH.get());

    }
}