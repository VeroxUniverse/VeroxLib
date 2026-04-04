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
import net.veroxuniverse.veroxlib.sanity.SanityData;
import net.veroxuniverse.veroxlib.sanity.SanitySavedData;

import java.util.UUID;

public class SanityAPI {

    private static final ResourceKey<Attribute> CORRUPTION_KEY = ResourceKey.create(Registries.ATTRIBUTE,
            ResourceLocation.fromNamespaceAndPath(VeroxLib.MOD_ID, "corruption"));

    private static final ResourceKey<Attribute> RESISTANCE_KEY = ResourceKey.create(Registries.ATTRIBUTE,
            ResourceLocation.fromNamespaceAndPath(VeroxLib.MOD_ID, "sanity_resistance"));

    private static final ResourceKey<Attribute> REGEN_KEY = ResourceKey.create(Registries.ATTRIBUTE,
            ResourceLocation.fromNamespaceAndPath(VeroxLib.MOD_ID, "sanity_regen"));

    public static float getSanity(Player player) {
        if (player.getServer() == null) return 100f;
        return SanitySavedData.get(player.getServer()).getSanityMap()
                .getOrDefault(player.getUUID(), new SanityData(100f)).value();
    }

    public static boolean isCultist(Player player) {
        if (player.getServer() == null) return false;
        return SanitySavedData.get(player.getServer()).getCultistMap()
                .getOrDefault(player.getUUID(), false);
    }

    public static void setCultist(Player player, boolean value) {
        if (player.getServer() == null) return;
        SanitySavedData data = SanitySavedData.get(player.getServer());
        data.getCultistMap().put(player.getUUID(), value);
        data.setDirty();
        SanityNetworking.syncToClient(player, getSanity(player));
    }

    public static void modifySanity(Player player, float amount) {
        if (player.level().isClientSide() || player.getServer() == null) return;
        SanitySavedData savedData = SanitySavedData.get(player.getServer());
        UUID uuid = player.getUUID();

        SanityData oldData = savedData.getSanityMap().getOrDefault(uuid, new SanityData(100f));
        SanityData newData = oldData.add(amount);
        savedData.getSanityMap().put(uuid, newData);
        savedData.setDirty();

        SanityNetworking.syncToClient(player, newData.value());
    }

    public static float getCorruptionValue(Player player) {
        try {
            Holder<Attribute> holder = player.level().registryAccess().registryOrThrow(Registries.ATTRIBUTE).getHolderOrThrow(CORRUPTION_KEY);
            AttributeInstance inst = player.getAttribute(holder);
            return inst != null ? (float) inst.getValue() : 0.0f;
        } catch (Exception e) {
            return 0.0f;
        }
    }

    public static float getSanityModifier(Player player) {
        float baseModifier = 1.0f;
        float totalProtection = 0.0f;

        try {
            Holder<Attribute> resHolder = player.level().registryAccess().registryOrThrow(Registries.ATTRIBUTE).getHolderOrThrow(RESISTANCE_KEY);
            AttributeInstance resistanceInstance = player.getAttribute(resHolder);
            if (resistanceInstance != null) {
                totalProtection += (float) resistanceInstance.getValue();
            }
        } catch (Exception ignored) {}

        for (ItemStack stack : player.getArmorSlots()) {
            if (!stack.isEmpty() && stack.getItem() instanceof ISanityModifier sanityItem) {
                totalProtection += sanityItem.getSanityResistance(stack);
            }
        }

        totalProtection = Math.min(totalProtection, 0.9f);

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

        try {
            Holder<Attribute> holder = player.level().registryAccess().registryOrThrow(Registries.ATTRIBUTE).getHolderOrThrow(REGEN_KEY);
            AttributeInstance inst = player.getAttribute(holder);
            if (inst != null) {
                totalRegenBonus += (float) inst.getValue();
            }
        } catch (Exception ignored) {}

        for (ItemStack stack : player.getArmorSlots()) {
            if (!stack.isEmpty() && stack.getItem() instanceof ISanityModifier sanityItem) {
                totalRegenBonus += sanityItem.getSanityRegen(stack);
            }
        }

        return Math.max(0.0f, 1.0f + totalRegenBonus);
    }
}