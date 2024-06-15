package mod.chloeprime.hitfeedback.client.particles;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import mod.chloeprime.hitfeedback.client.internal.SizedTexture;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL13;

import java.util.LinkedHashMap;
import java.util.Map;
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
        this.valid = tex.filter(t -> t.width >= SIZE && t.height >= SIZE).isPresent();
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
                .map(MC.getTextureManager()::getTexture)
                .map(tex -> tex instanceof SizedTexture simple ? simple : null)
                .map(tex -> new EntityTextureInfo(texture.get(), tex.hit_feedback$getWidth(), tex.hit_feedback$getHeight(), tex.hit_feedback$getFillRate()));
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    private static final Map<ResourceLocation, BufferRecord> BUFFER_TABLE = new LinkedHashMap<>();
    private static PoseStack POSE;

    private static class BufferRecord {
        public final Tesselator tesselator;
        public boolean hasContent;

        private BufferRecord(Tesselator tesselator) {
            this.tesselator = tesselator;
        }
    }

    public static void beforeRender(PoseStack pose) {
        POSE = pose;
    }

    public static void doRender() {
        withRenderSystemShit(() -> {
            var texManager = Minecraft.getInstance().getTextureManager();
            BUFFER_TABLE.forEach((texture, rec) -> {
                if (!rec.hasContent) {
                    return;
                }
                rec.hasContent = false;

                RenderSystem.setShaderTexture(0, texture);
                RenderSystem.applyModelViewMatrix();

                ParticleRenderType.CUSTOM.begin(rec.tesselator.getBuilder(), texManager);
                rec.tesselator.end();
            });
        });
    }

    private static void withRenderSystemShit(Runnable code) {
        if (POSE == null) {
            return;
        }
        RenderSystem.enableDepthTest();
        RenderSystem.activeTexture(GL13.GL_TEXTURE2);
        RenderSystem.activeTexture(GL13.GL_TEXTURE0);
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        PoseStack pose = RenderSystem.getModelViewStack();
        pose.pushPose();
        pose.mulPoseMatrix(POSE.last().pose());
        RenderSystem.applyModelViewMatrix();
        code.run();
        pose.popPose();
    }

    @Override
    public void render(VertexConsumer ignored, Camera renderInfo, float partialTicks) {
        if (!valid) {
            return;
        }
        var data = BUFFER_TABLE.computeIfAbsent(texture, tex -> new BufferRecord(new Tesselator(0x4000)));
        var buffer = data.tesselator.getBuilder();
        if (!data.hasContent) {
            buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
            data.hasContent = true;
        }
        super.render(buffer, renderInfo, partialTicks);
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
