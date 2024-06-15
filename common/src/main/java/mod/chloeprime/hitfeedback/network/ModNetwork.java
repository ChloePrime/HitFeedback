package mod.chloeprime.hitfeedback.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.codec.StreamCodec;

public class ModNetwork {
    public static void init() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, S2CHitFeedback.TYPE, StreamCodec.of(S2CHitFeedback::encode, S2CHitFeedback::new), S2CHitFeedback::handle);
    }
}
