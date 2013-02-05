package ml.boxes.api;

import net.minecraft.item.ItemStack;

public interface IItemFilter {

	public boolean ISMatchesFilter(ItemStack is);
}