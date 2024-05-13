package mod.chloeprime.hitfeedback.forge;

import dev.architectury.platform.forge.EventBuses;
import mod.chloeprime.hitfeedback.HitFeedbackMod;
import mod.chloeprime.hitfeedback.client.HitFeedbackClient;
import net.minecraftforge.fml.common.Mod;
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
            HitFeedbackClient.init();
            modBus.addListener((FMLClientSetupEvent event) -> HitFeedbackClient.setup());
        }
    }
}