package mod.chloeprime.hitfeedback;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import mod.chloeprime.hitfeedback.client.HitFeedbackClient;
import mod.chloeprime.hitfeedback.common.HitFeedbackTypes;
import mod.chloeprime.hitfeedback.common.ModSoundEvents;
import mod.chloeprime.hitfeedback.common.particle.ModParticleTypes;
import mod.chloeprime.hitfeedback.network.ModNetwork;
import net.minecraft.resources.ResourceLocation;

public class HitFeedbackMod
{
	private HitFeedbackMod() {}

	public static final String MOD_ID = "hit_feedback";

	public static ResourceLocation loc(String path) {
		return new ResourceLocation(MOD_ID, path);
	}

	public static void init() {
		ModSoundEvents.DFR.register();
		ModParticleTypes.DFR.register();
		HitFeedbackTypes.DFR.register();
		ModNetwork.init();
	}
}