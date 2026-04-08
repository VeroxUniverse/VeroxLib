package net.veroxuniverse.veroxlib.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.veroxuniverse.veroxlib.VeroxLib;

public class ModTags {

    public static final TagKey<Block> LIGHT_SOURCES = TagKey.create(
            Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(VeroxLib.MOD_ID, "light_sources")
    );

    public static final TagKey<Biome> IS_HORROR_BIOME = TagKey.create(
            Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(VeroxLib.MOD_ID, "is_horror_biome")
    );
}