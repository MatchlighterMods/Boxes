package ml.boxes.client;

import ml.boxes.Boxes;
import ml.boxes.Lib;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ContentTip extends Gui {

	public Slot slot;
	
	public GuiContainer gui;
	public int guiWidth;
	public int guiHeight;
	public int guiTop;
	public int guiLeft;
	
	private void render(){
		
	}
	
	public void tick(int mX, int mY){
		
		render();
	}
	
	public boolean StillValid(int mX, int mY){
		return (Lib.pointInRect(mX, mY, slot.xDisplayPosition, slot.yDisplayPosition, 16, 16) || 
				(Boxes.neiInstalled && gui.isShiftKeyDown() && true) // TODO Add pointInRect for whole tip
				);
	}
}
