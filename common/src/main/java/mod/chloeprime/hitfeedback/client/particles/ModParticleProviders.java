package mod.chloeprime.hitfeedback.client.particles;

import dev.architectury.registry.client.particle.ParticleProviderRegistry;
import mod.chloeprime.hitfeedback.common.particle.ModParticleTypes;

public class ModParticleProviders {
    private ModParticleProviders() {}

    public static void init() {
        ParticleProviderRegistry.register(ModParticleTypes.BLOOD, new BloodParticle.Provider());
        ParticleProviderRegistry.register(ModParticleTypes.SPARK, new SparkParticle.Provider());
    }
}
