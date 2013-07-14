package ml.boxes.client.gui;

import org.lwjgl.opengl.GL11;

import codechicken.nei.recipe.GuiUsageRecipe;

import invtweaks.api.ContainerGUI;
import ml.boxes.Icons;
import ml.boxes.inventory.ContainerSafe;
import ml.core.geo.Rectangle;
import ml.core.geo.Vector2;
import ml.core.gui.GuiContainerControl;
import ml.core.gui.GuiRenderUtils;
import ml.core.gui.GuiSide;
import ml.core.gui.MouseButton;
import ml.core.gui.controls.ControlTabManager;
import ml.core.gui.controls.TabLedger;
import ml.core.gui.controls.ControlTabManager.GuiTab;
import ml.core.texture.maps.BasicCustomTextureMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

@ContainerGUI
public class GuiSafe extends GuiContainerControl {

	public ContainerSafe safeCont;
	
	public GuiSafe(ContainerSafe scont) {
		super(scont);
		
		safeCont = scont;
		this.xSize = 178;
		this.ySize = 132 + 18*scont.sInv.getSizeInventory()/9;
	}

	@Override
	protected void initControls() {
		ControlTabManager ctm = new ControlTabManager(this, GuiSide.Right);
		ctm.tabTopMargin = 20;
		
		ctm.tabs.add(new TabLock(ctm));
		controls.add(ctm);
	}

	@Override
	protected void drawBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(safeCont.isLarge ? "/mods/Boxes/textures/gui/safeGui_lg.png" : "/mods/Boxes/textures/gui/safeGui_sm.png");
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
		
		private Vector2<Integer> trg = new Vector2<Integer>(24,24);
		@Override
		public Vector2<Integer> getTargetSize() {
			return trg.set(mouseIn ? 24+8+fontRenderer.getStringWidth("Lock") : defaultSize, defaultSize);
		}

		@Override
		public void renderContents(Minecraft mc, int lmX, int lmY) {
			mouseIn = new Rectangle(0, 0, this.size.X, this.size.Y).isPointInside(lmX, lmY);
			
			BasicCustomTextureMap.GUI.getTexture().bindTexture(0);
			drawTexturedModelRectFromIcon(3, 5, Icons.LOCK, 14, 14);
			if (mouseIn && size.equals(getTargetSize())) {
				fontRenderer.drawString("Lock", 24, (24-fontRenderer.FONT_HEIGHT)/2+1, 0xFFFFFF, true);
			}
		}
		
	}
	
}
