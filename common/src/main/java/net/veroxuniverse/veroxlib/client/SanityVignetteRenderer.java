package net.veroxuniverse.veroxlib.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.architectury.event.events.client.ClientGuiEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.veroxuniverse.veroxlib.config.SanityConfig;

public class SanityVignetteRenderer {
    private static final ResourceLocation VIGNETTE_TEX = ResourceLocation.withDefaultNamespace("textures/misc/vignette.png");
    private static float visualSanity = -1f;

    public static void init() {
        ClientGuiEvent.RENDER_HUD.register((guiGraphics, tickDelta) -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null || mc.level == null || mc.options.hideGui) return;

            if (!SanityConfig.INSTANCE.enableSanityEffects) {
                return;
            }

            float realSanity = ClientSanityData.getSanity();
            boolean isCultist = ClientSanityData.isCultist();

            if (visualSanity < 0) visualSanity = realSanity;

            if (Math.abs(visualSanity - realSanity) > 0.01f) {
                visualSanity = Mth.lerp(0.05f, visualSanity, realSanity);
            } else {
                visualSanity = realSanity;
            }

            float stressLevel = isCultist ? (visualSanity / 100f) : (1.0f - (visualSanity / 100f));
            if (stressLevel < 0.3f) return;

            float intensity = (stressLevel - 0.3f) / 0.7f;
            float gameTime = (float) mc.level.getGameTime() + tickDelta.getGameTimeDeltaTicks();

            if (isCultist) {
                float pulse = (float) Math.sin(gameTime * 0.04f) * 0.03f;
                float finalAlpha = Mth.clamp(intensity + pulse, 0.0f, 0.9f);
                renderVignette(guiGraphics, finalAlpha, 0.4f, 0.0f, 0.6f, true, 0.0f);
            } else {
                float pulse = (float) Math.sin(gameTime * 0.05f) * 0.15f;
                float finalAlpha = Mth.clamp((intensity * 0.85f) + pulse, 0.0f, 0.95f);
                renderVignette(guiGraphics, finalAlpha, 0.0f, 0.0f, 0.0f, false, 0.12f);
            }
        });
    }

    private static void renderVignette(GuiGraphics guiGraphics, float alpha, float r, float g, float b, boolean additive, float offsetMult) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();

        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();

        if (additive) {
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            guiGraphics.setColor(r, g, b, alpha);
            guiGraphics.blit(VIGNETTE_TEX, 0, 0, 0, 0, width, height, width, height);
        } else {
            RenderSystem.defaultBlendFunc();
            guiGraphics.setColor(r, g, b, alpha);

            int offset = (int) (width * offsetMult);
            guiGraphics.blit(VIGNETTE_TEX, -offset, -offset, 0, 0, width + (offset * 2), height + (offset * 2), width + (offset * 2), height + (offset * 2));
        }

        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
    }
}