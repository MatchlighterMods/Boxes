package ml.boxes.api.box;

import ml.boxes.inventory.ContentTip;
import net.minecraft.inventory.Slot;

public interface IContentTipRegistrar {

	public boolean registerContentTip(Slot slot, ContentTip tip);
	
}
