package ml.boxes.inventory;

import ml.boxes.data.Box;
import ml.boxes.data.ItemBoxContainer;
import ml.boxes.network.packets.PacketTipClick;
import ml.core.inventory.CustomSlotClick;
import ml.core.vec.Rectangle;
import ml.core.vec.Vector2i;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ContentTip {

	protected final Slot boxSlot;

	public Rectangle tipBounds = new Rectangle(0, 0, 0, 0);
	protected Rectangle gcBounds;
	protected int hvrSltIndex;

	protected Vector2i targetSize = new Vector2i(0, 0);

	protected boolean renderContents;
	public boolean interacting = false;

	protected int mousex = 0;
	protected int mousey = 0;

	private ItemStack origStack;

	public ContentTip(Slot slt, Rectangle gcRect) {
		boxSlot = slt;
		gcBounds = gcRect;
		origStack = boxSlot.getStack();
	}

	@SideOnly(Side.CLIENT)
	public void tick(Minecraft mc){

		renderContents = true;
		if (targetSize.x != tipBounds.width || targetSize.y != tipBounds.height){
			renderContents = false;
			if (targetSize.x > tipBounds.width){
				tipBounds.width += 16;
				if (targetSize.x < tipBounds.width)
					tipBounds.width = targetSize.x;
			} else if (targetSize.x < tipBounds.width) {
				tipBounds.width -= 16;
				if (targetSize.x > tipBounds.width)
					tipBounds.width = targetSize.x;
			}

			if (targetSize.y > tipBounds.height){
				tipBounds.height += 16;
				if (targetSize.y < tipBounds.height)
					tipBounds.height = targetSize.y;
			} else if (targetSize.y < tipBounds.height) {
				tipBounds.height -= 16;
				if (targetSize.y > tipBounds.height)
					tipBounds.height = targetSize.y;
			}

			tipBounds.xCoord = gcBounds.xCoord + boxSlot.xDisplayPosition + (16-tipBounds.width)/2;
			tipBounds.yCoord = gcBounds.yCoord + boxSlot.yDisplayPosition - tipBounds.height;
		}
	}

	@SideOnly(Side.CLIENT)
	public void renderTick(Minecraft mc, int mx, int my){
		hvrSltIndex = getSlotAtPosition(mx, my);
		mousex = mx;
		mousey = my;

		GL11.glPushMatrix();
		GL11.glTranslatef(tipBounds.xCoord, tipBounds.yCoord, 0F);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		renderBackground(mc, mx, my);

		if (renderContents){
			if (interacting){
				renderIteractable(mc, mx, my);
			} else {
				renderPreview(mc, mx, my);
			}
		}

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}

	@SideOnly(Side.CLIENT)
	protected abstract void renderPreview(Minecraft mc, int mx, int my);

	@SideOnly(Side.CLIENT)
	protected abstract void renderIteractable(Minecraft mc, int mx, int my);

	@SideOnly(Side.CLIENT)
	protected abstract void renderBackground(Minecraft mc, int mx, int my);

	@SideOnly(Side.CLIENT)
	public boolean handleMouseClick(int mx, int my, int btn){
		Minecraft mc = FMLClientHandler.instance().getClient();
		if (isPointInTip(mx, my)){
			if (btn == 2){
				clientSlotClick(mc, hvrSltIndex, 0, 3, mc.thePlayer);
			} else {
				clientSlotClick(mc, hvrSltIndex, btn, 0, mc.thePlayer);
			}
			return true;
		}
		return false;
	}

	@SideOnly(Side.CLIENT)
	public boolean handleKeyPress(char chr, int kc){
		Minecraft mc = FMLClientHandler.instance().getClient();
		//if (isPointInTip(mousex, mousey)){
		if (mc.thePlayer.inventory.getItemStack() == null && hvrSltIndex >= 0)
		{
			for (int var2 = 0; var2 < 9; ++var2)
			{
				if (kc == 2 + var2)
				{
					clientSlotClick(mc, hvrSltIndex, var2, 2, mc.thePlayer);
					return true;
				}
			}
		}
		//return true;
		//}
		return false;
	}

	@SideOnly(Side.CLIENT)
	protected ItemStack clientSlotClick(Minecraft mc, int slotNum, int arg, int action, EntityPlayer par4EntityPlayer){
		ItemStack ret = slotClick(slotNum, arg, action, par4EntityPlayer);
		if (mc.currentScreen instanceof GuiContainerCreative){
			mc.thePlayer.inventoryContainer.detectAndSendChanges();
		} else {
			mc.thePlayer.sendQueue.addToSendQueue((new PacketTipClick(mc.thePlayer, boxSlot.slotNumber, hvrSltIndex, arg, action)).convertToPkt250());
		}
		return ret;
	}

	public boolean revalidate(int mx, int my){
		GuiScreen oc = FMLClientHandler.instance().getClient().currentScreen;
		return oc instanceof GuiContainer &&
				((GuiContainer)oc).inventorySlots.inventorySlots.contains(boxSlot) &&
				(boxSlot != null && (((GuiContainer)oc).getSlotAtPosition(mx, my)==boxSlot || //GeoMath.pointInRect(mx, my, gcBounds.xCoord + boxSlot.xDisplayPosition, gcBounds.yCoord + boxSlot.yDisplayPosition, 16, 16) 
						(interacting && tipBounds.isPointInside(mx, my))) &&
						boxSlot.getHasStack() && ItemStack.areItemStackTagsEqual(origStack, boxSlot.getStack())
						) && (interacting || getIIB().getBox().canOpenContentPreview());
	}

	protected ItemBoxContainer getIIB(){
		return new ItemBoxContainer(boxSlot.getStack());
	}

	public abstract int getSlotAtPosition(int pX, int pY);

	public ItemStack getStackAtPosition(int pX, int pY){
		int sltNum = getSlotAtPosition(pX, pY);
		Box bd = getIIB().getBox();
		if (sltNum >= 0 && sltNum < bd.getSizeInventory()){
			return bd.getStackInSlot(sltNum);
		}
		return null;
	}

	public boolean isPointInTip(int pX, int pY){
		return tipBounds.isPointInside(pX, pY);
	}

	CustomSlotClick csc = new CustomSlotClick() {
		@Override
		public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
			return null;
		}
	};
	
	public ItemStack slotClick(int slotNum, int arg, int action, EntityPlayer par4EntityPlayer) {
		if (action == 1) return null;
		
		ItemBoxContainer ibox = getIIB();
		csc.inventorySlots = ibox.getBox().getSlots();
		
		ItemStack itemstack = csc.slotClick(slotNum, arg, action, par4EntityPlayer);

		ibox.saveData();
		ibox.boxClose();
		origStack = ibox.stack;

		return itemstack;
	}

}
