package net.veroxuniverse.veroxlib.sanity.effects;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.veroxuniverse.veroxlib.api.ISanityEffect;
import net.veroxuniverse.veroxlib.config.SanityConfig;
import net.veroxuniverse.veroxlib.registry.ModMobEffects;

public class AbsoluteDarknessSanityEffect implements ISanityEffect {
    @Override
    public float getThreshold() {
        return SanityConfig.INSTANCE.absoluteDarkness.threshold;
    }

    @Override
    public void apply(Player player, float currentSanity) {
        var effectHolder = player.level().registryAccess()
                .registryOrThrow(Registries.MOB_EFFECT)
                .getHolderOrThrow(ModMobEffects.ABSOLUTE_DARKNESS.getKey());

        if (!player.hasEffect(effectHolder)) {
            player.addEffect(new MobEffectInstance(effectHolder, 220, 0, false, false, true));
        }
    }

    @Override
    public boolean isClientSide() {
        return false;
    }
}
