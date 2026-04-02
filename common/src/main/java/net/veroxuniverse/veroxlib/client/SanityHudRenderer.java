package net.veroxuniverse.veroxlib.client;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.architectury.event.events.client.ClientGuiEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.veroxuniverse.veroxlib.VeroxLib;

public class SanityHudRenderer {
    private static final ResourceLocation EYE_TEX = ResourceLocation.fromNamespaceAndPath(VeroxLib.MOD_ID, "textures/gui/sanity_icons.png");
    private static final RandomSource RANDOM = RandomSource.create();

    private static int lastXOffset = 0;
    private static int lastYOffset = 0;

    public static void init() {
        ClientGuiEvent.RENDER_HUD.register((guiGraphics, tickDelta) -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null || mc.level == null || mc.options.hideGui || mc.player.isSpectator()) return;

            float sanity = ClientSanityData.getSanity();
            boolean isCultist = ClientSanityData.isCultist();
            float stressLevel = isCultist ? sanity : (100f - sanity);

            if (sanity > 80 && !isCultist) return;

            int u = (sanity < 20) ? 48 : (sanity < 40) ? 32 : (sanity < 60) ? 16 : 0;

            int x = guiGraphics.guiWidth() / 2 - 8;
            int y = guiGraphics.guiHeight() - 42 - 11;

            if (stressLevel > 80) {
                if (mc.level.getGameTime() % 3 == 0) {
                    lastXOffset = RANDOM.nextInt(3) - 1;
                    lastYOffset = RANDOM.nextInt(3) - 1;
                }
                x += lastXOffset;
                y += lastYOffset;
            }

            guiGraphics.pose().pushPose();

            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            guiGraphics.pose().translate(0, 0, 150);

            guiGraphics.blit(EYE_TEX, x, y, u, 0, 16, 16, 64, 16);

            RenderSystem.enableDepthTest();
            guiGraphics.pose().popPose();
        });
    }
}