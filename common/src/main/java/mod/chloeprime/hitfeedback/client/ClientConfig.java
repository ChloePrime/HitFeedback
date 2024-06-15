package mod.chloeprime.hitfeedback.client;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class ClientConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.DoubleValue PARTICLE_AMOUNT;

    static {
        var builder = new ModConfigSpec.Builder();

        PARTICLE_AMOUNT = builder
                .comment("Relative particle amount (0~1)")
                .defineInRange("particle_amount", 1.0, 0, 1);

        SPEC = builder.build();
    }
}
