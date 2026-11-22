package me.deftware.mixin.indigo;

import me.deftware.client.framework.global.types.BlockPropertyManager;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import me.deftware.mixin.shared.BlockManagement;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Con Fabric API, los chunks los arma Indigo (AltModelBlockRendererImpl) y no
 * ModelBlockRenderer, así que X-ray (BlockPropertyManager) no se aplicaba.
 * Misma lógica que MixinBlockModelRenderer, sobre el renderizador de Indigo.
 */
@Pseudo
@Mixin(targets = "net.fabricmc.fabric.impl.client.indigo.renderer.render.AltModelBlockRendererImpl", remap = false)
public abstract class MixinIndigoBlockRenderer {

    @Shadow private BlockAndTintGetter level;
    @Shadow private BlockPos pos;
    @Shadow private BlockState blockState;

    @Inject(method = "tesselateBlock", at = @At("HEAD"), cancellable = true)
    private void emc$head(@Coerce Object emitter, float x, float y, float z, BlockAndTintGetter level, BlockPos pos, BlockState state, BlockStateModel model, long seed, CallbackInfo ci) {
        BlockPropertyManager props = Bootstrap.blockProperties;
        if (!props.isActive()) {
            BlockManagement.isRenderingOpacityBlock.set(false);
            return;
        }
        int id = BuiltInRegistries.BLOCK.getId(state.getBlock());
        boolean isTarget = props.contains(id) && props.get(id).isRender();
        if (!isTarget && !props.isOpacityMode()) {
            BlockManagement.isRenderingOpacityBlock.set(false);
            ci.cancel();
            return;
        }
        BlockManagement.isRenderingOpacityBlock.set(props.isOpacityMode() && !isTarget);
    }

    @Inject(method = "tesselateBlock", at = @At("RETURN"))
    private void emc$ret(@Coerce Object emitter, float x, float y, float z, BlockAndTintGetter level, BlockPos pos, BlockState state, BlockStateModel model, long seed, CallbackInfo ci) {
        BlockManagement.isRenderingOpacityBlock.set(false);
    }

    @Inject(method = "shouldCullFace", at = @At("HEAD"), cancellable = true)
    private void emc$cull(Direction face, CallbackInfoReturnable<Boolean> cir) {
        if (face == null || this.blockState == null || this.level == null || this.pos == null) return;
        var draw = BlockManagement.shouldDrawSide(this.blockState, this.level, this.pos, face);
        draw.ifPresent(d -> cir.setReturnValue(!d));
    }
}
