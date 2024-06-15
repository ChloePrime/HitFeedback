package mod.chloeprime.hitfeedback.mixin.compat.azurelib.client;

import com.mojang.blaze3d.platform.NativeImage;
import mod.azure.azurelib.common.internal.common.cache.texture.AnimatableTexture;
import mod.chloeprime.hitfeedback.client.internal.SizedTexture;
import mod.chloeprime.hitfeedback.util.ImageHelper;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(value = AnimatableTexture.class, remap = false)
public class MixinAzureLibTexture extends SimpleTexture implements SizedTexture {
    private @Unique int hit_feedback$w;
    private @Unique int hit_feedback$h;
    private volatile @Unique float hit_feedback$fillRate = 0.6F;

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

    // 计算每帧的图像大小

    @Inject(method = "load", at = @At("RETURN"), remap = true)
    private void calculateFrameSize(ResourceManager manager, CallbackInfo ci) throws IOException {
        var res = manager.getResource(location);
        if (res.isEmpty()) {
            return;
        }
        var resource = res.get();

        int w, h;
        try (var imageFile = resource.open()) {
            try (var image = NativeImage.read(imageFile)) {
                w = image.getWidth();
                h = image.getHeight();
                hit_feedback$fillRate = ImageHelper.getFillRate(image);

                var meta = resource.metadata();
                meta.getSection(AnimationMetadataSection.SERIALIZER).ifPresentOrElse(section -> {
                    var size = section.calculateFrameSize(w, h);
                    hit_feedback$w = size.width();
                    hit_feedback$h = size.height();
                }, () -> {
                    hit_feedback$w = image.getWidth();
                    hit_feedback$h = image.getHeight();
                });
            }
        }
    }

    public MixinAzureLibTexture(ResourceLocation location) {
        super(location);
    }
}
