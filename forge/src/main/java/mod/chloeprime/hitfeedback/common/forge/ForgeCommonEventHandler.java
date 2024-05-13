package mod.chloeprime.hitfeedback.common.forge;

import mod.chloeprime.hitfeedback.common.CommonEventHandler;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ForgeCommonEventHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public static void onEndAttack(LivingDamageEvent event) {
        CommonEventHandler.onEndAttack(event.getSource(), event.getEntity(), event.getAmount());
    }
}