package me.deftware.mixin.mixins.gui;

import com.mojang.renderpearl.api.commands.RenderPass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.GuiRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Aristois (compilado para 1.21.4) genera áreas de recorte vacías o fuera de pantalla.
 * En 26.3 RenderPass.enableScissor las rechaza con IllegalArgumentException y el juego
 * crashea. Recortamos el área a la pantalla; si queda vacía, ese draw no se dibuja.
 */
@Mixin(GuiRenderer.class)
public class MixinGuiRendererScissor {

    @Unique
    private static boolean emc$skipDraw;

    @Inject(method = "executeDraw", at = @At("HEAD"))
    private void emc$resetSkip(@Coerce Object draw, RenderPass pass, CallbackInfo ci) {
        emc$skipDraw = false;
    }

    @Redirect(method = "enableScissor", at = @At(value = "INVOKE",
            target = "Lcom/mojang/renderpearl/api/commands/RenderPass;enableScissor(IIII)V"))
    private void emc$clampScissor(RenderPass pass, int x, int y, int w, int h) {
        com.mojang.blaze3d.platform.Window t = Minecraft.getInstance().getWindow();
        int x0 = Math.max(0, x), y0 = Math.max(0, y);
        int x1 = Math.min(t.getWidth(), x + w), y1 = Math.min(t.getHeight(), y + h);
        if (x1 <= x0 || y1 <= y0) {
            emc$skipDraw = true;
            pass.enableScissor(0, 0, 1, 1);
            return;
        }
        pass.enableScissor(x0, y0, x1 - x0, y1 - y0);
    }

    @Redirect(method = "executeDraw", at = @At(value = "INVOKE",
            target = "Lcom/mojang/renderpearl/api/commands/RenderPass;drawIndexed(IIIII)V"))
    private void emc$maybeDraw(RenderPass pass, int a, int b, int c, int d, int e) {
        if (emc$skipDraw) {
            emc$skipDraw = false;
            return;
        }
        pass.drawIndexed(a, b, c, d, e);
    }
}
