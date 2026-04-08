package net.veroxuniverse.veroxlib.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.veroxuniverse.veroxlib.VeroxLib;

@Config(name = VeroxLib.MOD_ID)
public class SanityConfig implements ConfigData {

    @ConfigEntry.Gui.Excluded
    public static SanityConfig INSTANCE;

    @ConfigEntry.Category("default")
    @ConfigEntry.Gui.Tooltip
    public boolean enableSanityEffects = true;

    @ConfigEntry.Category("default")
    @ConfigEntry.Gui.Tooltip
    public boolean corruptionAffectsSanity = true;

    @ConfigEntry.Category("default")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 1, max = 20)
    public int corruptionMultiplierStrength = 5;

    @ConfigEntry.Category("light_logic")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 0, max = 15)
    public int darknessThreshold = 6;

    @ConfigEntry.Category("light_logic")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 0, max = 15)
    public int brightnessThreshold = 10;

    @ConfigEntry.Category("light_logic")
    @ConfigEntry.Gui.Tooltip
    public float sanityReduction = -0.05f;

    @ConfigEntry.Category("light_logic")
    @ConfigEntry.Gui.Tooltip
    public float sanityGain = 0.02f;

    @ConfigEntry.Category("sleep")
    @ConfigEntry.Gui.Tooltip
    public boolean sleepResetsCompletely = false;

    @ConfigEntry.Category("sleep")
    @ConfigEntry.Gui.Tooltip
    public float sanityGainFromSleep = 30.0f;

    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Category("effects")
    public DarknessSettings absoluteDarkness = new DarknessSettings(true, 5.0f, 6);

    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Category("effects")
    public EffectSettings whispering = new EffectSettings(true, 0.005f, 90f);

    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Category("effects")
    public EffectSettings footsteps = new EffectSettings(true, 0.01f, 60f);

    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Category("effects")
    public EffectSettings doorCreak = new EffectSettings(true, 0.003f, 50f);

    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Category("effects")
    public EffectSettings fakeCreeper = new EffectSettings(true, 0.002f, 30f);

    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Category("effects")
    public SimpleEffectSettings heartbeat = new SimpleEffectSettings(true, 20f);

    @ConfigEntry.Category("biomes")
    @ConfigEntry.Gui.Tooltip
    public float horrorBiomeVisualSanityPressure = -0.2f;

    @ConfigEntry.Category("biomes")
    @ConfigEntry.Gui.Tooltip
    public boolean horrorBiomesOverrideRegen = true;

    public static class DarknessSettings {
        public boolean extinguishLamps;
        public float threshold;
        @ConfigEntry.BoundedDiscrete(min = 1, max = 16)
        public int radius;

        public DarknessSettings(boolean extinguishLamps, float threshold, int radius) {
            this.extinguishLamps = extinguishLamps;
            this.threshold = threshold;
            this.radius = radius;
        }
    }

    public static class EffectSettings {
        public boolean enabled;
        public float chance;
        public float threshold;

        public EffectSettings(boolean enabled, float chance, float threshold) {
            this.enabled = enabled;
            this.chance = chance;
            this.threshold = threshold;
        }
    }

    public static class SimpleEffectSettings {
        public boolean enabled;
        public float threshold;

        public SimpleEffectSettings(boolean enabled, float threshold) {
            this.enabled = enabled;
            this.threshold = threshold;
        }
    }
}