package ml.boxes.api;

import net.minecraft.item.ItemStack;

/**
 * This is a basic filter for the blacklist. Disallows items if:
 * 	1) they match the input itemID and Damage OR
 * 	2) they match the input itemID and the input Damage is -1
 * @author Matchlighter
 *
 */
public class BasicItemFilter implements IItemFilter {

	private final ItemStack isFilter;
	
	public BasicItemFilter(ItemStack is) {
		isFilter = is;
	}
	
	@Override
	public boolean ISMatchesFilter(ItemStack is) {
		return isFilter.isItemEqual(is) || (isFilter.itemID == is.itemID && isFilter.getItemDamage() == Short.MAX_VALUE);
	}

}
