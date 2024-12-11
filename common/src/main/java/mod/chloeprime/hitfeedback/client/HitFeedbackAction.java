package mod.chloeprime.hitfeedback.client;

import dev.architectury.networking.NetworkManager;
import mod.chloeprime.hitfeedback.client.particles.ParticleEmitterBase;
import mod.chloeprime.hitfeedback.client.particles.RatedEmitter;
import mod.chloeprime.hitfeedback.common.particle.ModParticleTypes;
import mod.chloeprime.hitfeedback.mixin.client.ParticleEngineAccessor;
import mod.chloeprime.hitfeedback.network.S2CHitFeedback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;

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
            if (Minecraft.getInstance().options.particles().get() == ParticleStatus.MINIMAL) {
                return;
            }
            var pos = packet.position;
            var vel = packet.velocity;
            var entity = packet.getEntity(context.getPlayer().level());
            var emitter = constructor.create(particle.get(), builder, entity, ((ClientLevel) entity.level()), pos.x, pos.y, pos.z, vel.x, vel.y, vel.z);
            if (emitter instanceof RatedEmitter rated) {
                // 最少只要造成25%最大HP的伤害就触发满力度反馈
                var spawnRate = Mth.clampedLerp(0.2F, 1F, 4 * packet.strength);
                rated.setSpawnRate(spawnRate);
            }
            ((ParticleEngineAccessor) MinecraftHolder.MC.particleEngine).getTrackingEmitters().add(emitter);
        };
    }
}
