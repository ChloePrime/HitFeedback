package mod.chloeprime.hitfeedback.client;

import mod.chloeprime.hitfeedback.client.particles.GunshotFeedbackEmitter;
import mod.chloeprime.hitfeedback.client.particles.ParticleEmitterBase;
import mod.chloeprime.hitfeedback.client.particles.SwordFeedbackEmitter;
import mod.chloeprime.hitfeedback.common.HitFeedbackType;
import mod.chloeprime.hitfeedback.common.HitFeedbackTypes;
import mod.chloeprime.hitfeedback.common.particle.ModParticleTypes;
import mod.chloeprime.hitfeedback.util.ComparableSupplier;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class HitFeedbackActions {
    private HitFeedbackActions() {}

    public static void register(Supplier<HitFeedbackType> type, HitFeedbackAction action) {
        registry.put(new ComparableSupplier<>(type), action);
    }

    public static Optional<HitFeedbackAction> get(HitFeedbackType type) {
        var key = new ComparableSupplier<>(() -> type);
        return Optional.ofNullable(registry.get(key));
    }

    private static final Map<ComparableSupplier<HitFeedbackType>, HitFeedbackAction> registry = new LinkedHashMap<>();

    public static void init() {
        HitFeedbackActions.register(HitFeedbackTypes.FLESH_SWORD, HitFeedbackAction.addEmitter(
                ModParticleTypes.BLOOD,
                swordEmitterBuilder().goreSpawnRate(0.25F),
                SwordFeedbackEmitter::new)
        );
        HitFeedbackActions.register(HitFeedbackTypes.FLESH_GUNSHOT, HitFeedbackAction.addEmitter(
                ModParticleTypes.BLOOD,
                gunshotEmitterBuilder().goreSpawnRate(0.25F),
                GunshotFeedbackEmitter::new)
        );

        HitFeedbackActions.register(HitFeedbackTypes.BONE, HitFeedbackAction.addEmitter(
                new ParticleEmitterBase.Builder().life(1).emitCountPerTick(5).goreOnly(),
                GunshotFeedbackEmitter::new)
        );
        HitFeedbackActions.register(HitFeedbackTypes.METAL, HitFeedbackAction.addEmitter(
                ModParticleTypes.SPARK,
                new ParticleEmitterBase.Builder().life(1).emitCountPerTick(10).goreSpawnRate(0),
                GunshotFeedbackEmitter::new)
        );

        HitFeedbackActions.register(HitFeedbackTypes.SLIME_SWORD, HitFeedbackAction.addEmitter(
                swordEmitterBuilder().goreOnly(),
                SwordFeedbackEmitter::new)
        );
        HitFeedbackActions.register(HitFeedbackTypes.SLIME_GUNSHOT, HitFeedbackAction.addEmitter(
                gunshotEmitterBuilder().goreOnly(),
                GunshotFeedbackEmitter::new)
        );
    }

    public static ParticleEmitterBase.Builder swordEmitterBuilder() {
        return new ParticleEmitterBase.Builder().life(7).emitCountPerTick(5);
    }

    public static ParticleEmitterBase.Builder gunshotEmitterBuilder() {
        return new ParticleEmitterBase.Builder().life(2).emitCountPerTick(8);
    }
}
