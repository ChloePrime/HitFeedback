package mod.chloeprime.hitfeedback.network;

import dev.architectury.networking.NetworkManager;
import mod.chloeprime.hitfeedback.client.HitFeedbackClient;
import mod.chloeprime.hitfeedback.common.HitFeedbackType;
import mod.chloeprime.hitfeedback.common.HitFeedbackTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public class S2CHitFeedback {
    public final int entityId;
    public final HitFeedbackType type;
    public final Vec3 position;
    public final Vec3 velocity;

    public S2CHitFeedback(Entity entity, HitFeedbackType type, Vec3 position, Vec3 velocity) {
        this.entityId = entity.getId();
        this.type = type;
        this.position = position;
        this.velocity = velocity;
    }

    @NotNull
    public Entity getEntity(Level level) {
        return Optional.ofNullable(level.getEntity(entityId)).orElseThrow(() -> new IllegalStateException("Invalid entity ID"));
    }

    public S2CHitFeedback(FriendlyByteBuf buf) {
        this.entityId = buf.readVarInt();
        var fbTypeId = buf.readResourceLocation();
        var posX = buf.readDouble();
        var posY = buf.readDouble();
        var posZ = buf.readDouble();
        var normalX = buf.readDouble();
        var normalY = buf.readDouble();
        var normalZ = buf.readDouble();

        this.type = Optional.ofNullable(HitFeedbackTypes.REGISTRY.get(fbTypeId)).orElseThrow(() -> new IllegalStateException("Unknown feedback type: %s".formatted(fbTypeId)));
        this.position = new Vec3(posX, posY, posZ);
        this.velocity = new Vec3(normalX, normalY, normalZ);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(entityId);
        buf.writeResourceLocation(Optional.ofNullable(HitFeedbackTypes.REGISTRY.getId(type)).orElseThrow(() -> new IllegalStateException("Unregistered feedback type: %s".formatted(type))));
        buf.writeDouble(position.x);
        buf.writeDouble(position.y);
        buf.writeDouble(position.z);
        buf.writeDouble(velocity.x);
        buf.writeDouble(velocity.y);
        buf.writeDouble(velocity.z);
    }

    public void handle(Supplier<NetworkManager.PacketContext> ctx) {
        ctx.get().queue(() -> HitFeedbackClient.handleFeedbackPacket(this, ctx.get()));
    }
}
