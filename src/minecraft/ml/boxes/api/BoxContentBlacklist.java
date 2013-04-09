package ml.boxes.api;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * This class is used to prevent your mod's items from being a placed in a box.
 * @author Matchlighter
 *
 */
public class BoxContentBlacklist {
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
