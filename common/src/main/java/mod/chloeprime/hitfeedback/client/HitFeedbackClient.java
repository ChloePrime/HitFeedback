package mod.chloeprime.hitfeedback.client;

import dev.architectury.networking.NetworkManager;
import mod.chloeprime.hitfeedback.client.particles.GunshotFeedbackEmitter;
import mod.chloeprime.hitfeedback.client.particles.ModParticleProviders;
import mod.chloeprime.hitfeedback.client.particles.ParticleEmitterBase;
import mod.chloeprime.hitfeedback.client.particles.SwordFeedbackEmitter;
import mod.chloeprime.hitfeedback.common.HitFeedbackTypes;
import mod.chloeprime.hitfeedback.common.particle.ModParticleTypes;
import mod.chloeprime.hitfeedback.network.S2CHitFeedback;
import net.minecraft.client.particle.BreakingItemParticle;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

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
