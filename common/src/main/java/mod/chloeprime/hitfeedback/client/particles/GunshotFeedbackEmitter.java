package mod.chloeprime.hitfeedback.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.Entity;

public class GunshotFeedbackEmitter extends ParticleEmitterBase {
    public GunshotFeedbackEmitter(ParticleOptions particle, Builder builder, Entity boundEntity, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
        super(particle, builder, boundEntity, clientLevel, d, e, f, g, h, i);
    }
}
