package mod.chloeprime.hitfeedback.forge;

import dev.architectury.platform.forge.EventBuses;
import mod.chloeprime.hitfeedback.HitFeedbackMod;
import mod.chloeprime.hitfeedback.client.ClientConfig;
import mod.chloeprime.hitfeedback.client.HitFeedbackClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forgespi.Environment;

@Mod(HitFeedbackMod.MOD_ID)
public class HitFeedbackForge {
    public HitFeedbackForge() {
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(HitFeedbackMod.MOD_ID, modBus);
        HitFeedbackMod.init();

        if (Environment.get().getDist().isClient()) {
            clientInit();
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void clientInit() {
        HitFeedbackClient.init();
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(HitFeedbackForge::clientSetup);
    }

    @OnlyIn(Dist.CLIENT)
    private static void clientSetup(FMLClientSetupEvent event) {
        HitFeedbackClient.setup();
    }
}