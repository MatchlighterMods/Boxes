package ml.boxes.nei;

import java.util.List;

import ml.boxes.Boxes;
import ml.boxes.client.ContentTipHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import codechicken.nei.MultiItemRange;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.forge.GuiContainerManager;
import codechicken.nei.forge.IContainerInputHandler;
import codechicken.nei.forge.IContainerObjectHandler;
import codechicken.nei.forge.IContainerTooltipHandler;

public class NEI_Boxes_Config implements IConfigureNEI {

	@Override
	public void loadConfig() {
		Boxes.neiInstalled = true;
		
		NEIHandler handler = new NEIHandler();
		GuiContainerManager.addInputHandler(handler);
		GuiContainerManager.addObjectHandler(handler);
		
		MultiItemRange range = new MultiItemRange();
		range.add(Boxes.BlockBox);
		API.addSetRange("Boxes", range);
	}

	@Override
	public String getName() {
		return "Boxes";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}
	
	private static class NEIHandler implements IContainerInputHandler, IContainerObjectHandler{

		@Override
		public boolean keyTyped(GuiContainer gui, char keyChar, int keyCode) {
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
			if (ContentTipHandler.currentTip != null)
				return ContentTipHandler.currentTip.getStackAtPosition(mousex, mousey);
			return null;
		}

		private boolean hideTips = true;
		@Override
		public boolean objectUnderMouse(GuiContainer gui, int mousex, int mousey) {
			hideTips = ContentTipHandler.currentTip != null && ContentTipHandler.currentTip.getStackAtPosition(mousex, mousey)==null; //&& ContentTipHandler.currentTip.pointInTip(mousex, mousey);
			return ContentTipHandler.currentTip != null && ContentTipHandler.currentTip.pointInTip(mousex, mousey);
		}

		@Override
		public boolean shouldShowTooltip(GuiContainer gui) {
			return !hideTips;
		}

//		//IContainerTooltipHandler
//		@Override
//		public List<String> handleTooltipFirst(GuiContainer gui, int mousex,
//				int mousey, List<String> currenttip) {
//			if (ContentTipHandler.currentTip != null){
//				//ContentTipHandler.currentTip.handleToolTip(mousex, mousey, currenttip);
//			}
//			return currenttip;
//		}
//
//		@Override
//		public List<String> handleItemTooltip(GuiContainer gui,
//				ItemStack itemstack, List<String> currenttip) {
//			return currenttip;
//		}
		
	}

}
