package net.veroxuniverse.veroxlib.sanity;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.Commands;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.veroxuniverse.veroxlib.VeroxLib;
import net.veroxuniverse.veroxlib.api.ISanityCondition;
import net.veroxuniverse.veroxlib.api.SanityAPI;
import net.veroxuniverse.veroxlib.config.SanityConfig;
import net.veroxuniverse.veroxlib.network.SanityNetworking;
import net.veroxuniverse.veroxlib.registry.ModMobEffects;
import net.veroxuniverse.veroxlib.util.LightExtinguisher;

public class SanityEventHandler {

    private static boolean debugEnabled = false;

    private static final ResourceKey<Attribute> CORRUPTION_KEY = ResourceKey.create(Registries.ATTRIBUTE,
            ResourceLocation.fromNamespaceAndPath(VeroxLib.MOD_ID, "corruption"));

    private static final String[] SLEEP_MESSAGE_KEYS = {
            "message.veroxlib.sleep_1", "message.veroxlib.sleep_2",
            "message.veroxlib.sleep_3", "message.veroxlib.sleep_4",
            "message.veroxlib.sleep_5", "message.veroxlib.sleep_6"
    };

    private static final String[] CULTIST_SLEEP_MESSAGE_KEYS = {
            "message.veroxlib.sleep_cultist_1",
            "message.veroxlib.sleep_cultist_2",
            "message.veroxlib.sleep_cultist_3"
    };

    public static void init() {
        TickEvent.PLAYER_POST.register(SanityEventHandler::onPlayerTick);

        PlayerEvent.PLAYER_JOIN.register(player -> {
            if (player instanceof ServerPlayer sp) {
                SanityNetworking.syncToClient(sp, SanityAPI.getSanity(sp));
            }
        });

        PlayerEvent.PLAYER_RESPAWN.register((player, atCheckpoint, status) -> {
            if (SanityAPI.getSanity(player) <= 0.1f) {
                try {
                    Holder<Attribute> holder = player.level().registryAccess().registryOrThrow(Registries.ATTRIBUTE).getHolderOrThrow(CORRUPTION_KEY);
                    AttributeInstance corruption = player.getAttribute(holder);
                    if (corruption != null) {
                        double newValue = Math.min(1.0, corruption.getBaseValue() + 0.02);
                        corruption.setBaseValue(newValue);
                        player.sendSystemMessage(Component.translatable("message.veroxlib.corruption_increased")
                                .withStyle(ChatFormatting.DARK_RED, ChatFormatting.ITALIC));
                    }
                } catch (Exception ignored) {}
            }
        });

        CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> {
            var baseCmd = Commands.literal("sanitysystem").requires(s -> s.hasPermission(2));

            baseCmd.then(Commands.literal("sanity")
                    .then(Commands.literal("set").then(Commands.argument("value", FloatArgumentType.floatArg(0, 100)).executes(c -> {
                        ServerPlayer p = c.getSource().getPlayerOrException();
                        float value = FloatArgumentType.getFloat(c, "value");
                        SanityAPI.modifySanity(p, value - SanityAPI.getSanity(p));
                        c.getSource().sendSuccess(() -> Component.literal("§aSanity set to: " + String.format("%.1f", SanityAPI.getSanity(p))), true);
                        return 1;
                    })))
                    .then(Commands.literal("get").executes(c -> {
                        ServerPlayer p = c.getSource().getPlayerOrException();
                        c.getSource().sendSuccess(() -> Component.literal("§eCurrent Sanity: " + String.format("%.1f", SanityAPI.getSanity(p))), false);
                        return 1;
                    })));

            baseCmd.then(Commands.literal("corruption")
                    .then(Commands.literal("set").then(Commands.argument("value", DoubleArgumentType.doubleArg(0, 100)).executes(c -> {
                        ServerPlayer p = c.getSource().getPlayerOrException();
                        double inputValue = DoubleArgumentType.getDouble(c, "value");
                        double internalValue = inputValue / 100.0;
                        try {
                            Holder<Attribute> holder = p.level().registryAccess().registryOrThrow(Registries.ATTRIBUTE).getHolderOrThrow(CORRUPTION_KEY);
                            AttributeInstance inst = p.getAttribute(holder);
                            if (inst != null) {
                                inst.setBaseValue(internalValue);
                                c.getSource().sendSuccess(() -> Component.literal("§dCorruption set to: " + inputValue + "%"), true);
                            }
                        } catch (Exception ignored) {}
                        return 1;
                    })))
                    .then(Commands.literal("get").executes(c -> {
                        ServerPlayer p = c.getSource().getPlayerOrException();
                        try {
                            Holder<Attribute> holder = p.level().registryAccess().registryOrThrow(Registries.ATTRIBUTE).getHolderOrThrow(CORRUPTION_KEY);
                            AttributeInstance inst = p.getAttribute(holder);
                            double val = (inst != null) ? inst.getValue() : 0.0;
                            c.getSource().sendSuccess(() -> Component.literal("§dCurrent Corruption: " + String.format("%.1f", val * 100.0) + "%"), false);
                        } catch (Exception ignored) {}
                        return 1;
                    })));

            baseCmd.then(Commands.literal("cultist")
                    .then(Commands.literal("on").executes(c -> {
                        SanityAPI.setCultist(c.getSource().getPlayerOrException(), true);
                        c.getSource().sendSuccess(() -> Component.literal("§dCultist mode enabled!"), true);
                        return 1;
                    }))
                    .then(Commands.literal("off").executes(c -> {
                        SanityAPI.setCultist(c.getSource().getPlayerOrException(), false);
                        c.getSource().sendSuccess(() -> Component.literal("§7Cultist mode disabled."), true);
                        return 1;
                    })));

            baseCmd.then(Commands.literal("debug")
                    .then(Commands.literal("on").executes(c -> {
                        debugEnabled = true;
                        c.getSource().sendSuccess(() -> Component.literal("§6Sanity Debug: §aEnabled"), true);
                        return 1;
                    }))
                    .then(Commands.literal("off").executes(c -> {
                        debugEnabled = false;
                        c.getSource().sendSuccess(() -> Component.literal("§6Sanity Debug: §cDisabled"), true);
                        return 1;
                    })));

            baseCmd.then(Commands.literal("reload").executes(c -> {
                AutoConfig.getConfigHolder(SanityConfig.class).load();
                SanityConfig.INSTANCE = AutoConfig.getConfigHolder(SanityConfig.class).getConfig();
                c.getSource().sendSuccess(() -> Component.literal("§aConfig reloaded from file!"), true);
                return 1;
            }));

            dispatcher.register(baseCmd);
            dispatcher.register(Commands.literal("veroxlib").redirect(dispatcher.getRoot().getChild("sanitysystem")));
        });
    }

    private static void onPlayerTick(Player player) {
        if (!player.level().isClientSide) {
            if (player.isSleeping() && player.getSleepTimer() == 100) {
                handleSleepResets(player);
            }

            var darknessEffect = player.level().registryAccess()
                    .registryOrThrow(Registries.MOB_EFFECT)
                    .getHolderOrThrow(ModMobEffects.ABSOLUTE_DARKNESS.getKey());

            if (player.hasEffect(darknessEffect)) {
                if (player.tickCount % 20 == 0 && SanityConfig.INSTANCE.absoluteDarkness.extinguishLamps) {
                    LightExtinguisher.extinguishAroundPlayer(player, SanityConfig.INSTANCE.absoluteDarkness.radius);
                }
            }

            if (player.tickCount % 20 == 0) {
                handleSanityLogic(player);
            }
        }

        if (player.tickCount % 20 == 0 && SanityConfig.INSTANCE.enableSanityEffects) {
            SanityEffectManager.tick(player);
        }
    }

    private static void handleSleepResets(Player player) {
        if (!SanityConditionManager.isBlocked(player, ISanityCondition.ConditionType.RESET)) {
            float current = SanityAPI.getSanity(player);
            if (current < 100f) {
                float amountToHeal = SanityConfig.INSTANCE.sleepResetsCompletely ? (100f - current) : Math.min(SanityConfig.INSTANCE.sanityGainFromSleep, 100f - current);
                if (amountToHeal > 0) {
                    SanityAPI.modifySanity(player, amountToHeal);
                    boolean isCultist = SanityAPI.isCultist(player);
                    String[] pool = isCultist ? CULTIST_SLEEP_MESSAGE_KEYS : SLEEP_MESSAGE_KEYS;
                    player.sendSystemMessage(Component.translatable(pool[player.getRandom().nextInt(pool.length)])
                            .withStyle(isCultist ? ChatFormatting.DARK_PURPLE : ChatFormatting.GREEN));
                }
            }
        }
    }

    private static void handleSanityLogic(Player player) {
        int light = player.level().getMaxLocalRawBrightness(player.blockPosition());
        boolean cultist = SanityAPI.isCultist(player);

        float modifier = SanityAPI.getSanityModifier(player);

        if (light < SanityConfig.INSTANCE.darknessThreshold) {
            if (!SanityConditionManager.isBlocked(player, ISanityCondition.ConditionType.DECREASE)) {
                SanityAPI.modifySanity(player, SanityConfig.INSTANCE.sanityReduction * modifier);
            }
        } else if (light > SanityConfig.INSTANCE.brightnessThreshold) {
            if (!SanityConditionManager.isBlocked(player, ISanityCondition.ConditionType.INCREASE)) {
                SanityAPI.modifySanity(player, SanityConfig.INSTANCE.sanityGain * modifier);
            }
        }

        if (debugEnabled) {
            double corruptionDisplay = SanityAPI.getCorruptionValue(player) * 100.0;
            String modeInfo = cultist ? "§d[Cultist Mode]" : "§b[Human Mode]";
            player.displayClientMessage(
                    Component.literal("§eSanity: §f" + String.format("%.1f", SanityAPI.getSanity(player)) +
                            " §8| §dCorr: §f" + String.format("%.1f", corruptionDisplay) + "%" +
                            " §8| §eLight: §f" + light + " §8| §6Mod: §f" + String.format("%.2f", modifier) + " " + modeInfo),
                    true
            );
        }
    }
}