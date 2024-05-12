package mod.chloeprime.hitfeedback.fabric;

import mod.chloeprime.hitfeedback.HitFeedbackMod;
import net.fabricmc.api.ModInitializer;


public class HitFeedbackFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        HitFeedbackMod.init();
    }
}