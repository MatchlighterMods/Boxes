package ml.boxes.client;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;

import org.lwjgl.input.Mouse;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ContentTipRenderer implements ITickHandler {

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		// TODO Auto-generated method stub

	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if (type.contains(TickType.RENDER)){
			renderContentTip();
		}
	}
	
	private void renderContentTip(){
		Minecraft mc = FMLClientHandler.instance().getClient();
		if (mc.currentScreen instanceof GuiContainer){
			GuiContainer asGuiContainer = (GuiContainer)mc.currentScreen;
			
			int guiXSize = ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, (GuiContainer)mc.currentScreen, 1);
			int guiYSize = ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, (GuiContainer)mc.currentScreen, 2);
			
			ScaledResolution var13 = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
            int var14 = var13.getScaledWidth();
            int var15 = var13.getScaledHeight();
            int adjMouseX = Mouse.getX() * var14 / mc.displayWidth;
            int adjMousY = var15 - Mouse.getY() * var15 / mc.displayHeight - 1;
            
            int guiLeft = (asGuiContainer.width - guiXSize) / 2;
            int guiTop = (asGuiContainer.height - guiYSize) / 2;
            
            for (Object slt : asGuiContainer.inventorySlots.inventorySlots){
            	Slot asSlot = (Slot)slt;
            	
            }
		}
	}

	private Boolean pointInRect(int pntX, int pntY, int rectX, int rectY, int rectW, int rectH){
		return (pntX >= rectX && pntY >= rectY && pntX <= rectX+rectW && pntY <= rectY + rectH);
	}
	
	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.RENDER);
	}

	@Override
	public String getLabel() {
		return "BoxesContentTipRenderer";
	}

}
