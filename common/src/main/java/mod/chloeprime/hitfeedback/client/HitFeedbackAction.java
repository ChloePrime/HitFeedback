package mod.chloeprime.hitfeedback.client;

import dev.architectury.networking.NetworkManager;
import mod.chloeprime.hitfeedback.client.particles.ParticleEmitterBase;
import mod.chloeprime.hitfeedback.common.particle.ModParticleTypes;
import mod.chloeprime.hitfeedback.mixin.client.ParticleEngineAccessor;
import mod.chloeprime.hitfeedback.network.S2CHitFeedback;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

@FunctionalInterface
public interface HitFeedbackAction extends BiConsumer<S2CHitFeedback, NetworkManager.PacketContext> {
    static HitFeedbackAction addEmitter(ParticleEmitterBase.Builder builder, ParticleEmitterBase.Constructor constructor) {
        return addEmitter(ModParticleTypes.BLOOD, builder, constructor);
    }

    static HitFeedbackAction addEmitter(Supplier<? extends ParticleOptions> particle, ParticleEmitterBase.Builder builder, ParticleEmitterBase.Constructor constructor) {
        return (packet, context) -> {
            if (ClientConfig.PARTICLE_AMOUNT.get() == 0) {
                return;
            }
            var pos = packet.position;
            var vel = packet.velocity;
            var entity = packet.getEntity(context.getPlayer().getLevel());
            var emitter = constructor.create(particle.get(), builder, entity, ((ClientLevel) entity.getLevel()), pos.x, pos.y, pos.z, vel.x, vel.y, vel.z);
            ((ParticleEngineAccessor) MinecraftHolder.MC.particleEngine).getTrackingEmitters().add(emitter);
        };
    }
}
