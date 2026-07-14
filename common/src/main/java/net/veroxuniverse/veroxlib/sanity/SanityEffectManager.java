package net.veroxuniverse.veroxlib.sanity;

import net.minecraft.world.entity.player.Player;
import net.veroxuniverse.veroxlib.api.ISanityEffect;
import net.veroxuniverse.veroxlib.api.SanityAPI;
import net.veroxuniverse.veroxlib.client.ClientSanityData;
import net.veroxuniverse.veroxlib.config.SanityConfig;
import net.veroxuniverse.veroxlib.sanity.effects.*;

import java.util.ArrayList;
import java.util.List;

public class SanityEffectManager {

    private static final List<ISanityEffect> EFFECTS = new ArrayList<>();

    static {
        EFFECTS.add(new WhisperingEffect());
        EFFECTS.add(new FootstepEffect());
        EFFECTS.add(new DoorCreakEffect());
        EFFECTS.add(new FakeCreeperEffect());
        EFFECTS.add(new HeartbeatEffect());
        EFFECTS.add(new AbsoluteDarknessSanityEffect());
    }

    public static void register(ISanityEffect effect) {
        EFFECTS.add(effect);
    }

    public static void tick(Player player) {
        float sanity;
        boolean cultist;

        if (player.level().isClientSide()) {
            sanity = ClientSanityData.getSanity();
            cultist = ClientSanityData.isCultist();
        } else {
            sanity = SanityAPI.getSanity(player);
            cultist = SanityAPI.isCultist(player);
        }

        if (cultist) return;

        boolean isClient = player.level().isClientSide();

        for (ISanityEffect effect : EFFECTS) {
            if (sanity <= effect.getThreshold() && effect.isClientSide() == isClient && effect.isEnabled(SanityConfig.INSTANCE)) {
                effect.apply(player, sanity);
            }
        }
    }
}