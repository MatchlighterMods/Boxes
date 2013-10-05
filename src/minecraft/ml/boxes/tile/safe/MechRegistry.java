package ml.boxes.tile.safe;

import java.util.HashMap;
import java.util.List;

import ml.boxes.api.safe.SafeMechanism;
import ml.core.item.StackUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.common.FMLLog;

public class MechRegistry {
	
	private static HashMap<String, SafeMechanism> mechs = new HashMap<String, SafeMechanism>();
	public static MechFallback fallback = new MechFallback();
	
	public static boolean registerMech(String mechId, SafeMechanism smech) {
		if (mechs.containsKey(mechId)) return false;
		FMLLog.info("Registering SafeMechanism \"%s\"", mechId);
		mechs.put(mechId, smech);
		return true;
	}
	
	public static SafeMechanism getMechForId(String mId) {
		if (mechs.containsKey(mId))
			return mechs.get(mId);
		return fallback;
	}

	public static void addInfoForSafe(NBTTagCompound mechTag, ItemStack safeStack, List lst) {
		String mechId = StackUtils.getStackTag(safeStack).getString("mech_id");
		SafeMechanism mech = getMechForId(mechId); 
		lst.add("Mechanism: " + EnumChatFormatting.ITALIC.toString() + EnumChatFormatting.WHITE + mech.getLocalizedName());
		mech.addInfoForSafe(mechTag, lst);
	}

	public static boolean isIdRegistered(String mId) {
		return mechs.containsKey(mId);
	}
}
