package mod.chloeprime.hitfeedback.common.particle;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import mod.chloeprime.hitfeedback.HitFeedbackMod;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;

public interface ModParticleTypes {
    DeferredRegister<ParticleType<?>> DFR = DeferredRegister.create(HitFeedbackMod.MOD_ID, Registry.PARTICLE_TYPE_REGISTRY);
    RegistrySupplier<SimpleParticleType> BLOOD = DFR.register("blood", SimpleParticleType::new);
    RegistrySupplier<SimpleParticleType> SPARK = DFR.register("spark", SimpleParticleType::new);
}
