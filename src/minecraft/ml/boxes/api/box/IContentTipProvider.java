package ml.boxes.api.box;

import java.util.Collection;

import ml.boxes.inventory.ContentTip;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public interface IContentTipProvider {

	/**
	 * @param slot TODO
	 * @param tips A list to add {@link ContentTip}s to. It also contains the already open tips, so you could use it to prevent from adding duplicate tips
	 * @param tipManager TODO
	 */
	public void createContentTips(Slot slot, ItemStack is, Collection<IContentTip> tips, IContentTipRegistrar tipManager);

}
