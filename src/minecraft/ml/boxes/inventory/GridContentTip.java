package ml.boxes.inventory;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import ml.boxes.Boxes;
import ml.boxes.client.GuiBox;
import ml.boxes.data.Box;
import ml.boxes.data.ItemBoxContainer;
import ml.core.Geometry;
import ml.core.Geometry.XYPair;
import ml.core.Geometry.rectangle;
import ml.core.lib.StringLib;
import ml.core.lib.render.GuiRenderLib;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GridContentTip extends ContentTip {

	protected static XYPair gridDimensions;
	private static List<ItemStack> contentStacks = new ArrayList<ItemStack>();
	
	public GridContentTip(Slot slt, rectangle gcRect) {
		super(slt, gcRect);
		
	}

	@Override
	protected void renderPreview(Minecraft mc, int mx, int my) {
		for (int i=0; i<contentStacks.size(); i++){
			int col = i%gridDimensions.X;
			int row = i/gridDimensions.X;

			int slotX = 8+col*18;
			int slotY = 10+row*18;
			
			ItemStack is = contentStacks.get(i);
			GuiRenderLib.drawSpecialStackAt(mc, slotX, slotY, is, is.stackSize> 1 ? StringLib.toGroupedString(is.stackSize,1) : "");
		}
	}

	@Override
	protected void renderIteractable(Minecraft mc, int mx, int my) {
		for (int i=0; i<contentStacks.size(); i++){
			int col = i%gridDimensions.X;
			int row = i/gridDimensions.X;

			int slotX = 8+col*18;
			int slotY = 10+row*18;

			ItemStack is = contentStacks.get(i);
			mc.renderEngine.bindTexture("/mods/Boxes/textures/gui/contentTipGui2.png");
			GuiRenderLib.drawTexturedModalRect(slotX-1, slotY-1, 0, 106, 18, 18);

			GuiRenderLib.drawStackAt(mc, slotX, slotY, is);

			GL11.glDisable(GL11.GL_LIGHTING);
			if (Geometry.pointInRect(mx - tipBounds.xCoord, my - tipBounds.yCoord, slotX, slotY, 16, 16)){
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				GuiRenderLib.drawGradientRect(slotX, slotY, slotX + 16, slotY + 16, -2130706433, -2130706433);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}
		}
	}
	
	@Override
	protected void renderBackground(Minecraft mc, int mx, int my) {
		mc.renderEngine.bindTexture("/mods/Boxes/textures/gui/contentTipGui2.png");
		GuiRenderLib.drawSlicedRect(0, 0, tipBounds.width, tipBounds.height, 0, 0, 178, 106, 9, 9, 7, 7);
	}
	
	@Override
	public void tick(Minecraft mc) {
		ItemBoxContainer iib = getIIB();
		Box bd = iib.getBox();
		
		contentStacks.clear();
		if (interacting){
			for (int i=0; i<bd.getSizeInventory(); i++){
				contentStacks.add(bd.getStackInSlot(i));
			}
			gridDimensions = Geometry.determineSquarestGrid(bd.getSizeInventory());
		} else {
			List<ItemStack> iss = bd.getContainedItemStacks();
			for (ItemStack is : iss){
				boolean matched = false;
				for (ItemStack his : contentStacks){
					if (his.isItemEqual(is) && ItemStack.areItemStackTagsEqual(his, is)){
						his.stackSize += is.stackSize;
						matched = true;
						break;
					}
				}
				if (!matched)
					contentStacks.add(is.copy());
			}
			gridDimensions = Geometry.determineSquarestGrid(contentStacks.size());
		}
		targetSize.X = gridDimensions.X*18 +16;
		targetSize.Y = gridDimensions.Y*18 +16;
		
		super.tick(mc);
	}

	@Override
	public int getSlotAtPosition(int pX, int pY) {
		if (interacting && renderContents){
			for (int i=0; i< getIIB().getBox().getSizeInventory(); i++){
				int col = i%gridDimensions.X;
				int row = i/gridDimensions.X;

				int slotX = 8+col*18 + tipBounds.xCoord;
				int slotY = 10+row*18 + tipBounds.yCoord;

				if (Geometry.pointInRect(pX, pY, slotX, slotY, 16, 16)){
					return i;
				}
			}
		}
		return -1;
	}

}
