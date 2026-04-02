package net.veroxuniverse.veroxlib.api;

import net.minecraft.world.entity.player.Player;

public interface ISanityCondition {

    enum ConditionType {
        DECREASE, // Sanity loss (darkness)
        INCREASE, // Sanity gain (light)
        RESET     // Full reset (sleeping)
    }

    /**
     * @param player The player to check.
     * @param type The type of sanity change occurring.
     * @return true if the sanity change should be blocked.
     */
    boolean shouldBlock(Player player, ConditionType type);
}