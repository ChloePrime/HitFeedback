package mod.chloeprime.hitfeedback.client.fabric;

import mod.chloeprime.hitfeedback.client.HitFeedbackClient;
import net.fabricmc.api.ClientModInitializer;

/**
 * @see mod.chloeprime.hitfeedback.mixin.fabric.MixinLivingEntity onEndAttack
 */
public class HitFeedbackClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HitFeedbackClient.init();
        HitFeedbackClient.setup();
    }
}
