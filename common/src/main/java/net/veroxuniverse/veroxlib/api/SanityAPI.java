package net.veroxuniverse.veroxlib.api;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.veroxuniverse.veroxlib.VeroxLib;
import net.veroxuniverse.veroxlib.config.SanityConfig;
import net.veroxuniverse.veroxlib.network.SanityNetworking;
import net.veroxuniverse.veroxlib.registry.ModMobEffects;
import net.veroxuniverse.veroxlib.sanity.SanitySavedData;

public class SanityAPI {

    private static final ResourceKey<Attribute> CORRUPTION_KEY = ResourceKey.create(Registries.ATTRIBUTE,
            ResourceLocation.fromNamespaceAndPath(VeroxLib.MOD_ID, "corruption"));
    private static final ResourceKey<Attribute> RESISTANCE_KEY = ResourceKey.create(Registries.ATTRIBUTE,
            ResourceLocation.fromNamespaceAndPath(VeroxLib.MOD_ID, "sanity_resistance"));
    private static final ResourceKey<Attribute> REGEN_KEY = ResourceKey.create(Registries.ATTRIBUTE,
            ResourceLocation.fromNamespaceAndPath(VeroxLib.MOD_ID, "sanity_regen"));

    public static float getSanity(Player player) {
        if (player.getServer() == null) return 100f;
        return SanitySavedData.get(player.getServer()).getSanity(player.getUUID());
    }

    public static boolean isCultist(Player player) {
        if (player.getServer() == null) return false;
        return SanitySavedData.get(player.getServer()).isCultist(player.getUUID());
    }

    public static void setCultist(Player player, boolean value) {
        if (player.getServer() == null) return;
        SanitySavedData data = SanitySavedData.get(player.getServer());
        data.setCultist(player.getUUID(), value);
        SanityNetworking.syncToClient(player, getSanity(player));
    }

    public static void modifySanity(Player player, float amount) {
        if (player.level().isClientSide() || player.getServer() == null) return;
        SanitySavedData data = SanitySavedData.get(player.getServer());
        data.modifySanity(player.getUUID(), amount);
        SanityNetworking.syncToClient(player, data.getSanity(player.getUUID()));
    }

    private static AttributeInstance getAttribute(Player player, ResourceKey<Attribute> key) {
        try {
            Holder<Attribute> holder = player.level().registryAccess()
                    .registryOrThrow(Registries.ATTRIBUTE)
                    .getHolderOrThrow(key);
            return player.getAttribute(holder);
        } catch (Exception e) {
            return null;
        }
    }

    public static float getCorruptionValue(Player player) {
        AttributeInstance inst = getAttribute(player, CORRUPTION_KEY);
        return inst != null ? (float) inst.getValue() : 0.0f;
    }

    public static float getSanityModifier(Player player) {
        float totalProtection = 0.0f;

        AttributeInstance resistanceInstance = getAttribute(player, RESISTANCE_KEY);
        if (resistanceInstance != null) {
            totalProtection += (float) resistanceInstance.getValue();
        }

        for (ItemStack stack : player.getArmorSlots()) {
            if (!stack.isEmpty() && stack.getItem() instanceof ISanityModifier sanityItem) {
                totalProtection += sanityItem.getSanityResistance(stack);
            }
        }

        totalProtection = Math.min(totalProtection, 0.9f);

        float baseModifier = 1.0f;
        if (SanityConfig.INSTANCE.corruptionAffectsSanity) {
            float corruption = getCorruptionValue(player);
            float strength = SanityConfig.INSTANCE.corruptionMultiplierStrength / 10.0f;
            baseModifier *= (1.0f + (corruption * strength));
        }

        float finalModifier = baseModifier * (1.0f - totalProtection);

        if (player.hasEffect(ModMobEffects.SANITY_PROTECTION)) {
            finalModifier *= 0.5f;
        }

        return finalModifier;
    }

    public static float getSanityRegenModifier(Player player) {
        float totalRegenBonus = 0.0f;

        AttributeInstance inst = getAttribute(player, REGEN_KEY);
        if (inst != null) {
            totalRegenBonus += (float) inst.getValue();
        }

        for (ItemStack stack : player.getArmorSlots()) {
            if (!stack.isEmpty() && stack.getItem() instanceof ISanityModifier sanityItem) {
                totalRegenBonus += sanityItem.getSanityRegen(stack);
            }
        }

        return Math.max(0.0f, 1.0f + totalRegenBonus);
    }
}