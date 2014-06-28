package ml.boxes.nei;

import ml.boxes.Boxes;
import ml.boxes.Registry;
import ml.boxes.api.box.IContentTip;
import ml.boxes.block.MetaType;
import ml.boxes.inventory.ContentTipManager;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import codechicken.nei.MultiItemRange;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.api.INEIGuiAdapter;
import codechicken.nei.forge.GuiContainerManager;
import codechicken.nei.forge.IContainerDrawHandler;
import codechicken.nei.forge.IContainerInputHandler;
import codechicken.nei.forge.IContainerObjectHandler;

public class NEI_Boxes_Config implements IConfigureNEI {

	@Override
	public void loadConfig() {
		Boxes.neiInstalled = true;

		NEIContentTipHandler handler = new NEIContentTipHandler();
		GuiContainerManager.addInputHandler(handler);
		GuiContainerManager.addObjectHandler(handler);
		GuiContainerManager.addDrawHandler(handler);
		API.registerNEIGuiHandler(handler);

		MultiItemRange mainRng = new MultiItemRange();
		mainRng.add(Registry.itemResources);
		for (MetaType mt : MetaType.values()){
			if (!mt.hidden()){
				mainRng.add(new ItemStack(Registry.BlockMeta, 1, mt.ordinal()));
			}
		}
		API.addSetRange("Mod.Boxes", mainRng);

		MultiItemRange range = new MultiItemRange();
		range.add(Registry.BlockBox);
		API.addSetRange("Mod.Boxes.Boxes", range);
	}

	@Override
	public String getName() {
		return "Boxes";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	private static class NEIContentTipHandler extends INEIGuiAdapter implements IContainerInputHandler, IContainerObjectHandler, IContainerDrawHandler {

		@Override
		public boolean keyTyped(GuiContainer gui, char keyChar, int keyCode) {
			if (ContentTipManager.instance != null) {
				for (IContentTip tip : ContentTipManager.instance.contentTips.values()) {
					if (tip.handleKeyPress(keyChar, keyCode)) return true;
				}
			}
			return false;
		}

		@Override
		public void onKeyTyped(GuiContainer gui, char keyChar, int keyID) {

		}

		@Override
		public boolean lastKeyTyped(GuiContainer gui, char keyChar, int keyID) {
			return false;
		}

		@Override
		public boolean mouseClicked(GuiContainer gui, int mousex, int mousey, int button) {
			if (ContentTipManager.instance != null) {
				for (IContentTip tip : ContentTipManager.instance.contentTips.values()) {
					if (tip.handleMouseClick(mousex, mousey, button)) return true;
				}
			}
			return false;
		}

		@Override
		public void onMouseClicked(GuiContainer gui, int mousex, int mousey, int button) {

		}

		@Override
		public void onMouseUp(GuiContainer gui, int mousex, int mousey, int button) {

		}

		@Override
		public boolean mouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled) {
			return false;
		}

		@Override
		public void onMouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled) {

		}

		@Override
		public void onMouseDragged(GuiContainer gui, int mousex, int mousey, int button, long heldTime) {

		}

		//IContainerObjectHandler
		@Override
		public void guiTick(GuiContainer gui) {}

		@Override
		public void refresh(GuiContainer gui) {}

		@Override
		public void load(GuiContainer gui) {}

		private boolean hideToolTips = false;
		@Override
		public ItemStack getStackUnderMouse(GuiContainer gui, int mousex, int mousey) {
			if (ContentTipManager.instance != null) {
				IContentTip ict = ContentTipManager.instance.getTipAt(mousex, mousey, false);
				if (ict != null) return ict.getStackAtPosition(mousex, mousey);
			}
			return null;
		}

		@Override
		public boolean objectUnderMouse(GuiContainer gui, int mousex, int mousey) {
			if (ContentTipManager.instance != null) {
				return ContentTipManager.instance.getTipAt(mousex, mousey, false) != null;
			}
			return false;
		}

		@Override
		public boolean shouldShowTooltip(GuiContainer gui) {
			return !hideToolTips;
		}

		//IContainerDrawHandler
		@Override
		public void onPreDraw(GuiContainer gui) {}

		@Override
		public void renderObjects(GuiContainer gui, int mousex, int mousey) {
			if (ContentTipManager.instance != null) ContentTipManager.instance.doRender(mousex, mousey);
			hideToolTips = getStackUnderMouse(gui, mousex, mousex) == null && objectUnderMouse(gui, mousex, mousey);
		}

		@Override
		public void postRenderObjects(GuiContainer gui, int mousex, int mousey) {}

		@Override
		public void renderSlotUnderlay(GuiContainer gui, Slot slot) {}

		@Override
		public void renderSlotOverlay(GuiContainer gui, Slot slot) {}

		@Override
		public boolean hideItemPanelSlot(GuiContainer gui, int x, int y, int w, int h) {
			// TODO
			return false;
		}
	}
}
