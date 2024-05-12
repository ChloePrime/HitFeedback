package mod.chloeprime.hitfeedback.common;

import dev.architectury.core.RegistryEntry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
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

public class HitFeedbackType extends RegistryEntry<HitFeedbackType> {
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
        var isGunshot = source instanceof IndirectEntityDamageSource;
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

    private static boolean isWeapon(ItemStack stack, EquipmentSlot hand) {
        return stack.getItem().getDefaultAttributeModifiers(hand).get(Attributes.ATTACK_DAMAGE).stream()
                .anyMatch(mdf -> mdf.getOperation() == AttributeModifier.Operation.ADDITION && mdf.getAmount() > 0);
    }
}
