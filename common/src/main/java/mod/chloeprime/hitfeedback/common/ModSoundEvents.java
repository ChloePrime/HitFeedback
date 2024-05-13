package mod.chloeprime.hitfeedback.common;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import mod.chloeprime.hitfeedback.HitFeedbackMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface ModSoundEvents {
    DeferredRegister<SoundEvent> DFR = DeferredRegister.create(HitFeedbackMod.MOD_ID, Registries.SOUND_EVENT);
    RegistrySupplier<SoundEvent> FLESH_PUNCH_HIT = register("feedback.flesh.punch");
    RegistrySupplier<SoundEvent> FLESH_SWORD_HIT = register("feedback.flesh.sword");
    RegistrySupplier<SoundEvent> FLESH_GUNSHOT = register("feedback.flesh.gunshot");
    RegistrySupplier<SoundEvent> METAL = register("feedback.metal");
    RegistrySupplier<SoundEvent> METAL_FAILURE = register("feedback.metal.failure");

    private static RegistrySupplier<SoundEvent> register(String id) {
        return DFR.register(id, () -> SoundEvent.createVariableRangeEvent(HitFeedbackMod.loc(id)));
    }
}
