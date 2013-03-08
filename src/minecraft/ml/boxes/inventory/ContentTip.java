package ml.boxes.inventory;

import ml.boxes.Boxes;
import ml.boxes.IBox;
import ml.boxes.data.BoxData;
import ml.boxes.data.ItemIBox;
import ml.boxes.network.packets.PacketTipClick;
import ml.core.Geometry;
import ml.core.Geometry.XYPair;
import ml.core.Geometry.rectangle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ContentTip {
	
	protected final Slot boxSlot;
	
	protected rectangle tipBounds = new rectangle(0, 0, 0, 0);
	protected rectangle gcBounds;
	protected int hvrSltIndex;
	
	protected XYPair targetSize = new XYPair(0, 0);
	
	protected boolean renderContents;
	public boolean interacting = false;
	
	protected int mousex = 0;
	protected int mousey = 0;
	
	private ItemStack origStack;
	
	public ContentTip(Slot slt, rectangle gcRect) {
		boxSlot = slt;
		gcBounds = gcRect;
		origStack = boxSlot.getStack();
	}
	
	@SideOnly(Side.CLIENT)
	public void tick(Minecraft mc){
		
		renderContents = true;
		if (targetSize.X != tipBounds.width || targetSize.Y != tipBounds.height){
			renderContents = false;
			if (targetSize.X > tipBounds.width){
				tipBounds.width += 16;
				if (targetSize.X < tipBounds.width)
					tipBounds.width = targetSize.X;
			} else if (targetSize.X < tipBounds.width) {
				tipBounds.width -= 16;
				if (targetSize.X > tipBounds.width)
					tipBounds.width = targetSize.X;
			}
			
			if (targetSize.Y > tipBounds.height){
				tipBounds.height += 16;
				if (targetSize.Y < tipBounds.height)
					tipBounds.height = targetSize.Y;
			} else if (targetSize.Y < tipBounds.height) {
				tipBounds.height -= 16;
				if (targetSize.Y > tipBounds.height)
					tipBounds.height = targetSize.Y;
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
			mc.thePlayer.sendQueue.addToSendQueue((new PacketTipClick((Player)mc.thePlayer, boxSlot.slotNumber, hvrSltIndex, arg, action)).convertToPkt250());
		}
		return ret;
	}
		
	public boolean revalidate(int mx, int my){
		GuiScreen oc = FMLClientHandler.instance().getClient().currentScreen;
		return oc instanceof GuiContainer &&
				((GuiContainer)oc).inventorySlots.inventorySlots.contains(boxSlot) &&
				(boxSlot != null && (Geometry.pointInRect(mx, my, gcBounds.xCoord + boxSlot.xDisplayPosition, gcBounds.yCoord + boxSlot.yDisplayPosition, 16, 16) || 
				(interacting && Geometry.pointInRect(mx, my, tipBounds))) &&
				boxSlot.getHasStack() && ItemStack.areItemStackTagsEqual(origStack, boxSlot.getStack())
				) && (interacting || getIIB().getBoxData().canOpenContentPreview());
	}
		
	protected ItemIBox getIIB(){
		return new ItemIBox(boxSlot.getStack());
	}
	
	public abstract int getSlotAtPosition(int pX, int pY);
	
	public ItemStack getStackAtPosition(int pX, int pY){
		int sltNum = getSlotAtPosition(pX, pY);
		BoxData bd = getIIB().getBoxData();
		if (sltNum >= 0 && sltNum < bd.getSizeInventory()){
			return bd.getStackInSlot(sltNum);
		}
		return null;
	}
	
	public boolean isPointInTip(int pX, int pY){
		return Geometry.pointInRect(pX, pY, tipBounds);
	}
	
    /* Actions
     * 0 - Standard action. Arg: mouseButton
     * 1 - Slot merge into my inventory
     * 2 - Move to Hotbar. Arg: targetSlot
     * 3 - Creative pick stack
     */
    public ItemStack slotClick(int slotNum, int arg, int action, EntityPlayer par4EntityPlayer)
    {
    	ItemIBox ibox = getIIB();
    	
        ItemStack var5 = null;
        InventoryPlayer invPl = par4EntityPlayer.inventory;
        Slot var7;
        ItemStack var8;
        int var10;
        ItemStack var11;

        if ((action == 0 || action == 1) && (arg == 0 || arg == 1))
        {
            if (action == 0)
            {
                if (slotNum < 0)
                {
                    return null;
                }

                var7 = ibox.getBoxData().getSlots().get(slotNum);

                if (var7 != null)
                {
                    var8 = var7.getStack();
                    ItemStack var13 = invPl.getItemStack();

                    if (var8 != null)
                    {
                        var5 = var8.copy();
                    }

                    if (var8 == null)
                    {
                        if (var13 != null && var7.isItemValid(var13))
                        {
                            var10 = arg == 0 ? var13.stackSize : 1;

                            if (var10 > var7.getSlotStackLimit())
                            {
                                var10 = var7.getSlotStackLimit();
                            }

                            var7.putStack(var13.splitStack(var10));

                            if (var13.stackSize == 0)
                            {
                                invPl.setItemStack((ItemStack)null);
                            }
                        }
                    }
                    else if (var7.canTakeStack(par4EntityPlayer))
                    {
                        if (var13 == null)
                        {
                            var10 = arg == 0 ? var8.stackSize : (var8.stackSize + 1) / 2;
                            var11 = var7.decrStackSize(var10);
                            invPl.setItemStack(var11);

                            if (var8.stackSize == 0)
                            {
                                var7.putStack((ItemStack)null);
                            }

                            var7.onPickupFromSlot(par4EntityPlayer, invPl.getItemStack());
                        }
                        else if (var7.isItemValid(var13))
                        {
                            if (var8.itemID == var13.itemID && var8.getItemDamage() == var13.getItemDamage() && ItemStack.areItemStackTagsEqual(var8, var13))
                            {
                                var10 = arg == 0 ? var13.stackSize : 1;

                                if (var10 > var7.getSlotStackLimit() - var8.stackSize)
                                {
                                    var10 = var7.getSlotStackLimit() - var8.stackSize;
                                }

                                if (var10 > var13.getMaxStackSize() - var8.stackSize)
                                {
                                    var10 = var13.getMaxStackSize() - var8.stackSize;
                                }

                                var13.splitStack(var10);

                                if (var13.stackSize == 0)
                                {
                                    invPl.setItemStack((ItemStack)null);
                                }

                                var8.stackSize += var10;
                            }
                            else if (var13.stackSize <= var7.getSlotStackLimit())
                            {
                                var7.putStack(var13);
                                invPl.setItemStack(var8);
                            }
                        }
                        else if (var8.itemID == var13.itemID && var13.getMaxStackSize() > 1 && (!var8.getHasSubtypes() || var8.getItemDamage() == var13.getItemDamage()) && ItemStack.areItemStackTagsEqual(var8, var13))
                        {
                            var10 = var8.stackSize;

                            if (var10 > 0 && var10 + var13.stackSize <= var13.getMaxStackSize())
                            {
                                var13.stackSize += var10;
                                var8 = var7.decrStackSize(var10);

                                if (var8.stackSize == 0)
                                {
                                    var7.putStack((ItemStack)null);
                                }

                                var7.onPickupFromSlot(par4EntityPlayer, invPl.getItemStack());
                            }
                        }
                    }

                    var7.onSlotChanged();
                }
            }
        }
        else if (action == 2 && arg >= 0 && arg < 9 && arg != boxSlot.getSlotIndex()) //Move to hotbar
        {
            var7 = ibox.getBoxData().getSlots().get(slotNum);

            if (var7.canTakeStack(par4EntityPlayer))
            {
                var8 = invPl.getStackInSlot(arg);
                boolean var9 = var8 == null || var7.inventory == invPl && var7.isItemValid(var8);
                var10 = -1;

                if (!var9)
                {
                    var10 = invPl.getFirstEmptyStack();
                    var9 |= var10 > -1;
                }

                if (var7.getHasStack() && var9)
                {
                    var11 = var7.getStack();
                    invPl.setInventorySlotContents(arg, var11);

                    if ((var7.inventory != invPl || !var7.isItemValid(var8)) && var8 != null)
                    {
                        if (var10 > -1)
                        {
                            invPl.addItemStackToInventory(var8);
                            var7.decrStackSize(var11.stackSize);
                            var7.putStack((ItemStack)null);
                            var7.onPickupFromSlot(par4EntityPlayer, var11);
                        }
                    }
                    else
                    {
                        var7.decrStackSize(var11.stackSize);
                        var7.putStack(var8);
                        var7.onPickupFromSlot(par4EntityPlayer, var11);
                    }
                }
                else if (!var7.getHasStack() && var8 != null && var7.isItemValid(var8))
                {
                    invPl.setInventorySlotContents(arg, (ItemStack)null);
                    var7.putStack(var8);
                }
            }
        }
        else if (action == 3 && par4EntityPlayer.capabilities.isCreativeMode && invPl.getItemStack() == null && slotNum >= 0)
        {
            var7 = ibox.getBoxData().getSlots().get(slotNum);

            if (var7 != null && var7.getHasStack())
            {
                var8 = var7.getStack().copy();
                var8.stackSize = var8.getMaxStackSize();
                invPl.setItemStack(var8);
            }
        }

        ibox.saveData();
        ibox.boxClose();
        origStack = ibox.stack;
        
        return var5;
    }
    
}
