package net.veroxuniverse.veroxlib.sanity.effects;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.veroxuniverse.veroxlib.api.ISanityEffect;
import net.veroxuniverse.veroxlib.config.SanityConfig;

public class HeartbeatEffect implements ISanityEffect {
    @Override
    public float getThreshold() {
        return SanityConfig.INSTANCE.heartbeat.threshold;
    }

    @Override
    public void apply(Player player, float currentSanity) {
        if (player.tickCount % 30 == 0) {
            player.level().playLocalSound(
                    player.getX(), player.getY(), player.getZ(),
                    SoundEvents.WARDEN_HEARTBEAT,
                    SoundSource.AMBIENT,
                    0.4f,
                    0.8f,
                    false
            );
        }
    }

    @Override
    public boolean isEnabled(SanityConfig config) {
        return config.heartbeat.enabled;
    }

    @Override
    public boolean isClientSide() {
        return true;
    }
}
