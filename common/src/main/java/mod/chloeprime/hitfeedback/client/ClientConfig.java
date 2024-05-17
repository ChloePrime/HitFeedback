package mod.chloeprime.hitfeedback.client;

import net.minecraftforge.common.ForgeConfigSpec;

public final class ClientConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.DoubleValue PARTICLE_AMOUNT;

    static {
        var builder = new ForgeConfigSpec.Builder();

        PARTICLE_AMOUNT = builder
                .comment("Relative particle amount (0~1)")
                .defineInRange("particle_amount", 1.0, 0, 1);

        SPEC = builder.build();
    }
}
