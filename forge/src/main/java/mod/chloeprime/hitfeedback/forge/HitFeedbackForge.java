package mod.chloeprime.hitfeedback.forge;

import mod.chloeprime.hitfeedback.HitFeedbackMod;
import mod.chloeprime.hitfeedback.client.ClientConfig;
import mod.chloeprime.hitfeedback.client.HitFeedbackClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforgespi.Environment;

@Mod(HitFeedbackMod.MOD_ID)
public class HitFeedbackForge {
    public HitFeedbackForge() {
        HitFeedbackMod.init();

        if (Environment.get().getDist().isClient()) {
            clientInit();
        }
    }

    @SuppressWarnings("DataFlowIssue")
    @OnlyIn(Dist.CLIENT)
    private static void clientInit() {
        HitFeedbackClient.init();
        var container = ModLoadingContext.get().getActiveContainer();
        container.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        container.getEventBus().addListener(HitFeedbackForge::clientSetup);
    }

    @OnlyIn(Dist.CLIENT)
    private static void clientSetup(FMLClientSetupEvent event) {
        HitFeedbackClient.setup();
    }
}