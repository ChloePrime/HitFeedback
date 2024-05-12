package mod.chloeprime.hitfeedback.common;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.Entity;

public class PlatformMethods {
    @ExpectPlatform
    public static double getAttackReach(@SuppressWarnings("unused") Entity entity) {
        throw new AssertionError("Expect Platform");
    }
}
