package ml.boxes.inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import ml.boxes.api.box.IContentTip;
import ml.boxes.api.box.IContentTipProvider;
import ml.boxes.api.box.IContentTipRegistrar;
import ml.core.vec.Rectangle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ContentTipManager implements IContentTipRegistrar {

	public GuiContainer gContainer;
	public Rectangle guiBounds = new Rectangle(0, 0, 0, 0); 
	public static ContentTipManager instance;

	public final Multimap<Slot, IContentTip> contentTips = HashMultimap.create();
	public final ArrayList<IContentTip> tipZOrder = new ArrayList<IContentTip>();
	public final Map<Slot, ItemStack> slotStacks = new HashMap<Slot, ItemStack>(); //Used for detecting changes and causing revalidation
	
	public ContentTipManager(GuiContainer gc) {
		gContainer = gc;
	}
	
	@Override
	public boolean registerContentTip(Slot slot, IContentTip tip) {
		return contentTips.put(slot, tip);
	}
	
	@Override
	public Collection<IContentTip> getTipsForSlot(Slot slot) {
		return contentTips.get(slot);
	}

	@Override
	public GuiContainer getGuiContainer() {
		return gContainer;
	}
	
	public void discoverTipProviders() {
		try { //There is room for _something_ to go wrong in here, so probably better safe than crashing the game.
			for (int index=0; index < gContainer.inventorySlots.inventorySlots.size(); index++) {
				Slot slot = (Slot)gContainer.inventorySlots.inventorySlots.get(index);
				ItemStack stack = slot.getStack();
				if (stack != null && slotStacks.get(slot) != stack) {
					slotStacks.put(slot, stack);
					if (stack.getItem() instanceof IContentTipProvider) { // TODO Prevent opening the tip of the current box
						((IContentTipProvider)stack.getItem()).createContentTips(slot, stack, contentTips.get(slot), this);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public boolean revalidateTips() {
		if (FMLClientHandler.instance().getClient().currentScreen != gContainer) {
			instance = null;
			return false;
		} else {
			Iterator<Map.Entry<Slot, IContentTip>> i = contentTips.entries().iterator();
			while (i.hasNext()) {
				Entry<Slot, IContentTip> entry = i.next();
				if (!gContainer.inventorySlots.inventorySlots.contains(entry.getKey()) ||
						entry.getKey().getStack() == null ||
						!ItemStack.areItemStacksEqual(entry.getKey().getStack(), slotStacks.get(entry.getKey())) ||
						!ItemStack.areItemStackTagsEqual(entry.getKey().getStack(), slotStacks.get(entry.getKey())) ||
						//entry.getKey().getStack() != slotStacks.get(entry.getKey()) ||
						!entry.getValue().revalidate()) {
					
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
			
			for (IContentTip ct : contentTips.values()) {
				ct.tick(mc);
			}
		}
	}

	public void doRender(int mousex, int mousey) {
		Minecraft mc = FMLClientHandler.instance().getClient();
		if (revalidateTips()) {
			for (IContentTip ct : contentTips.values()) {
				if (ct.isVisible()) ct.renderTick(mc, mousex, mousey);
			}
		}
	}
	
	public IContentTip getTipAt(int x, int y, boolean updateZ) {
		for (IContentTip ict : contentTips.values()) {
			if (!tipZOrder.contains(ict)) tipZOrder.add(ict);
		}
		for (int i=tipZOrder.size()-1; i>=0; i--) {
			IContentTip ict = tipZOrder.get(i);
			if (ict.isVisible() && ict.isPointInside(x, y)) {
				if (updateZ) {
					tipZOrder.remove(ict);
					tipZOrder.add(ict);
				}
				return ict;
			}
		}
		return null;
	}
	
	public static class TickHandler implements ITickHandler {

		@Override
		public void tickStart(EnumSet<TickType> type, Object... tickData) {
		}

		@Override
		public void tickEnd(EnumSet<TickType> type, Object... tickData) {
			if (instance != null) instance.doTick();
		}

		@Override
		public EnumSet<TickType> ticks() {
			return EnumSet.of(TickType.CLIENT);
		}

		@Override
		public String getLabel() {
			return "ConententTips";
		}
		
	}
	
	static {
		TickRegistry.registerTickHandler(new TickHandler(), Side.CLIENT);
	}
	
//	Minecraft mc = FMLClientHandler.instance().getClient();
//	if (mc.currentScreen instanceof GuiContainer){
//		Vector2i m = GeoMath.getScaledMouse();
//		GL11.glPushMatrix();
//		GL11.glTranslatef(0F, 0F, 200F);
//		renderContentTip(mc, m.x, m.y, (Float)tickData[0]);
//		GL11.glPopMatrix();
//	}
	
}
