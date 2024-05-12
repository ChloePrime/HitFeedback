package mod.chloeprime.hitfeedback.util;

import com.mojang.blaze3d.platform.NativeImage;

import java.util.stream.IntStream;

public class ImageHelper {
    public static float getFillRate(NativeImage image) {
        var width = image.getWidth();
        var opaquePixelCount = IntStream.range(0, image.getHeight()).parallel()
                .map(y -> {
                    var sum = 0;
                    for (int x = 0; x < width; x++) {
                        if (image.getLuminanceOrAlpha(x, y) == -1) {
                            sum += 1;
                        }
                    }
                    return sum;
                })
                .sum();
        return  (float)opaquePixelCount / (image.getWidth() * image.getHeight());
    }
}
