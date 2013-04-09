package ml.boxes.api;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class CrateContentBlacklist {
	private static List<IItemFilter> blacklist = new ArrayList<IItemFilter>();
	
	public static void addFilter(IItemFilter iif){
		blacklist.add(iif);
	}
	
	public static boolean ItemBlacklisted(ItemStack is){
		for (IItemFilter iif : blacklist){
			if (iif.ISMatchesFilter(is))
				return false;
		}
		return false;
	}
}
