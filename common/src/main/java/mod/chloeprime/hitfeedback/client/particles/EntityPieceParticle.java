package mod.chloeprime.hitfeedback.client.particles;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import mod.chloeprime.hitfeedback.client.internal.SizedTexture;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static mod.chloeprime.hitfeedback.client.MinecraftHolder.MC;

public class EntityPieceParticle extends SingleQuadParticle {
    public static final int SIZE = 4;

    protected EntityPieceParticle(Entity entity, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.quadSize /= 2;
        this.gravity = 1;
        this.lifetime += random.nextInt(40, 100);

        var tex = getEntityTexture(entity);
        this.valid = tex.isPresent();
        this.texture = valid ? tex.get().texture : MissingTextureAtlasSprite.getLocation();
        var w = valid ? tex.get().width : 1;
        var h = valid ? tex.get().height : 1;
        this.u0 = valid ? random.nextInt(w - SIZE + 1) / (float) w : 0;
        this.v0 = valid ? random.nextInt(h - SIZE + 1) / (float) h : 0;
        this.u1 = u0 + SIZE / (float) w;
        this.v1 = v0 + SIZE / (float) h;
    }

    public record EntityTextureInfo(
            ResourceLocation texture,
            int width,
            int height,
            float fillRate
    ) {
    }

    private final ResourceLocation texture;
    private final boolean valid;
    private final float u0, v0;
    private final float u1, v1;

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Optional<EntityTextureInfo> getEntityTexture(Entity entity) {
        var texture = Optional
                .ofNullable(entity)
                .map(MC.getEntityRenderDispatcher()::getRenderer)
                .map((EntityRenderer d) -> d.getTextureLocation(entity));

        if (texture.isEmpty()) {
            return Optional.empty();
        }

        return texture
                .map(tex -> MC.getTextureManager().getTexture(tex))
                .map(tex -> tex instanceof SizedTexture simple ? simple : null)
                .map(tex -> new EntityTextureInfo(texture.get(), tex.hit_feedback$getWidth(), tex.hit_feedback$getHeight(), tex.hit_feedback$getFillRate()));
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        if (!valid || !(buffer instanceof BufferBuilder builder)) {
            return;
        }
        RenderSystem.setShaderTexture(0, texture);
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        super.render(buffer, renderInfo, partialTicks);
        BufferUploader.drawWithShader(builder.end());
    }

    @Override
    protected float getU0() {
        return u0;
    }

    @Override
    protected float getU1() {
        return u1;
    }

    @Override
    protected float getV0() {
        return v0;
    }

    @Override
    protected float getV1() {
        return v1;
    }
}
