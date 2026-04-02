package net.veroxuniverse.veroxlib.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class SanityProtectionEffect extends MobEffect {
    public SanityProtectionEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xADD8E6);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        return true;
    }

}