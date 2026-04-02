package net.veroxuniverse.veroxlib.sanity.effects;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.veroxuniverse.veroxlib.api.ISanityEffect;
import net.veroxuniverse.veroxlib.config.SanityConfig;

public class WhisperingEffect implements ISanityEffect {
    @Override
    public float getThreshold() {
        return SanityConfig.INSTANCE.whispering.threshold;
    }

    @Override
    public void apply(Player player, float currentSanity) {
        if (player.getRandom().nextFloat() < SanityConfig.INSTANCE.whispering.chance) {
            float pitch = 0.5f + (player.getRandom().nextFloat() * 0.4f);
            player.level().playLocalSound(
                    player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ENDERMAN_STARE,
                    SoundSource.AMBIENT,
                    0.25f,
                    pitch,
                    false);
        }
    }

    @Override
    public boolean isClientSide() {
        return true;
    }
}