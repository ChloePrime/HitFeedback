package mod.chloeprime.hitfeedback.mixin;

import com.mojang.blaze3d.platform.NativeImage;
import mod.chloeprime.hitfeedback.client.internal.SizedTexture;
import mod.chloeprime.hitfeedback.util.ImageHelper;
import net.minecraft.client.renderer.texture.SimpleTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SimpleTexture.class)
public class MixinSimpleTexture implements SizedTexture {
    @Unique
    private int hit_feedback$w = -1, hit_feedback$h = -1;

    @Unique
    private float hit_feedback$fillRate;

    @Override
    public int hit_feedback$getWidth() {
        return hit_feedback$w;
    }

    @Override
    public int hit_feedback$getHeight() {
        return hit_feedback$h;
    }

    @Override
    public float hit_feedback$getFillRate() {
        return hit_feedback$fillRate;
    }

    @Inject(method = "doLoad", at = @At("HEAD"))
    private void captureTextureSize(NativeImage image, boolean blur, boolean clamp, CallbackInfo ci) {
        hit_feedback$w = image.getWidth();
        hit_feedback$h = image.getHeight();
        hit_feedback$fillRate = ImageHelper.getFillRate(image);
        if (hit_feedback$fillRate == 0) {
            hit_feedback$fillRate = 0.01F;
        }
    }
}
