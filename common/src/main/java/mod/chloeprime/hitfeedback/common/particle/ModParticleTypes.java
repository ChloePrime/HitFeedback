package mod.chloeprime.hitfeedback.common.particle;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import mod.chloeprime.hitfeedback.HitFeedbackMod;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;

public interface ModParticleTypes {
    DeferredRegister<ParticleType<?>> DFR = DeferredRegister.create(HitFeedbackMod.MOD_ID, Registries.PARTICLE_TYPE);
    RegistrySupplier<SimpleParticleType> BLOOD = DFR.register("blood", SimpleParticleType::new);
    RegistrySupplier<SimpleParticleType> SPARK = DFR.register("spark", SimpleParticleType::new);
}
