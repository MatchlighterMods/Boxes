package ml.boxes.api.box;

import java.util.Collection;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;

public interface IContentTipRegistrar {

	public boolean registerContentTip(Slot slot, IContentTip tip);

	public abstract Collection<IContentTip> getTipsForSlot(Slot slot);

	public abstract GuiContainer getGuiContainer();
	
}
