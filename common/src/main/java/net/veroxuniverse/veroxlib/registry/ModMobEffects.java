package net.veroxuniverse.veroxlib.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.veroxuniverse.veroxlib.VeroxLib;
import net.veroxuniverse.veroxlib.effect.AbsoluteDarknessEffect;
import net.veroxuniverse.veroxlib.effect.SanityProtectionEffect;

public class ModMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(VeroxLib.MOD_ID, Registries.MOB_EFFECT);

    public static final RegistrySupplier<MobEffect> SANITY_PROTECTION =
            MOB_EFFECTS.register("sanity_protection", SanityProtectionEffect::new);

    public static final RegistrySupplier<MobEffect> ABSOLUTE_DARKNESS =
            MOB_EFFECTS.register("absolute_darkness", AbsoluteDarknessEffect::new);


    private ModMobEffects() {}

    public static void register() {
        MOB_EFFECTS.register();
    }
}