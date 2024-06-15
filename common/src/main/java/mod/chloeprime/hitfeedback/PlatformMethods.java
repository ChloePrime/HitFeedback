package mod.chloeprime.hitfeedback;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.registry.client.particle.ParticleProviderRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public class PlatformMethods {
    @ExpectPlatform
    @Environment(EnvType.CLIENT)
    public static <T extends ParticleOptions> void register(RegistrySupplier<? extends ParticleType<T>> supplier, ParticleProviderRegistry.DeferredParticleProvider<T> provider) {
        throw new AssertionError();
    }
}
