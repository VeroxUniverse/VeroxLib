package net.veroxuniverse.veroxlib.sanity.effects;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.veroxuniverse.veroxlib.api.ISanityEffect;
import net.veroxuniverse.veroxlib.config.SanityConfig;

public class FakeCreeperEffect implements ISanityEffect {
    @Override
    public float getThreshold() {
        return SanityConfig.INSTANCE.fakeCreeper.threshold;
    }

    @Override
    public void apply(Player player, float currentSanity) {
        if (player.getRandom().nextFloat() < SanityConfig.INSTANCE.fakeCreeper.chance) {
            Vec3 backPos = player.position().add(player.getLookAngle().reverse().scale(1.5));
            player.level().playLocalSound(
                    backPos.x, backPos.y, backPos.z,
                    SoundEvents.CREEPER_PRIMED,
                    SoundSource.AMBIENT,
                    0.5f,
                    1.0f + player.getRandom().nextFloat() * 0.1f,
                    false);
        }
    }

    @Override
    public boolean isEnabled(SanityConfig config) {
        return config.fakeCreeper.enabled;
    }

    @Override
    public boolean isClientSide() {
        return true;
    }
}