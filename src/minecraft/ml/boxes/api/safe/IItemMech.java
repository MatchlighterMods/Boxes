package ml.boxes.api.safe;

import net.minecraft.item.ItemStack;

public interface IItemMech {

	/**
	 * Get a unique and unchanging mechanism Id for the stack
	 */
	public String getMechID(ItemStack mechStack);
	
}
