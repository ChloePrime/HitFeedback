package mod.chloeprime.hitfeedback.client.particles;

import dev.architectury.registry.client.particle.ParticleProviderRegistry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class BloodParticle extends SimpleTexturedParticle {
    public BloodParticle(SpriteSet sprite, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
        super(sprite, clientLevel, d, e, f, g, h, i);
        this.lifetime += random.nextInt(40, 100);
    }

    public static final class Provider implements ParticleProviderRegistry.DeferredParticleProvider<SimpleParticleType> {
        @Override
        public ParticleProvider<SimpleParticleType> create(ParticleProviderRegistry.ExtendedSpriteSet spriteSet) {
            return (options, clientLevel, d, e, f, g, h, i) -> new BloodParticle(spriteSet, clientLevel, d, e, f, g, h, i);
        }
    }
}
