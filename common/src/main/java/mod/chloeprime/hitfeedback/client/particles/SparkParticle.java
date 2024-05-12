package mod.chloeprime.hitfeedback.client.particles;

import dev.architectury.registry.client.particle.ParticleProviderRegistry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class SparkParticle extends SimpleTexturedParticle {
    public SparkParticle(SpriteSet sprite, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
        super(sprite, clientLevel, d, e, f, 1.5 * g, 1.5 * h, 1.5 * i);
        this.lifetime = 10;
        this.quadSize /= 8;
    }

    public static final class Provider implements ParticleProviderRegistry.DeferredParticleProvider<SimpleParticleType> {
        @Override
        public ParticleProvider<SimpleParticleType> create(ParticleProviderRegistry.ExtendedSpriteSet spriteSet) {
            return (options, clientLevel, d, e, f, g, h, i) -> new SparkParticle(spriteSet, clientLevel, d, e, f, g, h, i);
        }
    }
}
