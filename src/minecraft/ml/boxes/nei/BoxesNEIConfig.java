package ml.boxes.nei;

import ml.boxes.Boxes;
import net.minecraft.client.gui.inventory.GuiContainer;
import codechicken.nei.MultiItemRange;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.forge.GuiContainerManager;
import codechicken.nei.forge.IContainerInputHandler;

public class BoxesNEIConfig implements IConfigureNEI {

	@Override
	public void loadConfig() {
		Boxes.neiInstalled = true;
		GuiContainerManager.addInputHandler(new BoxesNEIInputHandler());
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
	
	private static class BoxesNEIInputHandler implements IContainerInputHandler{

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
		
	}

}
