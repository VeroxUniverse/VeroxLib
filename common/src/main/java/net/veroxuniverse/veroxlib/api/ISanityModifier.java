package net.veroxuniverse.veroxlib.api;

import net.minecraft.world.item.ItemStack;

/**
 * Interface for items that provide a static sanity modifier.
 * Note: For dynamic scaling and cross-mod compatibility,
 * using the custom Sanity Attribute is recommended.
 */
public interface ISanityModifier {
    /**
     * @return 1.0 = normal, 0.5 = half sanity change, 0.0 = no change.
     */
    float getSanityResistance(ItemStack stack);
}