package mod.chloeprime.hitfeedback.mixin.forge.client;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.chloeprime.hitfeedback.client.particles.EntityPieceParticle;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleEngine.class)
public class MixinParticleEngine {
    @Inject(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/client/Camera;FLnet/minecraft/client/renderer/culling/Frustum;)V",
            remap = false,
            at = @At("HEAD"))
    private void beginCustomParticlePieceRender(PoseStack poseStack, MultiBufferSource.BufferSource arg2, LightTexture arg3, Camera arg4, float f, Frustum clippingHelper, CallbackInfo ci) {
        EntityPieceParticle.beforeRender(poseStack);
    }

    @Inject(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/client/Camera;FLnet/minecraft/client/renderer/culling/Frustum;)V",
            remap = false,
            at = @At(value = "RETURN")
    )
    private void endCustomParticleRender(PoseStack arg, MultiBufferSource.BufferSource arg2, LightTexture lightTexture, Camera arg4, float f, Frustum clippingHelper, CallbackInfo ci) {
        lightTexture.turnOnLightLayer();
        EntityPieceParticle.doRender();
        lightTexture.turnOffLightLayer();
    }
}
