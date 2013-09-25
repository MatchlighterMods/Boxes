package ml.boxes.inventory;

import ml.boxes.tile.TileEntityDisplayCase;
import ml.core.item.StackUtils;
import ml.core.item.StackUtils.RSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerDisplayCase extends Container {

	protected TileEntityDisplayCase tedc;
	
	public ContainerDisplayCase(TileEntityDisplayCase tedc, EntityPlayer pl) {
		this.tedc = tedc;
		
		int ySize = 166;
		
		addSlotToContainer(new RSlot(tedc, 0, 80, 30));
		
		for (int slt=9; slt < pl.inventory.mainInventory.length; slt++){
			int row = (int)Math.floor(slt/9) -1;
			addSlotToContainer(new Slot(pl.inventory, slt, 8 + (slt%9)*18, ySize - 82 + row*18));
		}

		for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
			addSlotToContainer(new Slot(pl.inventory, hotbarSlot, 8 + hotbarSlot * 18, ySize - 24));
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
		ItemStack itemstack = null;
		Slot slot = (Slot)this.inventorySlots.get(par2);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (par2 < tedc.getSizeInventory()) {
				if (!StackUtils.mergeItemStack(itemstack1, tedc.getSizeInventory(), this.inventorySlots.size(), true, this)) {
					return null;
				}
			} else if (!StackUtils.mergeItemStack(itemstack1, 0, tedc.getSizeInventory(), false, this)) {
				return null;
			}

			if (itemstack1.stackSize == 0) {
				slot.putStack((ItemStack)null);
			} else {
				slot.onSlotChanged();
			}
		}

		return itemstack;
	}

}
