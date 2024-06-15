package mod.chloeprime.hitfeedback.mixin.forge;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import mod.chloeprime.hitfeedback.client.particles.EntityPieceParticle;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.culling.Frustum;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(ParticleEngine.class)
public class MixinParticleEngine {
    private static @Unique boolean hit_feedback$isRenderingCustom;

    @ModifyExpressionValue(
            method = "render(Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/client/Camera;FLnet/minecraft/client/renderer/culling/Frustum;Ljava/util/function/Predicate;)V",
            at = @At(value = "INVOKE",
                    ordinal = 0,
                    target = "Ljava/util/Iterator;next()Ljava/lang/Object;")
    )
    private <E> E beginCustomParticlePieceRender(E type) {
        hit_feedback$isRenderingCustom = type == ParticleRenderType.CUSTOM;
        if (hit_feedback$isRenderingCustom) {
            EntityPieceParticle.beforeRender();
        }
        return type;
    }

    @Inject(
            method = "render(Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/client/Camera;FLnet/minecraft/client/renderer/culling/Frustum;Ljava/util/function/Predicate;)V",
            at = @At(value = "INVOKE",
                    ordinal = 0,
                    target = "Lcom/mojang/blaze3d/vertex/BufferUploader;drawWithShader(Lcom/mojang/blaze3d/vertex/MeshData;)V")
    )
    private void endCustomParticleRender(LightTexture arg, Camera arg2, float f, Frustum frustum, Predicate<ParticleRenderType> renderTypePredicate, CallbackInfo ci) {
        if (hit_feedback$isRenderingCustom) {
            EntityPieceParticle.doRender();
        }
    }
}
