package mod.chloeprime.hitfeedback.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

public class SwordFeedbackEmitter extends ParticleEmitterBase {
    private static final Vec3 UP = new Vec3(0, 1, 0);
    private final Vec3 emitterMotion;
    private boolean prepared;
    private static final Map<Entity, Boolean> hitDirections = new WeakHashMap<>();

    public SwordFeedbackEmitter(ParticleOptions particle, Builder builder, Entity boundEntity, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
        super(particle, builder, boundEntity, clientLevel, d, e, f, g, h, i);
        if (relPos.lengthSqr() <= 1e-6) {
            this.emitterMotion = Vec3.ZERO;
            return;
        }
        var direction = hitDirections.compute(boundEntity, (k, v) -> !Objects.requireNonNullElse(v, random.nextBoolean()));
        var relX = UP.cross(relMotion).normalize();
        var angle = Mth.lerp(random.nextDouble(), -Math.PI / 12, Math.PI / 12) + (direction ? Math.PI : 0);
        var offsetDirection = relX.scale(Math.cos(angle)).add(UP.scale(Math.sin(angle)));
        var offsetAmount = 0.5;
        this.relPos = this.relPos.add(offsetDirection.scale(offsetAmount));
        this.emitterMotion = offsetDirection.scale(-2 * offsetAmount / (lifetime - 1));
        this.prepared = true;
    }

    @Override
    public void tick() {
        super.tick();
        if (prepared) {
            relPos = relPos.add(emitterMotion);
        }
    }
}
