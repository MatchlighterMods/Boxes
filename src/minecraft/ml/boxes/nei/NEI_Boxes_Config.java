package ml.boxes.nei;

import ml.boxes.Boxes;
import ml.boxes.Registry;
import ml.boxes.block.MetaType;
import ml.boxes.client.ContentTipHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import codechicken.nei.MultiItemRange;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
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

		API.registerRecipeHandler(new BoxesRecipeHandler());
		API.registerUsageHandler(new BoxesRecipeHandler());
		
		if (Boxes.config.lockbox_allowCrafting) {
			API.registerRecipeHandler(new ComboRecipeHandler());
			API.registerUsageHandler(new ComboRecipeHandler());
		}
		
		MultiItemRange mainRng = new MultiItemRange();
		mainRng.add(Registry.ItemResources);
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
	
	private static class NEIContentTipHandler implements IContainerInputHandler, IContainerObjectHandler, IContainerDrawHandler{

		@Override
		public boolean keyTyped(GuiContainer gui, char keyChar, int keyCode) {
			if (ContentTipHandler.openTip != null){
				return ContentTipHandler.openTip.handleKeyPress(keyChar, keyCode);
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
		public boolean mouseClicked(GuiContainer gui, int mousex, int mousey,
				int button) {
			if (ContentTipHandler.revalidateCurrentTip(mousex, mousey)){
				return ContentTipHandler.openTip.handleMouseClick(mousex, mousey, button);
			}
			return false;
		}

		@Override
		public void onMouseClicked(GuiContainer gui, int mousex, int mousey,
				int button) {

		}

		@Override
		public void onMouseUp(GuiContainer gui, int mousex, int mousey,
				int button) {

		}

		@Override
		public boolean mouseScrolled(GuiContainer gui, int mousex, int mousey,
				int scrolled) {
			return false;
		}

		@Override
		public void onMouseScrolled(GuiContainer gui, int mousex, int mousey,
				int scrolled) {

		}

		@Override
		public void onMouseDragged(GuiContainer gui, int mousex, int mousey,
				int button, long heldTime) {

		}

		//IContainerObjectHandler
		@Override
		public void guiTick(GuiContainer gui) {}

		@Override
		public void refresh(GuiContainer gui) {}

		@Override
		public void load(GuiContainer gui) {}

		@Override
		public ItemStack getStackUnderMouse(GuiContainer gui, int mousex,
				int mousey) {
			if (ContentTipHandler.revalidateCurrentTip(mousex, mousey)){
				return ContentTipHandler.openTip.getStackAtPosition(mousex, mousey);
			}
			return null;
		}

		private boolean hideTips = false;
		@Override
		public boolean objectUnderMouse(GuiContainer gui, int mousex, int mousey) {
			hideTips = ContentTipHandler.openTip != null
					&& ContentTipHandler.openTip.getStackAtPosition(mousex, mousey) == null;
			return ContentTipHandler.openTip != null && ContentTipHandler.openTip.isPointInTip(mousex, mousey);
		}

		@Override
		public boolean shouldShowTooltip(GuiContainer gui) {
			return !hideTips;
		}

		//IContainerDrawHandler
		@Override
		public void onPreDraw(GuiContainer gui) {}

		@Override
		public void renderObjects(GuiContainer gui, int mousex, int mousey) {
			Minecraft mc = Minecraft.getMinecraft();
			if (ContentTipHandler.openTip != null)
				ContentTipHandler.renderContentTip(mc, mousex, mousey, 0);
		}

		@Override
		public void postRenderObjects(GuiContainer gui, int mousex, int mousey) {}

		@Override
		public void renderSlotUnderlay(GuiContainer gui, Slot slot) {}

		@Override
		public void renderSlotOverlay(GuiContainer gui, Slot slot) {}

	}
}
