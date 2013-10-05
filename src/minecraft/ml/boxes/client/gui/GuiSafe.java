package ml.boxes.client.gui;

import ml.boxes.client.Icons;
import ml.boxes.inventory.ContainerSafe;
import ml.boxes.network.packets.PacketDescribeSafe;
import ml.core.enums.MouseButton;
import ml.core.enums.NaturalSide;
import ml.core.gui.controls.tabs.ControlTabManager;
import ml.core.gui.controls.tabs.ControlTabManager.GuiTab;
import ml.core.texture.maps.BasicCustomTextureMap;
import ml.core.vec.Rectangle;
import ml.core.vec.Vector2i;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.PacketDispatcher;

public class GuiSafe extends GuiContainerControl {

	public ContainerSafe safeCont;
	protected static ResourceLocation bgRes = new ResourceLocation("Boxes:textures/gui/safeGui_lg.png");
	
	public GuiSafe(ContainerSafe scont) {
		super(scont);
		
		safeCont = scont;
		this.xSize = 178;
		this.ySize = 132 + 18*scont.sInv.getSizeInventory()/9;
	}

	@Override
	public void initControls(ControlsManager cm) {
		ControlTabManager ctm = new ControlTabManager(this, NaturalSide.Right);
		ctm.tabMargin = 20;
		
		ctm.tabs.add(new TabLock(ctm));
		cm.controls.add(ctm);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mX, int mY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		this.mc.getTextureManager().bindTexture(bgRes);
		drawTexturedModalRect(0, 0, 0, 0, xSize, 25+safeCont.invRows*18);
		drawTexturedModalRect(0, 25+safeCont.invRows*18, 0, 133, xSize, 107);
		
		super.drawGuiContainerBackgroundLayer(f, mX, mY);
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
			PacketDispatcher.sendPacketToServer(new PacketDescribeSafe.PacketLockSafe(safeCont.masterSafe).convertToPkt250());
			return true;
		}
		
		private Vector2i trg = new Vector2i(24,24);
		@Override
		public Vector2i getTargetSize() {
			return trg.set(mouseIn ? 24+8+fontRenderer.getStringWidth("Lock") : defaultSize, defaultSize);
		}

		@Override
		public void renderContents(Minecraft mc, int lmX, int lmY) {
			mouseIn = new Rectangle(0, 0, this.size.x, this.size.y).isPointInside(lmX, lmY);
			
			mc.renderEngine.bindTexture(BasicCustomTextureMap.GUI.resourceLoc);
			drawTexturedModelRectFromIcon(3, 5, Icons.LOCK, 14, 14);
			if (mouseIn && size.equals(getTargetSize())) {
				fontRenderer.drawString("Lock", 24, (24-fontRenderer.FONT_HEIGHT)/2+1, 0xFFFFFF, true);
			}
		}
		
	}
	
}
