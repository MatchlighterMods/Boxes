package ml.boxes.client;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import ml.boxes.BoxData;
import ml.boxes.Boxes;
import ml.boxes.Lib;
import ml.boxes.Lib.XYPair;
import ml.boxes.item.ItemBox;
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
public class ContentTipHandler implements ITickHandler {
	
	public static ContentTip currentTip;
	
	private static Slot hoverSlot;
	private static long tickerTime = 0;
		
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {

	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if (type.contains(TickType.RENDER)){
			Minecraft mc = FMLClientHandler.instance().getClient();
			if (mc.currentScreen instanceof GuiContainer){
				
				XYPair m = Lib.getScaledMouse();
				if (!Boxes.neiInstalled) //NEI Provides a better place for doing this. Ue it if we can
					renderContentTip(mc, m.X, m.Y, (Float)tickData[0]);
			}
		}else if (type.contains(TickType.CLIENT)){
			updateCurrentTip();
		}
	}
	
	// Do calculations for the tip
	private void updateCurrentTip(){
		Minecraft mc = FMLClientHandler.instance().getClient();
		if (mc.currentScreen instanceof GuiContainer){
			GuiContainer asGuiContainer = (GuiContainer)mc.currentScreen;
			
			int guiXSize = ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, (GuiContainer)mc.currentScreen, 1);
			int guiYSize = ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, (GuiContainer)mc.currentScreen, 2);
			
			XYPair m = Lib.getScaledMouse();

            int guiLeft = (asGuiContainer.width - guiXSize) / 2;
            int guiTop = (asGuiContainer.height - guiYSize) / 2;

            if (currentTip == null || !currentTip.StillValid(m.X, m.Y)){
            	currentTip = null;
            	boolean inASlot = false;
            	for (Object slt : asGuiContainer.inventorySlots.inventorySlots){
            		Slot asSlot = (Slot)slt;
            		
            		if (Lib.pointInRect(m.X-guiLeft, m.Y-guiTop, asSlot.xDisplayPosition, asSlot.yDisplayPosition, 16, 16)){
            			inASlot = true;
            			if (hoverSlot != asSlot || !asSlot.getHasStack() || !(asSlot.getStack().getItem() instanceof ItemBox)){ //
	            			tickerTime = mc.getSystemTime();
            			}
            			hoverSlot = asSlot;
            			
            			if (asSlot.getHasStack() && 
            					(asSlot.getStack().getItem() instanceof ItemBox) &&
            					//!(asGuiContainer instanceof GuiBox && false) && // TODO Make sure the tip will not be for the open box
                				(!Boxes.shiftForTip || asGuiContainer.isShiftKeyDown()) &&
                				(mc.getSystemTime() - tickerTime > Boxes.tipReactionTime || asGuiContainer.isShiftKeyDown())
                				){
            				currentTip = new ContentTip(asGuiContainer, asSlot, guiTop, guiLeft);
            			}
            		}
            	}
            	if (!inASlot)
            		hoverSlot = null;
            }

            if (currentTip!=null)
            	currentTip.doTick();
		}
	}
	
	//Render the tip
	public static void renderContentTip(Minecraft mc, int mx, int my, float tickTime){
		if (!(hoverSlot.getStack().getItem() instanceof ItemBox))
			currentTip=null;
		if (currentTip != null)
			currentTip.doRender(mc, mx, my);
	}
		
	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.RENDER, TickType.CLIENT);
	}

	@Override
	public String getLabel() {
		return "Boxes";
	}

}
