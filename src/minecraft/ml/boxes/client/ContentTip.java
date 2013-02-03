package ml.boxes.client;

import java.util.ArrayList;
import java.util.List;

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
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ContentTip extends Gui {
	
	public static RenderItem itemRenderer = new RenderItem();
	
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
	private List<ItemStack> activeStacks = new ArrayList<ItemStack>();
	private boolean resizing = false;
	private boolean inInteractMode = false; //Better for keeping things in sync than calling interactable() from doRender() (doRender() may come before tick(), so it would render the background slot before resizing is set)
	
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
	
//	private int getSlotAt(int x, int y){
//		int ax = x - position.X-5;
//		int ay = y-position.Y-5;
//		return (int)Math.floor(ax/18) + (int)Math.floor(ay/18)*gridSize.X;
//	}
	
	public void doRender(Minecraft mc, int mX, int mY, float tickTime){
		RenderEngine re = mc.renderEngine;
		
		int tex = re.getTexture("/ml/boxes/gfx/contentTipGui2.png");
		re.bindTexture(tex);
		
		int amX = mX - position.X;
		int amY = mY - position.Y;
		
		zLevel = 300F;
		itemRenderer.zLevel = 300F;
		
		GL11.glPushMatrix();
		GL11.glTranslatef(position.X, position.Y, 0);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		drawTexturedModalRect(0, 0, 0, 0, currentSize.X-9, currentSize.Y-7);
		drawTexturedModalRect(7, 0, 178-(currentSize.X-7), 0, currentSize.X-7, currentSize.Y-7);
		drawTexturedModalRect(7, 9, 178-(currentSize.X-7), 106-(currentSize.Y-9), currentSize.X-7, currentSize.Y-9);
		drawTexturedModalRect(0, 9, 0, 106-(currentSize.Y-9), currentSize.X-9, currentSize.Y-9);
		
		if (!resizing){
			int numStacks = activeStacks.size();
			for (int i=0; i< activeStacks.size(); i++){
				int col = i%gridSize.X;
				int row = i/gridSize.X;
				
				int slotX = 8+col*18;
				int slotY = 10+row*18;
				
				re.bindTexture(tex);
				if (inInteractMode)
					drawTexturedModalRect(slotX-1, slotY-1, 0, 106, 18, 18);
				
				if (activeStacks.get(i) != null){
					drawStackAt(re, slotX, slotY, activeStacks.get(i));
				}
				
				GL11.glDisable(GL11.GL_LIGHTING); //Will affect the next iteration as well (this is good)
				if (inInteractMode && Lib.pointInRect(amX, amY, slotX, slotY, 16, 16)){
					this.drawGradientRect(slotX, slotY, slotX + 16, slotY + 16, -2130706433, -2130706433);
				}
			}
		}
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
		
		//gui.manager.renderToolTips(mX+guiLeft, mY+guiTop);
		
		itemRenderer.zLevel = 0F;
		zLevel = 0F;
	}
	
		
	public int getSlotAtPosition(int pX, int pY){
		int numStacks = activeStacks.size();
		for (int i=0; i< activeStacks.size(); i++){
			int col = i%gridSize.X;
			int row = i/gridSize.X;
			
			int slotX = 8+col*18 + position.X;
			int slotY = 10+row*18 + position.Y;
			
			if (Lib.pointInRect(pX, pY, slotX, slotY, 16, 16)){
				return i;
			}
		}
		return -1;
	}
	
	public ItemStack getStackAtPosition(int pX, int pY){
		if (inInteractMode){
			int numStacks = activeStacks.size();
			for (int i=0; i< activeStacks.size(); i++){
				int col = i%gridSize.X;
				int row = i/gridSize.X;
				
				int slotX = 8+col*18 + position.X;
				int slotY = 10+row*18 + position.Y;
				
				if (Lib.pointInRect(pX, pY, slotX, slotY, 16, 16)){
					return activeStacks.get(i);
				}
			}
		}
		return null;
	}
}
