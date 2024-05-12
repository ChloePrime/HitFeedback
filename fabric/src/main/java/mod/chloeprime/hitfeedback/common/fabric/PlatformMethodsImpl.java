package mod.chloeprime.hitfeedback.common.fabric;

import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

/**
 * @see mod.chloeprime.hitfeedback.common.PlatformMethods
 */
@SuppressWarnings("unused")
public class PlatformMethodsImpl {
    public static double getAttackReach(Entity entity) {
        var baseRange = (entity instanceof ServerPlayer player && player.isCreative()) ? 6 : 3;
        if (entity instanceof LivingEntity living) {
            return ReachEntityAttributes.getAttackRange(living, baseRange);
        }
        return baseRange;
    }
}
