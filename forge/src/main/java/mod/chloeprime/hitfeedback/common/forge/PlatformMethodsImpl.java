package mod.chloeprime.hitfeedback.common.forge;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;

/**
 * @see mod.chloeprime.hitfeedback.common.PlatformMethods
 */
@SuppressWarnings("unused")
public class PlatformMethodsImpl {
    public static double getAttackReach(Entity entity) {
        if (entity instanceof Player player) {
            return player.getAttackRange();
        }
        return ForgeMod.ATTACK_RANGE.get().getDefaultValue();
    }
}
