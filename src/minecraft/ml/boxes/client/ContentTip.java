package ml.boxes.client;

import org.lwjgl.opengl.GL11;

import ml.boxes.BoxData;
import ml.boxes.Boxes;
import ml.boxes.Lib;
import ml.boxes.Lib.XYPair;
import ml.boxes.item.ItemBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ContentTip extends Gui {
	
	public RenderItem itemRenderer = new RenderItem();
	
	public final GuiContainer gui;
	public final Slot slot;
	public final BoxData box;
	public final int guiWidth;
	public final int guiHeight;
	public final int guiTop;
	public final int guiLeft;

	private XYPair position = new XYPair(0, 0);
	private XYPair currentSize = new XYPair(0, 0);
	private XYPair gridSize = new XYPair(0,0);
	private int slotSize = 16;
	
	public ContentTip(GuiContainer gui, Slot slot, int gWidth, int gHeight, int gTop, int gLeft) {
		this.gui = gui;
		this.slot = slot;
		this.box = ItemBox.getDataFromIS(slot.getStack());
		this.guiWidth = gWidth;
		this.guiHeight = gHeight;
		this.guiTop = gTop;
		this.guiLeft = gLeft;
	}
	
	private void drawStackAt(RenderEngine re, int x, int y, ItemStack is){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
        itemRenderer.renderItemAndEffectIntoGUI(gui.fontRenderer, re, is, x, y);
        itemRenderer.renderItemOverlayIntoGUI(gui.fontRenderer, re, is, x, y);
	}
	
	private int getSlotAt(int x, int y){
		int ax = x - position.X-5;
		int ay = y-position.Y-5;
		return (int)Math.floor(ax/18) + (int)Math.floor(ay/18)*gridSize.X;
	}
	
	public void doRender(Minecraft mc, int mX, int mY, float tickTime){
		RenderEngine re = mc.renderEngine;
		
		re.bindTexture(re.getTexture("/ml/boxes/gfx/contentTip.png"));
		drawTexturedModalRect(position.X, position.Y, 0, 0, currentSize.X-5, currentSize.Y-5);
		drawTexturedModalRect(position.X+5, position.Y, 128-(currentSize.X-5), 0, currentSize.X-5, currentSize.Y-5);
		drawTexturedModalRect(position.X+5, position.Y+5, 128-(currentSize.X-5), 64-(currentSize.Y-5), currentSize.X-5, currentSize.Y-5);
		drawTexturedModalRect(position.X, position.Y+5, 0, 64-(currentSize.X-5), currentSize.X-5, currentSize.Y-5);
		
//		for (int i=0; i<elementCount; i++){
//			
//		}
	}
	
	public void tick(Minecraft mc, int mX, int mY){
		int elementCount = interactable(mX, mY) ? box.getSizeInventory() : box.getContainedItemStacks().size();
		XYPair targetGrid = Lib.determineBestGrid(elementCount);
		
		int targX = targetGrid.X*(slotSize+2) +10;
		int targY = targetGrid.Y*(slotSize+2) +10;
		
		if (targX > currentSize.X){
			currentSize.X += 4;
			if (targX < currentSize.X)
				currentSize.X = targX;
		} else if (targX < currentSize.X) {
			currentSize.X -= 4;
			if (targX > currentSize.X)
				currentSize.X = targX;
		}
		
		if (targY > currentSize.Y){
			currentSize.Y += 4;
			if (targY < currentSize.Y)
				currentSize.Y = targY;
		} else if (targY < currentSize.Y) {
			currentSize.Y -= 4;
			if (targY > currentSize.Y)
				currentSize.Y = targY;
		}
		
		position.X = guiLeft + slot.xDisplayPosition + (slotSize-currentSize.X)/2;
		position.Y = guiTop + slot.yDisplayPosition - currentSize.Y;
		
	}
	
	private boolean interactable(int mouseX, int mouseY){
		return (Boxes.neiInstalled && gui.isShiftKeyDown() && true); // TODO Add pointInRect for whole tip
	}
	
	public boolean StillValid(int mX, int mY){
		return (Lib.pointInRect(mX-guiLeft, mY-guiTop, slot.xDisplayPosition, slot.yDisplayPosition, 16, 16) || 
				interactable(mX, mY)
				);
	}
}
