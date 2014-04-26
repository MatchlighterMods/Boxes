package ml.boxes.api.box;

import net.minecraft.inventory.Slot;
import ml.boxes.inventory.ContentTip;

public interface IContentTipRegistrar {

	public boolean registerContentTip(Slot owningSlot, ContentTip tip);
	
}
