package mod.chloeprime.hitfeedback.common.forge;

import mod.chloeprime.hitfeedback.common.CommonEventHandler;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber
public class ForgeCommonEventHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public static void onEndAttack(LivingDamageEvent event) {
        CommonEventHandler.onEndAttack(event.getSource(), event.getEntity(), event.getAmount());
    }
}