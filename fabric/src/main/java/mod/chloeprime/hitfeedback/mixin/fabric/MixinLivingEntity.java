package mod.chloeprime.hitfeedback.mixin.fabric;

import mod.chloeprime.hitfeedback.common.CommonEventHandler;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @see mod.chloeprime.hitfeedback.client.fabric.HitFeedbackClientFabric onBeginAttack
 */
@Mixin(LivingEntity.class)
public class MixinLivingEntity {
    @Inject(
            method = "actuallyHurt",
            at = @At(value = "CONSTANT", args = "floatValue=0.0f"),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;awardStat(Lnet/minecraft/resources/ResourceLocation;I)V"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setHealth(F)V")
            )
    )
    private void beforeActuallyHurt(DamageSource damageSource, float f, CallbackInfo ci) {
        CommonEventHandler.onEndAttack(damageSource, (LivingEntity) (Object) this, f);
    }
}
