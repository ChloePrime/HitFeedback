package mod.chloeprime.hitfeedback.client.fabric;

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import mod.chloeprime.hitfeedback.HitFeedbackMod;
import mod.chloeprime.hitfeedback.client.ClientConfig;
import mod.chloeprime.hitfeedback.client.HitFeedbackClient;
import net.fabricmc.api.ClientModInitializer;
import net.neoforged.fml.config.ModConfig;

/**
 * @see mod.chloeprime.hitfeedback.mixin.fabric.MixinLivingEntity onEndAttack
 */
public class HitFeedbackClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HitFeedbackClient.init();
        NeoForgeConfigRegistry.INSTANCE.register(HitFeedbackMod.MOD_ID, ModConfig.Type.CLIENT, ClientConfig.SPEC);
        HitFeedbackClient.setup();
    }
}
