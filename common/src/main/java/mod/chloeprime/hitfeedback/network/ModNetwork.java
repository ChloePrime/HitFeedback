package mod.chloeprime.hitfeedback.network;

import dev.architectury.networking.NetworkChannel;
import mod.chloeprime.hitfeedback.HitFeedbackMod;

public class ModNetwork {
    public static final NetworkChannel CHANNEL = NetworkChannel.create(HitFeedbackMod.loc("main"));

    public static void init() {
        CHANNEL.register(S2CHitFeedback.class, S2CHitFeedback::encode, S2CHitFeedback::new, S2CHitFeedback::handle);
    }
}
