package mod.chloeprime.hitfeedback.client.particles;

import dev.architectury.registry.client.particle.ParticleProviderRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import mod.chloeprime.hitfeedback.common.particle.ModParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public class ModParticleProviders {
    private ModParticleProviders() {}

    @FunctionalInterface
    public interface RegisterMethod {
        <O extends ParticleOptions, T extends ParticleType<O>>
        void register(RegistrySupplier<T> supplier, ParticleProviderRegistry.DeferredParticleProvider<O> provider);
    }

    public static void init(RegisterMethod method) {
        method.register(ModParticleTypes.BLOOD, new BloodParticle.Provider());
        method.register(ModParticleTypes.SPARK, new SparkParticle.Provider());
    }
}
