package mod.chloeprime.hitfeedback.network;

import dev.architectury.networking.NetworkManager;
import mod.chloeprime.hitfeedback.HitFeedbackMod;
import mod.chloeprime.hitfeedback.client.HitFeedbackClient;
import mod.chloeprime.hitfeedback.common.HitFeedbackType;
import mod.chloeprime.hitfeedback.common.HitFeedbackTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class S2CHitFeedback implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<S2CHitFeedback> TYPE = new Type<>(HitFeedbackMod.loc("feedback"));

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

    public S2CHitFeedback(RegistryFriendlyByteBuf buf) {
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

    public static void encode(RegistryFriendlyByteBuf buf, S2CHitFeedback self) {
        buf.writeVarInt(self.entityId);
        buf.writeResourceLocation(Optional.ofNullable(HitFeedbackTypes.REGISTRY.getId(self.type)).orElseThrow(() -> new IllegalStateException("Unregistered feedback type: %s".formatted(self.type))));
        buf.writeDouble(self.position.x);
        buf.writeDouble(self.position.y);
        buf.writeDouble(self.position.z);
        buf.writeDouble(self.velocity.x);
        buf.writeDouble(self.velocity.y);
        buf.writeDouble(self.velocity.z);
    }

    public void handle(NetworkManager.PacketContext ctx) {
        ctx.queue(() -> HitFeedbackClient.handleFeedbackPacket(this, ctx));
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
