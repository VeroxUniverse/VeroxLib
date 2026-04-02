package net.veroxuniverse.veroxlib.sanity.effects;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.veroxuniverse.veroxlib.api.ISanityEffect;
import net.veroxuniverse.veroxlib.config.SanityConfig;

public class FootstepEffect implements ISanityEffect {
    @Override
    public float getThreshold() {
        return SanityConfig.INSTANCE.footsteps.threshold;
    }

    @Override
    public void apply(Player player, float currentSanity) {
        if (player.getRandom().nextFloat() < SanityConfig.INSTANCE.footsteps.chance) {
            Vec3 backPos = player.position().add(player.getLookAngle().reverse().scale(2.5));

            player.level().playLocalSound(
                    backPos.x, backPos.y, backPos.z,
                    SoundEvents.WARDEN_STEP,
                    SoundSource.AMBIENT,
                    0.35f,
                    0.9f + player.getRandom().nextFloat() * 0.2f,
                    false
            );
        }
    }

    @Override
    public boolean isClientSide() {
        return true;
    }
}