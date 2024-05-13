package mod.chloeprime.hitfeedback.common;

import dev.architectury.networking.NetworkManager;
import mod.chloeprime.hitfeedback.mixin.LivingEntityAccessor;
import mod.chloeprime.hitfeedback.network.ModNetwork;
import mod.chloeprime.hitfeedback.network.S2CHitFeedback;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class CommonEventHandler {
    public static void onEndAttack(DamageSource source, LivingEntity victim, float amount) {
        if (victim.level().isClientSide()) {
            return;
        }
        var attacker = source.getEntity();
        var bullet = source.getDirectEntity();
        if (attacker == null && bullet == null) {
            return;
        }
        var isRangedAttack = bullet != null && bullet != attacker;
        var position = isRangedAttack ? getBulletHitPosition(bullet, victim) : getMeleeHitPosition(attacker, victim);
        var velocity = 0.2;
        var normal = (isRangedAttack ? bullet.getDeltaMovement() : attacker.getLookAngle().with(Direction.Axis.Y, 0))
                .normalize()
                .scale(-velocity);
        var feedback = HitFeedbackType.match(source, victim, amount > 0);
        if (!feedback.isServerOnly()) {
            var packet = new S2CHitFeedback(victim, feedback, position, normal);
            ((ServerLevel) victim.level()).getChunkSource().broadcast(victim, ModNetwork.CHANNEL.toPacket(NetworkManager.Side.S2C, packet));
        }
        feedback.getHitSound().ifPresent(sound -> {
            var pitch = 1 + (victim.getRandom().nextFloat() - victim.getRandom().nextFloat()) * 0.2f;
            victim.playSound(sound, ((LivingEntityAccessor) victim).invokeGetSoundVolume(), pitch);
        });
    }

    private static Vec3 getBulletHitPosition(@NotNull Entity bullet, Entity victim) {
        var ray = bullet.getDeltaMovement();
        if (ray.lengthSqr() <= 1e-6) {
            return bullet.position();
        }
        var start = bullet.position();
        var end = start.add(ray);
        var len = ray.length();
        var aabb = bullet.getBoundingBox().expandTowards(ray).inflate(1);
        var hit = ProjectileUtil.getEntityHitResult(bullet, start, end, aabb, entity -> entity == victim, len * len);
        if (hit != null) {
            return hit.getLocation();
        }
        var dir = ray.scale(1 / len);
        var ratLength = ray.with(Direction.Axis.Y, 0).length();
        var groundDistance = victim.position().subtract(bullet.position()).with(Direction.Axis.Y, 0).length();
        if (ratLength == 0 || groundDistance == 0) {
            return victim.getEyePosition();
        }
        return bullet.position().add(dir.scale(len * groundDistance / ratLength));
    }

    private static Vec3 getMeleeHitPosition(@NotNull Entity swordsman, Entity victim) {
        var reach = PlatformMethods.getAttackReach(swordsman);
        var ray = swordsman.getLookAngle().scale(reach);
        var start = swordsman.getEyePosition();
        var end = start.add(ray);
        var aabb = swordsman.getBoundingBox().expandTowards(ray).inflate(1);
        var hit = ProjectileUtil.getEntityHitResult(swordsman, start, end, aabb, entity -> entity == victim, reach * reach);
        if (hit != null) {
            return hit.getLocation();
        }
        return victim.position().with(Direction.Axis.Y, Mth.clamp(start.y, victim.getY(), victim.getY() + victim.getBbHeight()));
    }
}
