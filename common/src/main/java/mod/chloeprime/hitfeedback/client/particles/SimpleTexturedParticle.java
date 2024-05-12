package mod.chloeprime.hitfeedback.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class SimpleTexturedParticle extends TextureSheetParticle {
    public SimpleTexturedParticle(SpriteSet sprite, ClientLevel clientLevel, double d, double e, double f, double xv, double yv, double zv) {
        super(clientLevel, d, e, f, xv, yv, zv);
        this.sprite = sprite;
        this.gravity = 1;
        setSpriteFromAge(sprite);

        var oldVelocity = 1 / Mth.fastInvSqrt(xd * xd + yd * yd + zd * zd);
        var newVelocity = 1 / Mth.fastInvSqrt(xv * xv + yv * yv + zv * zv);
        var velScale = newVelocity / oldVelocity;
        this.xd *= velScale;
        this.yd *= velScale;
        this.zd *= velScale;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        super.tick();
        setSpriteFromAge(sprite);
    }

    private final SpriteSet sprite;
}
