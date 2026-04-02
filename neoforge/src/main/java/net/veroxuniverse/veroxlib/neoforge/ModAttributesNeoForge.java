package net.veroxuniverse.veroxlib.neoforge;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.veroxuniverse.veroxlib.VeroxLib;

@EventBusSubscriber(modid = VeroxLib.MOD_ID)
public class ModAttributesNeoForge {

    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        Holder<Attribute> sanityResistance = BuiltInRegistries.ATTRIBUTE
                .getHolderOrThrow(ResourceKey.create(
                        Registries.ATTRIBUTE,
                        ResourceLocation.fromNamespaceAndPath(VeroxLib.MOD_ID, "sanity_resistance")
                ));

        Holder<Attribute> corruption = BuiltInRegistries.ATTRIBUTE
                .getHolderOrThrow(ResourceKey.create(
                        Registries.ATTRIBUTE,
                        ResourceLocation.fromNamespaceAndPath(VeroxLib.MOD_ID, "corruption")
                ));

        event.add(EntityType.PLAYER, sanityResistance);
        event.add(EntityType.PLAYER, corruption);
    }
}