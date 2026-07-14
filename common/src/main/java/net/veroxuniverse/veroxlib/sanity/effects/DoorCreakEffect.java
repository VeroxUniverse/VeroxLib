package net.veroxuniverse.veroxlib.sanity.effects;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.veroxuniverse.veroxlib.api.ISanityEffect;
import net.veroxuniverse.veroxlib.config.SanityConfig;

public class DoorCreakEffect implements ISanityEffect {
    @Override
    public float getThreshold() {
        return SanityConfig.INSTANCE.doorCreak.threshold;
    }

    @Override
    public void apply(Player player, float currentSanity) {
        if (player.getRandom().nextFloat() < SanityConfig.INSTANCE.doorCreak.chance) {
            Vec3 pos = player.position().add(
                    (player.getRandom().nextDouble() - 0.5) * 10,
                    0,
                    (player.getRandom().nextDouble() - 0.5) * 10
            );

            player.level().playLocalSound(
                    pos.x, pos.y, pos.z,
                    SoundEvents.WOODEN_DOOR_OPEN,
                    SoundSource.AMBIENT,
                    0.4f,
                    0.5f + player.getRandom().nextFloat() * 0.3f,
                    false
            );
        }
    }

    @Override
    public boolean isEnabled(SanityConfig config) {
        return config.doorCreak.enabled;
    }

    @Override
    public boolean isClientSide() {
        return true;
    }
}
