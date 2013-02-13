package ml.boxes.client;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import ml.boxes.BoxData;
import ml.boxes.Boxes;
import ml.boxes.ItemIBox;
import ml.boxes.client.ContentTip.HintItemStack;
import ml.boxes.inventory.ContainerBox;
import ml.boxes.item.ItemBox;
import ml.core.Geometry;
import ml.core.Geometry.XYPair;
import ml.core.Geometry.rectangle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
//TODO Rewrite w/o ContentTip class
//TODO Tip does not close when an NEI recipe is shown
public class ContentTipHandler implements ITickHandler {

	private static Slot hoverSlot;
	private static long tickerTime = 0;
	public static boolean showingTip = false;

	private static rectangle curBounds;
	private static boolean renderContents = true;
	private static rectangle gcBounds = new rectangle(0, 0, 0, 0);
	private static boolean interacting = false;
	private static XYPair gridDimensions;
	private static List<ItemStack> contentStacks = new ArrayList<ItemStack>();

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {

	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if (type.contains(TickType.RENDER)){
			Minecraft mc = FMLClientHandler.instance().getClient();
			if (mc.currentScreen instanceof GuiContainer){

				Geometry.XYPair m = Geometry.getScaledMouse();
				if (!Boxes.neiInstalled) //NEI Provides a better place for doing this. Use it if we can
					renderContentTip(mc, m.X, m.Y, (Float)tickData[0]);
			}
		}else if (type.contains(TickType.CLIENT)){
			updateCurrentTip();
		}
	}

	// Do calculations for the tip
	private static void updateCurrentTip(){
		Minecraft mc = FMLClientHandler.instance().getClient();
		if (mc.currentScreen instanceof GuiContainer){
			GuiContainer asGuiContainer = (GuiContainer)mc.currentScreen;

			int guiXSize = ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, (GuiContainer)mc.currentScreen, 1);
			int guiYSize = ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, (GuiContainer)mc.currentScreen, 2);

			Geometry.XYPair m = Geometry.getScaledMouse();

			gcBounds.width = asGuiContainer.width;
			gcBounds.height = asGuiContainer.height;
			
			gcBounds.xCoord = (asGuiContainer.width - guiXSize) / 2;
			gcBounds.yCoord = (asGuiContainer.height - guiYSize) / 2;
			
			if (showingTip && isTipValid(m.X, m.Y)){
				BoxData bd = ItemBox.getDataFromIS(hoverSlot.getStack());
				
				if (Boxes.neiInstalled && asGuiContainer.isShiftKeyDown() && hoverSlot.inventory instanceof InventoryPlayer){
					interacting = true;
					gridDimensions = Geometry.determineSquarestGrid(bd.getSizeInventory());
				} else {
					interacting = false;
					contentStacks.clear();
					List<ItemStack> iss = bd.getContainedItemStacks();
					boolean matched = false;
					for (ItemStack is : iss){
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
				
				int tX = gridDimensions.X*18 +16;
				int tY = gridDimensions.Y*18 +16;
				renderContents = true;
				if (tX != curBounds.width || tY != curBounds.height){
					renderContents = false;
					if (tX > curBounds.width){
						curBounds.width += 16;
						if (tX < curBounds.width)
							curBounds.width = tX;
					} else if (tX < curBounds.width) {
						curBounds.width -= 16;
						if (tX > curBounds.width)
							curBounds.width = tX;
					}
					
					if (tY > curBounds.height){
						curBounds.height += 16;
						if (tY < curBounds.height)
							curBounds.height = tY;
					} else if (tY < curBounds.height) {
						curBounds.height -= 16;
						if (tY > curBounds.height)
							curBounds.height = tY;
					}
					
					curBounds.xCoord = gcBounds.xCoord + hoverSlot.xDisplayPosition + (16-curBounds.width)/2;
					curBounds.yCoord = gcBounds.yCoord + hoverSlot.yDisplayPosition - curBounds.height;
				}
			} else {
				showingTip = false;
				curBounds = new rectangle(0, 0, 0, 0);
				boolean thoverSlot = false;
				for (Object objSlt : asGuiContainer.inventorySlots.inventorySlots){
					Slot slt = (Slot)objSlt;
					if (Geometry.pointInRect(m.X, m.Y, gcBounds.xCoord + slt.xDisplayPosition, gcBounds.yCoord + slt.yDisplayPosition, 16, 16)){
						thoverSlot = true;
						if (hoverSlot != slt)
							tickerTime = mc.getSystemTime();
						hoverSlot = slt;

						if (slt.getHasStack() &&
								(slt.getStack().getItem() instanceof ItemBox) &&
								(!Boxes.config.shiftForTip || asGuiContainer.isShiftKeyDown()) &&
								(mc.getSystemTime() - tickerTime > Boxes.config.tipReactionTime || asGuiContainer.isShiftKeyDown()) &&
								!(asGuiContainer instanceof GuiBox && ((ContainerBox)asGuiContainer.inventorySlots).box instanceof ItemIBox && ((ItemIBox)((ContainerBox)asGuiContainer.inventorySlots).box).stack == slt.getStack())
								){
							showingTip = true;
						}
					}
				}
				if (!thoverSlot)
					hoverSlot = null;
			}
		}
	}
	
	public static boolean isTipValid(int mX, int mY){
		return ((Geometry.pointInRect(mX, mY, gcBounds.xCoord + hoverSlot.xDisplayPosition, gcBounds.yCoord + hoverSlot.yDisplayPosition, 16, 16) || 
				(interacting && Geometry.pointInRect(mX, mY, curBounds))) &&
				hoverSlot.getHasStack() // TODO Add better checking to ensure that it is the same box.
				);
	}

	public static boolean isPointInTip(int pX, int pY){
		return Geometry.pointInRect(pX, pY, curBounds);
	}

	//Render the tip
	public static void renderContentTip(Minecraft mc, int mx, int my, float tickTime){
		if (!isTipValid(mx, my)){
			showingTip = false;
		}
		
		if (showingTip){
			RenderEngine re = mc.renderEngine;
			int tex = re.getTexture("/ml/boxes/gfx/contentTipGui2.png");
			re.bindTexture(tex);
			
			GL11.glPushMatrix();
			GL11.glTranslatef(curBounds.xCoord, curBounds.yCoord, 0F);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

			RenderUtils.drawTexturedModalRect(0, 0, 0, 0, curBounds.width-9, curBounds.height-7);
			RenderUtils.drawTexturedModalRect(7, 0, 178-(curBounds.width-7), 0, curBounds.width-7, curBounds.height-7);
			RenderUtils.drawTexturedModalRect(7, 9, 178-(curBounds.width-7), 106-(curBounds.height-9), curBounds.width-7, curBounds.height-9);
			RenderUtils.drawTexturedModalRect(0, 9, 0, 106-(curBounds.height-9), curBounds.width-9, curBounds.height-9);

			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glPopMatrix();
		}
	}

//	public static boolean handleClick(int mx, int my, int btn){
//		if (!isTipValid(mx, my)){
//			showingTip = false;
//			return false;
//		}
//		
//		
//	}
	
	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.RENDER, TickType.CLIENT);
	}

	@Override
	public String getLabel() {
		return "Boxes";
	}
}
