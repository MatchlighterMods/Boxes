package ml.boxes.tile.safe;

import java.util.ArrayList;
import java.util.List;

import ml.boxes.api.safe.IMechHandler;
import ml.boxes.api.safe.ISafe;
import ml.boxes.api.safe.SafeMechanism;
import net.minecraft.item.ItemStack;

public class MechRegistry {
	
	public static ArrayList<IMechHandler> handlers = new ArrayList<IMechHandler>();
	
	public static SafeMechanism getMechForStack(ItemStack is, ISafe safe) {
		for (IMechHandler hndlr : handlers) {
			if (hndlr.handlesMechForStack(is))
				return hndlr.createMechanism(safe, is.stackTagCompound);
		}
		return new MechFallback(safe);
	}
	
	public static boolean isAMechanism(ItemStack is) {
		for (IMechHandler hndlr : handlers) {
			if (hndlr.handlesMechForStack(is))
				return true;
		}
		return false;
	}
	
	public static void getInfoForSafe(ItemStack mech, ItemStack safe, List lst) {
		for (IMechHandler hndlr : handlers) {
			if (hndlr.handlesMechForStack(mech))
				hndlr.addInfoToSafe(mech, safe, lst);
		}
	}

}
