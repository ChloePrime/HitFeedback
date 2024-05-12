package mod.chloeprime.hitfeedback.mixin.client;

import net.minecraft.client.particle.TrackingEmitter;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TrackingEmitter.class)
public interface TrackingEmitterAccessor {
    @Accessor Entity getEntity();
}
