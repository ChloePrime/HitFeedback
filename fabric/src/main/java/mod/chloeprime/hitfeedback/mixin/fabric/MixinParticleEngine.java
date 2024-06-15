package mod.chloeprime.hitfeedback.mixin.fabric;

import mod.chloeprime.hitfeedback.client.particles.EntityPieceParticle;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleEngine.class)
public class MixinParticleEngine {

    @Inject(
            method = "render(Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/client/Camera;F)V",
            at = @At(value = "HEAD")
    )
    private void beginCustomParticlePieceRender(LightTexture lightTexture, Camera camera, float f, CallbackInfo ci) {
            EntityPieceParticle.beforeRender();
    }

    @Inject(
            method = "render(Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/client/Camera;F)V",
            at = @At(value = "RETURN")
    )
    private void endCustomParticleRender(LightTexture lightTexture, Camera camera, float f, CallbackInfo ci) {
        lightTexture.turnOnLightLayer();
        EntityPieceParticle.doRender();
        lightTexture.turnOffLightLayer();
    }
}
