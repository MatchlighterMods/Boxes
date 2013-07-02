package ml.boxes.client.gui;

import org.lwjgl.opengl.GL11;

import invtweaks.api.ContainerGUI;
import ml.boxes.inventory.ContainerSafe;
import ml.core.geo.Rectangle;
import ml.core.geo.Vector2;
import ml.core.gui.GuiContainerControl;
import ml.core.gui.GuiSide;
import ml.core.gui.MouseButton;
import ml.core.gui.controls.ControlTabManager;
import ml.core.gui.controls.TabLedger;
import ml.core.gui.controls.ControlTabManager.GuiTab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

@ContainerGUI
public class GuiSafe extends GuiContainerControl {

	public GuiSafe(Container par1Container) {
		super(par1Container);
		
		this.xSize = 178;
		this.ySize = 168;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initControls() {
		ControlTabManager ctm = new ControlTabManager(this, GuiSide.Right);
		ctm.tabTopMargin = 80;
		
		ctm.tabs.add(new TabLock(ctm));
		controls.add(ctm);
	}

	@Override
	protected void drawBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture("/mods/Boxes/textures/gui/boxGui.png");
		drawTexturedModalRect(0, 0, 0, 0, xSize, ySize);		
	}

	protected class TabLock extends GuiTab {

		boolean mouseIn = false;
		
		public TabLock(ControlTabManager ctm) {
			super(ctm);
			this.tabColor = 0xDD3333;
		}
		
		@Override
		public boolean onMouseClicked(int lmX, int lmY, MouseButton button) {
			mc.thePlayer.closeScreen();
			// TODO Send lock packet
			return true;
		}
		
		@Override
		public void updateTick() {
			super.updateTick();
			
			int trg = mouseIn ? 80 : defaultSize;
			size.X = Math.abs(size.X-trg) < 8 ? trg : size.X + (size.X > trg ? -8 : 8);
		}

		@Override
		public void renderContents(Minecraft mc, int lmX, int lmY) {
			mouseIn = new Rectangle(0, 0, this.size.X, this.size.Y).isPointInside(lmX, lmY);
			
		}
		
	}
	
}
