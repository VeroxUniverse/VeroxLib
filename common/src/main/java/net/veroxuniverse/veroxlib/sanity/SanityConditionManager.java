package net.veroxuniverse.veroxlib.sanity;

import net.minecraft.world.entity.player.Player;
import net.veroxuniverse.veroxlib.api.ISanityCondition;
import java.util.ArrayList;
import java.util.List;

public class SanityConditionManager {
    private static final List<ISanityCondition> CONDITIONS = new ArrayList<>();

    public static void registerCondition(ISanityCondition condition) {
        CONDITIONS.add(condition);
    }

    public static boolean isBlocked(Player player, ISanityCondition.ConditionType type) {
        for (ISanityCondition condition : CONDITIONS) {
            if (condition.shouldBlock(player, type)) {
                return true;
            }
        }
        return false;
    }
}