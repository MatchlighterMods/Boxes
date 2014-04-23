package ml.boxes.inventory;

import java.util.ArrayList;
import java.util.List;

import ml.boxes.data.Box;
import ml.boxes.data.ItemBoxContainer;
import ml.core.gui.GuiRenderUtils;
import ml.core.util.StringUtils;
import ml.core.vec.GeoMath;
import ml.core.vec.Rectangle;
import ml.core.vec.Vector2i;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GridContentTip extends ContentTip {

	protected static ResourceLocation tipBgRes = new ResourceLocation("Boxes:textures/gui/contentTipGui2.png");
	protected static Vector2i gridDimensions;
	private static List<ItemStack> contentStacks = new ArrayList<ItemStack>();
	
	public GridContentTip(Slot slt, Rectangle gcRect) {
		super(slt, gcRect);
		
	}

	@Override
	protected void renderPreview(Minecraft mc, int mx, int my) {
		for (int i=0; i<contentStacks.size(); i++){
			int col = i%gridDimensions.x;
			int row = i/gridDimensions.x;

			int slotX = 8+col*18;
			int slotY = 10+row*18;
			
			ItemStack is = contentStacks.get(i);
			GuiRenderUtils.drawSpecialStackAt(mc, slotX, slotY, is, is.stackSize> 1 ? StringUtils.toGroupedString(is.stackSize,1) : "");
		}
	}

	@Override
	protected void renderIteractable(Minecraft mc, int mx, int my) {
		for (int i=0; i<contentStacks.size(); i++){
			int col = i%gridDimensions.x;
			int row = i/gridDimensions.x;

			int slotX = 8+col*18;
			int slotY = 10+row*18;

			ItemStack is = contentStacks.get(i);
			mc.getTextureManager().bindTexture(tipBgRes);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GuiRenderUtils.drawTexturedModalRect(slotX-1, slotY-1, 0, 106, 18, 18);

			GuiRenderUtils.drawStackAt(mc, slotX, slotY, is);

			GL11.glDisable(GL11.GL_LIGHTING);
			if (GeoMath.pointInRect(mx - tipBounds.xCoord, my - tipBounds.yCoord, slotX, slotY, 16, 16)){
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				GuiRenderUtils.drawGradientRect(slotX, slotY, slotX + 16, slotY + 16, -2130706433, -2130706433);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}
		}
	}
	
	@Override
	protected void renderBackground(Minecraft mc, int mx, int my) {
		mc.getTextureManager().bindTexture(tipBgRes);
		GuiRenderUtils.drawSlicedRect(0, 0, tipBounds.width, tipBounds.height, 0, 0, 178, 106, 9, 9, 7, 7);
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
			gridDimensions = GeoMath.determineSquarestGrid(bd.getSizeInventory());
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
			gridDimensions = GeoMath.determineSquarestGrid(contentStacks.size());
		}
		targetSize.x = gridDimensions.x*18 +16;
		targetSize.y = gridDimensions.y*18 +16;
		
		super.tick(mc);
	}

	@Override
	public int getSlotAtPosition(int pX, int pY) {
		if (interacting && renderContents){
			for (int i=0; i< getIIB().getBox().getSizeInventory(); i++){
				int col = i%gridDimensions.x;
				int row = i/gridDimensions.x;

				int slotX = 8+col*18 + tipBounds.xCoord;
				int slotY = 10+row*18 + tipBounds.yCoord;

				if (GeoMath.pointInRect(pX, pY, slotX, slotY, 16, 16)){
					return i;
				}
			}
		}
		return -1;
	}

}
