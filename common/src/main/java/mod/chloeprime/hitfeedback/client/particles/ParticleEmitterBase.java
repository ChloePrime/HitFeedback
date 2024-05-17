package mod.chloeprime.hitfeedback.client.particles;

import com.google.common.base.Suppliers;
import mod.chloeprime.hitfeedback.client.ClientConfig;
import mod.chloeprime.hitfeedback.mixin.client.TrackingEmitterAccessor;
import mod.chloeprime.hitfeedback.util.Basis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.TrackingEmitter;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class ParticleEmitterBase extends TrackingEmitter {
    protected static final Supplier<ParticleEngine> ENGINE = Suppliers.memoize(() -> Minecraft.getInstance().particleEngine);
    protected final ParticleOptions particle;
    protected final int emitCountPerTick;
    private final float goreSpawnRate;
    private final boolean goreOnly;
    protected Vec3 relPos;
    protected Vec3 relMotion;
    @SuppressWarnings("FieldMayBeFinal")
    private boolean prepared;
    private final float configRate = ClientConfig.PARTICLE_AMOUNT.get().floatValue() * switch (Minecraft.getInstance().options.particles) {
        case ALL -> 1F;
        case DECREASED -> 0.5F;
        case MINIMAL -> 0F;
    };

    public ParticleEmitterBase(ParticleOptions particle, Builder builder, Entity entity, ClientLevel clientLevel, double x, double y, double z, double g, double h, double i) {
        super(clientLevel, entity, particle, builder.life);
        this.particle = particle;
        this.lifetime = builder.life;
        this.emitCountPerTick = builder.emitCountPerTick;
        this.goreOnly = builder.goreOnly;
        this.goreSpawnRate = (goreOnly ? 1 : builder.goreSpawnRate) / EntityPieceParticle
                .getEntityTexture(entity)
                .map(EntityPieceParticle.EntityTextureInfo::fillRate)
                .orElse(1F);
        var basis = Basis.fromEntityBody(entity);
        this.relPos = basis.toLocal(new Vec3(x, y, z).subtract(entity.position()));
        this.relMotion = basis.toLocal(new Vec3(g, h, i));
        prepared = true;
    }

    public static class Builder {
        private int life, emitCountPerTick;
        private float goreSpawnRate;
        private boolean goreOnly;

        public Builder life(int life) {
            this.life = life;
            return this;
        }

        public Builder emitCountPerTick(int emitCountPerTick) {
            this.emitCountPerTick = emitCountPerTick;
            return this;
        }

        public Builder goreSpawnRate(float rate) {
            this.goreSpawnRate = rate;
            return this;
        }

        public Builder goreOnly() {
            this.goreOnly = true;
            return this;
        }
    }

    @Override
    public void tick() {
        if (!prepared) {
            return;
        }
        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }
        var pos = getEmitPos();
        var vel = getEmitVelocity();
        for (int i = 0; i < emitCountPerTick; i++) {
            if (goreOnly) {
                float cnt = goreSpawnRate;
                while (cnt >= 1) {
                    wrapConfigRate(() -> ENGINE.get().add(new EntityPieceParticle(((TrackingEmitterAccessor) this).getEntity(), level, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z)));
                    cnt -= 1;
                }
                if (cnt > 0 && Math.random() < cnt) {
                    wrapConfigRate(() -> ENGINE.get().add(new EntityPieceParticle(((TrackingEmitterAccessor) this).getEntity(), level, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z)));
                }
                continue;
            }
            wrapConfigRate(() -> level.addParticle(particle, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z));
            if (goreSpawnRate <= 0) {
                continue;
            }
            for (int j = 0; j < 4; j++) {
                if (Math.random() > goreSpawnRate / 4) {
                    continue;
                }
                wrapConfigRate(() -> ENGINE.get().add(new EntityPieceParticle(((TrackingEmitterAccessor) this).getEntity(), level, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z)));
            }
        }
    }

    private void wrapConfigRate(Runnable action) {
        if (Math.random() <= configRate) {
            action.run();
        }
    }

    public Vec3 getEmitPos() {
        Entity boundEntity = ((TrackingEmitterAccessor) this).getEntity();
        return boundEntity.position().add(Basis.fromEntityBody(boundEntity).toGlobal(relPos));
    }

    public Vec3 getEmitVelocity() {
        Entity boundEntity = ((TrackingEmitterAccessor) this).getEntity();
        return Basis.fromEntityBody(boundEntity).toGlobal(relMotion);
    }

    public interface Constructor {
        TrackingEmitter create(ParticleOptions particle, Builder builder, Entity entity, ClientLevel clientLevel, double x, double y, double z, double g, double h, double i);
    }
}
