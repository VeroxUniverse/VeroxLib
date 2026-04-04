package net.veroxuniverse.veroxlib.api;

import net.minecraft.world.item.ItemStack;

/**
 * Interface for items that provide a static sanity modifier.
 * Note: For dynamic scaling and cross-mod compatibility,
 * using the custom Sanity Attribute is recommended.
 */
public interface ISanityModifier {
    /**
     * @return 0.0 = no protection, 0.2 = 20% protection, 0.8 = 80% protection.
     */
    float getSanityResistance(ItemStack stack);

    /**
     * @return 0.0 = no bonus, 0.2 = +20% faster healing, -0.2 = 20% slower healing.
     * Default returns 0.0 (no change).
     */
    default float getSanityRegen(ItemStack stack) {
        return 0.0f;
    }
}