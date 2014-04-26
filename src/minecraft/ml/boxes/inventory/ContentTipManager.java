package ml.boxes.inventory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import ml.boxes.api.box.IContentTipRegistrar;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class ContentTipManager implements IContentTipRegistrar {

	public GuiContainer gContainer;
	
	public final Multimap<Slot, ContentTip> contentTips = HashMultimap.create();
	public final Map<Slot, ItemStack> slotStacks = new HashMap<Slot, ItemStack>(); //Used for detecting changes and causing revalidation

	@Override
	public boolean registerContentTip(Slot owningSlot, ContentTip tip) {
		return contentTips.put(owningSlot, tip);
	}
	
	public void discoverTipProviders() {
		try { //There is room for _something_ to go wrong in here, so probably better safe than crashing the game.
			for (int index=0; index < gContainer.inventorySlots.inventorySlots.size(); index++) {
				Slot slot = (Slot)gContainer.inventorySlots.inventorySlots.get(index);
				ItemStack stack = slot.getStack();
				if (stack != null) {
					//TODO
				}
			}
		} catch (Exception ex) {
			
		}
	}
	
	public void revalidateTips() {
		Iterator<Map.Entry<Slot, ContentTip>> i = contentTips.entries().iterator();
		while (i.hasNext()) {
			Entry<Slot, ContentTip> entry = i.next();
			if (entry.getKey().getStack() != slotStacks.get(entry.getKey()) || !entry.getValue().revalidate(0, 0)) {
				i.remove();
			}
		}
	}
	
}
