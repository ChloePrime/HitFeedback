package mod.chloeprime.hitfeedback.client;

import dev.architectury.networking.NetworkManager;
import mod.chloeprime.hitfeedback.client.particles.ModParticleProviders;
import mod.chloeprime.hitfeedback.network.S2CHitFeedback;

public class HitFeedbackClient {
    private HitFeedbackClient() {}

    public static void init() {
        ModParticleProviders.init();
    }

    public static void setup() {
        HitFeedbackActions.init();
    }

    public static void handleFeedbackPacket(S2CHitFeedback packet, NetworkManager.PacketContext context) {
        HitFeedbackActions.get(packet.type).ifPresent(action -> action.accept(packet, context));
    }
}
