package net.veroxuniverse.veroxlib.neoforge.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.veroxuniverse.veroxlib.registry.ModBlocks;

import java.util.List;
import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {

    public ModBlockLootTableProvider(HolderLookup.Provider provider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    protected void generate() {
        this.add(ModBlocks.UNLIT_TORCH.get(), block -> this.createSingleItemTable(ModBlocks.UNLIT_TORCH.get()));
        this.add(ModBlocks.UNLIT_WALL_TORCH.get(), block -> this.createSingleItemTable(ModBlocks.UNLIT_TORCH.get()));

        this.add(ModBlocks.UNLIT_LANTERN.get(), block -> this.createSingleItemTable(ModBlocks.UNLIT_LANTERN.get()));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        List<Block> blocks = new java.util.ArrayList<>();
        ModBlocks.BLOCKS.forEach(supplier -> blocks.add(supplier.get()));
        return blocks;
    }

}