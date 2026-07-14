package net.veroxuniverse.veroxlib.api;

import net.minecraft.world.entity.player.Player;
import net.veroxuniverse.veroxlib.config.SanityConfig;

/**
 * Interface for all madness-related effects that occur when sanity drops.
 */
public interface ISanityEffect {

    /**
     * @return The sanity threshold (0.0 - 100.0) at which this effect becomes active.
     */
    float getThreshold();

    /**
     * Logic to execute when the effect is active.
     * @param player The affected player.
     * @param currentSanity The current sanity value for intensity scaling.
     */
    void apply(Player player, float currentSanity);

    /**
     * Defines where the effect should be executed.
     * @return true if it's a client-side effect (visuals, local sounds),
     * false if it's server-side (spawning mobs, applying damage/debuffs).
     */
    boolean isClientSide();

    default boolean isEnabled(SanityConfig config) {
        return true;
    }
}
