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
public class ContentTipHandler extends Gui implements ITickHandler {
	
	public static boolean tipVisible = false;
	
	private Slot tickerSlot;
	private long tickerTime = 0;
	
	private GuiContainer openGui;
	public int guiTop;
	public int guiLeft;
	
	private XYPair position = new XYPair(0, 0);
	private XYPair currentSize = new XYPair(0, 0);
	private XYPair gridSize = new XYPair(0,0);
	private List<ItemStack> visibleStacks = new ArrayList<ItemStack>();
	private boolean resizing = false;
	private boolean inInteractMode = false; //Better for keeping things in sync than calling interactable() from doRender() (doRender() may come before tick(), so it would render the background slot before resizing is set)
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {

	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if (type.contains(TickType.RENDER)){
			if (!Boxes.neiInstalled) //NEI Provides a better place for doing this. Ue it if we can
				renderContentTip((Float)tickData[0]);
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
			
			ScaledResolution var13 = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
            int var14 = var13.getScaledWidth();
            int var15 = var13.getScaledHeight();
            int adjMouseX = Mouse.getX() * var14 / mc.displayWidth;
            int adjMouseY = var15 - Mouse.getY() * var15 / mc.displayHeight - 1;

            guiLeft = (asGuiContainer.width - guiXSize) / 2;
            guiTop = (asGuiContainer.height - guiYSize) / 2;

            if (!tipVisible || !StillValid(adjMouseX, adjMouseY)){
            	tipVisible = false;
            	boolean inASlot = false;
            	for (Object slt : asGuiContainer.inventorySlots.inventorySlots){
            		Slot asSlot = (Slot)slt;
            		
            		if (Lib.pointInRect(adjMouseX-guiLeft, adjMouseY-guiTop, asSlot.xDisplayPosition, asSlot.yDisplayPosition, 16, 16)){
            			inASlot = true;
            			if (tickerSlot != asSlot){
	            			tickerSlot=asSlot;
	            			tickerTime = mc.getSystemTime();
            			}
            			
            			tipVisible = (asSlot.getHasStack() &&
                				(asSlot.getStack().getItem() instanceof ItemBox) &&
                				//!(asGuiContainer instanceof GuiBox && false) && // TODO Make sure the tip will not be for the open box
                				(!Boxes.shiftForTip || asGuiContainer.isShiftKeyDown()) &&
                				(mc.getSystemTime() - tickerTime > Boxes.tipReactionTime || asGuiContainer.isShiftKeyDown())
                				);
            		}
            	}
            	if (!inASlot)
            		tickerSlot = null;
            }

            if (tipVisible){
	            //Tip Tick Stuff
            	BoxData bd = ItemBox.getDataFromIS(tickerSlot.getStack());
	            visibleStacks.clear();
	            
	            int slotSpacing = 0;
	            if (!interactStartable()){
	    			inInteractMode = false;
	    			visibleStacks.addAll(bd.getContainedItemStacks());
	    			slotSpacing = 0;
	    		} else {
	    			inInteractMode = true;
	    			slotSpacing = 2;
	    			for (int i=0; i<bd.getSizeInventory(); i++){
	    				visibleStacks.add(i, bd.getStackInSlot(i));
	    			}
	    		}
	            
	    		gridSize = Lib.determineBestGrid(visibleStacks.size());
	    		resizing = false;
	    		int targX = gridSize.X*(16+slotSpacing) +16;
	    		int targY = gridSize.Y*(16+slotSpacing) +16;
	            
	    		if (targX != currentSize.X || targY != currentSize.Y){
	    			resizing = true;
	    			if (targX > currentSize.X){
	    				currentSize.X += 16;
	    				if (targX < currentSize.X)
	    					currentSize.X = targX;
	    			} else if (targX < currentSize.X) {
	    				currentSize.X -= 16;
	    				if (targX > currentSize.X)
	    					currentSize.X = targX;
	    			}
	    			
	    			if (targY > currentSize.Y){
	    				currentSize.Y += 16;
	    				if (targY < currentSize.Y)
	    					currentSize.Y = targY;
	    			} else if (targY < currentSize.Y) {
	    				currentSize.Y -= 16;
	    				if (targY > currentSize.Y)
	    					currentSize.Y = targY;
	    			}
	    		}
	    				
	    		position.X = guiLeft + tickerSlot.xDisplayPosition + (16-currentSize.X)/2;
	    		position.Y = guiTop + tickerSlot.yDisplayPosition - currentSize.Y;
            }
		}
	}
	
	//Render the tip
	private void renderContentTip(float tickTime){
		Minecraft mc = FMLClientHandler.instance().getClient();
		if (mc.currentScreen instanceof GuiContainer){
			
			ScaledResolution var13 = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
            int var14 = var13.getScaledWidth();
            int var15 = var13.getScaledHeight();
            int adjMouseX = Mouse.getX() * var14 / mc.displayWidth;
            int adjMouseY = var15 - Mouse.getY() * var15 / mc.displayHeight - 1;

            if (tipVisible){
            	RenderEngine re = mc.renderEngine;
            	int tex = re.getTexture("/ml/boxes/gfx/contentTipGui2.png");
            	re.bindTexture(tex);
            	
            	int lmx = adjMouseX = position.X;
            	int lmy = adjMouseY = position.Y;
            	
            	GL11.glPushMatrix();
        		GL11.glTranslatef(position.X, position.Y, 0);
        		GL11.glDisable(GL11.GL_LIGHTING);
        		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        		
        		drawTexturedModalRect(0, 0, 0, 0, currentSize.X-9, currentSize.Y-7);
        		drawTexturedModalRect(7, 0, 178-(currentSize.X-7), 0, currentSize.X-7, currentSize.Y-7);
        		drawTexturedModalRect(7, 9, 178-(currentSize.X-7), 106-(currentSize.Y-9), currentSize.X-7, currentSize.Y-9);
        		drawTexturedModalRect(0, 9, 0, 106-(currentSize.Y-9), currentSize.X-9, currentSize.Y-9);
        		
        		if (!resizing){
        			int numStacks = visibleStacks.size();
        			for (int i=0; i< visibleStacks.size(); i++){
        				int col = i%gridSize.X;
        				int row = i/gridSize.X;
        				
        				int slotX = 8+col*18;
        				int slotY = 10+row*18;
        				
        				re.bindTexture(tex);
        				if (inInteractMode)
        					drawTexturedModalRect(slotX-1, slotY-1, 0, 106, 18, 18);
        				
        				if (visibleStacks.get(i) != null){
        					RenderUtils.drawStackAt(mc, slotX, slotY, visibleStacks.get(i));
        				}
        				
        				GL11.glDisable(GL11.GL_LIGHTING); //Will affect the next iteration as well (this is good)
        				if (inInteractMode && Lib.pointInRect(lmx, lmy, slotX, slotY, 16, 16)){
        					this.drawGradientRect(slotX, slotY, slotX + 16, slotY + 16, -2130706433, -2130706433);
        				}
        			}
        		}
        		GL11.glEnable(GL11.GL_LIGHTING);
        		GL11.glPopMatrix();
            }
		}
	}
		
	public boolean pointInTip(int px, int py){
		return Lib.pointInRect(px, py, position.X, position.Y, currentSize.X, currentSize.Y);
	}
	
	public boolean StillValid(int mX, int mY){
		return ((Lib.pointInRect(mX-guiLeft, mY-guiTop, tickerSlot.xDisplayPosition, tickerSlot.yDisplayPosition, 16, 16) || 
				(interactStartable() && pointInTip(mX, mY))) &&
				tickerSlot.getHasStack() // TODO Add better checking to ensure that it is the same box.
				);
	}
	
	private boolean interactStartable(){
		return (Boxes.neiInstalled && openGui.isShiftKeyDown() && tickerSlot.inventory instanceof InventoryPlayer);
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
