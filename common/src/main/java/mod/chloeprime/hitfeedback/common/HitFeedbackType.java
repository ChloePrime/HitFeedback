package mod.chloeprime.hitfeedback.common;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

import static mod.chloeprime.hitfeedback.common.HitFeedbackTypes.*;

public class HitFeedbackType {
    private final @Nullable TagKey<EntityType<?>> tag;
    private final @Nullable Supplier<SoundEvent> sound;
    private final Set<Options> options;

    public interface Options {
        Options SERVER_ONLY = new Options() {};
    }

    public HitFeedbackType(@Nullable Supplier<SoundEvent> sound, Options... options) {
        this(sound, null, options);
    }

    public HitFeedbackType(@Nullable Supplier<SoundEvent> sound, @Nullable TagKey<EntityType<?>> tag, Options... options) {
        this.tag = tag;
        this.sound = sound;
        this.options = Collections.newSetFromMap(new IdentityHashMap<>());
        this.options.addAll(List.of(options));
    }

    public Optional<SoundEvent> getHitSound() {
        return Optional.ofNullable(sound).map(Supplier::get);
    }

    public boolean isServerOnly() {
        return options.contains(Options.SERVER_ONLY);
    }

    public static HitFeedbackType match(DamageSource source, LivingEntity victim, boolean valid) {
        return HitFeedbackTypes.REGISTRY.entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(type -> type.tag != null && victim.getType().is(type.tag))
                .findFirst()
                .orElseGet(() -> matchDefault(source, victim, valid));
    }

    private static HitFeedbackType matchDefault(DamageSource source, LivingEntity victim, boolean valid) {
        var attacker = source.getEntity();
        var isGunshot = !source.isDirect();
        var isHoldingSword = !isGunshot && isHoldingSword(attacker);
        if (!valid) {
            return isHoldingSword ? METAL_FAILURE.get() : PUNCH.get();
        }
        if (victim instanceof AbstractSkeleton) {
            return BONE.get();
        }
        if (victim instanceof Slime) {
            return isHoldingSword ? SLIME_SWORD.get() : SLIME_GUNSHOT.get();
        }
        return isGunshot
                ? FLESH_GUNSHOT.get()
                : (isHoldingSword ? FLESH_SWORD.get() : PUNCH.get());
    }

    private static boolean isHoldingSword(Entity attacker) {
        if (!(attacker instanceof LivingEntity livingAttacker)) {
            return false;
        }
        return isWeapon(livingAttacker.getMainHandItem(), EquipmentSlot.MAINHAND) || isWeapon(livingAttacker.getOffhandItem(), EquipmentSlot.OFFHAND);
    }

    @SuppressWarnings("deprecation")
    private static boolean isWeapon(ItemStack stack, EquipmentSlot hand) {
        boolean[] result = new boolean[1];
        stack.getItem().getDefaultInstance().forEachModifier(hand, (attribute, modifier) -> {
            if (result[0]) {
                return;
            }
            if (!attribute.is(Attributes.ATTACK_DAMAGE) || modifier.operation() != AttributeModifier.Operation.ADD_VALUE) {
                return;
            }
            if (modifier.amount() > 0) {
                result[0] = true;
            }
        });
        return result[0];
    }
}
