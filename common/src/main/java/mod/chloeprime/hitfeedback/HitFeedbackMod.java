package mod.chloeprime.hitfeedback;

import mod.chloeprime.hitfeedback.common.HitFeedbackTypes;
import mod.chloeprime.hitfeedback.common.ModSoundEvents;
import mod.chloeprime.hitfeedback.common.particle.ModParticleTypes;
import mod.chloeprime.hitfeedback.network.ModNetwork;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class HitFeedbackMod {
	private HitFeedbackMod() {}

	public static final String MOD_ID = "hit_feedback";
	public static final TagKey<Item> SHARP_WEAPONS = TagKey.create(Registries.ITEM, loc("sharp_weapons"));
	public static final TagKey<Item> SHARP_WEAPONS_IF_HAS_BUKKIT = TagKey.create(Registries.ITEM, loc("sharp_weapons_if_have_bukkit"));
	public static final boolean HAS_BUKKIT = hasClass("org.bukkit.Bukkit");

	public static ResourceLocation loc(String path) {
		return new ResourceLocation(MOD_ID, path);
	}

	@SuppressWarnings("RedundantIfStatement")
    public static boolean isSharpWeapon(ItemStack item) {
		if (item.is(SHARP_WEAPONS)) {
			return true;
		}
		if (HAS_BUKKIT && item.is(SHARP_WEAPONS_IF_HAS_BUKKIT)) {
			return true;
		}
		return false;
	}

	public static void init() {
		ModSoundEvents.DFR.register();
		ModParticleTypes.DFR.register();
		HitFeedbackTypes.DFR.register();
		ModNetwork.init();
	}

	@SuppressWarnings("SameParameterValue")
    private static boolean hasClass(String fullName) {
		try {
			Class.forName(fullName);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
        }
    }
}
