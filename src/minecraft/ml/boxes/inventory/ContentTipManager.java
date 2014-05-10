package ml.boxes.inventory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import ml.boxes.api.box.IContentTipRegistrar;
import ml.core.vec.Rectangle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;

public class ContentTipManager implements IContentTipRegistrar {

	public GuiContainer gContainer;
	public Rectangle guiBounds = new Rectangle(0, 0, 0, 0); 
	public static ContentTipManager instance;

	public final Multimap<Slot, ContentTip> contentTips = HashMultimap.create();
	public final Map<Slot, ItemStack> slotStacks = new HashMap<Slot, ItemStack>(); //Used for detecting changes and causing revalidation
	
	public ContentTipManager(GuiContainer gc) {
		gContainer = gc;
	}
	
	@Override
	public boolean registerContentTip(Slot slot, ContentTip tip) {
		return contentTips.put(slot, tip);
	}

	public void discoverTipProviders() {
		try { //There is room for _something_ to go wrong in here, so probably better safe than crashing the game.
			for (int index=0; index < gContainer.inventorySlots.inventorySlots.size(); index++) {
				Slot slot = (Slot)gContainer.inventorySlots.inventorySlots.get(index);
				ItemStack stack = slot.getStack();
				if (stack != null && slotStacks.get(slot) != stack) {
					slotStacks.put(slot, stack);
					//TODO Create Tips
				}
			}
		} catch (Exception ex) {

		}
	}

	public boolean revalidateTips() {
		if (FMLClientHandler.instance().getClient().currentScreen != gContainer) {
			instance = null;
			return false;
		} else {
			Iterator<Map.Entry<Slot, ContentTip>> i = contentTips.entries().iterator();
			while (i.hasNext()) {
				Entry<Slot, ContentTip> entry = i.next();
				if (entry.getKey().getStack() != slotStacks.get(entry.getKey()) || !entry.getValue().revalidate(0, 0)) {
					i.remove();
					slotStacks.remove(entry.getKey());
				}
			}
		}
		return true;
	}

	public void doTick() {
		Minecraft mc = FMLClientHandler.instance().getClient();
		if (revalidateTips()) {
			discoverTipProviders();
			
			int guiXSize = ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, gContainer, "field_" + "74194_b", "xSize", "b");
			int guiYSize = ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, gContainer, "field_" + "74195_c", "ySize", "c");
			
			guiBounds.width = gContainer.width;
			guiBounds.height = gContainer.height;
			guiBounds.xCoord = (gContainer.width - guiXSize) / 2;
			guiBounds.yCoord = (gContainer.height - guiYSize) / 2;
			
			for (ContentTip ct : contentTips.values()) {
				ct.tick(mc);
			}
		}
	}

	public void doRender(int mousex, int mousey) {
		Minecraft mc = FMLClientHandler.instance().getClient();
		if (revalidateTips()) {
			for (ContentTip ct : contentTips.values()) {
				ct.renderTick(mc, mousex, mousey);
			}
		}
	}
}
