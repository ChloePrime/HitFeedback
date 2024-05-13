package mod.chloeprime.hitfeedback.common;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import mod.chloeprime.hitfeedback.HitFeedbackMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import static mod.chloeprime.hitfeedback.HitFeedbackMod.MOD_ID;

public interface HitFeedbackTypes {
    Registrar<HitFeedbackType> REGISTRY = RegistrarManager.get(MOD_ID)
            .<HitFeedbackType>builder(HitFeedbackMod.loc("types"))
            .build();

    @SuppressWarnings("unchecked")
    DeferredRegister<HitFeedbackType> DFR = DeferredRegister.create(MOD_ID, (ResourceKey<Registry<HitFeedbackType>>) REGISTRY.key());

    RegistrySupplier<HitFeedbackType> PUNCH = DFR.register("flesh_punch", () -> new HitFeedbackType(
            ModSoundEvents.FLESH_PUNCH_HIT
    ));

    RegistrySupplier<HitFeedbackType> FLESH_SWORD = DFR.register("flesh_sword", () -> new HitFeedbackType(
            ModSoundEvents.FLESH_SWORD_HIT
    ));

    RegistrySupplier<HitFeedbackType> FLESH_GUNSHOT = DFR.register("flesh_gunshot", () -> new HitFeedbackType(
            ModSoundEvents.FLESH_GUNSHOT
    ));

    RegistrySupplier<HitFeedbackType> BONE = DFR.register("bone", () -> new HitFeedbackType(
            null,
            TagKey.create(Registries.ENTITY_TYPE, HitFeedbackMod.loc("type/bone"))
    ));

    RegistrySupplier<HitFeedbackType> METAL = DFR.register("metal", () -> new HitFeedbackType(
            null,
            TagKey.create(Registries.ENTITY_TYPE, HitFeedbackMod.loc("type/metal"))
    ));

    RegistrySupplier<HitFeedbackType> SLIME_SWORD = DFR.register("slime_sword", () -> new HitFeedbackType(null));

    RegistrySupplier<HitFeedbackType> SLIME_GUNSHOT = DFR.register("slime_gunshot", () -> new HitFeedbackType(
            null,
            TagKey.create(Registries.ENTITY_TYPE, HitFeedbackMod.loc("type/slime"))
    ));

    RegistrySupplier<HitFeedbackType> METAL_FAILURE = DFR.register("metal_failure", () -> new HitFeedbackType(
            ModSoundEvents.METAL_FAILURE
    ));
}
