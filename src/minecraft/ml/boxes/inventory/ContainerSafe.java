package ml.boxes.inventory;

import java.util.List;

import org.omg.PortableServer.IdUniquenessPolicyValue;

import ml.boxes.tile.TileEntitySafe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerSafe extends Container {

	public TileEntitySafe masterSafe;
	public IInventory sInv;
	public int invRows;
	
	public ContainerSafe(EntityPlayer pl, TileEntitySafe tes) {
		masterSafe = tes;
		sInv = tes.isConnected() ? new InventoryLargeChest("", ((TileEntitySafe)tes.getConnected()).inventory, tes.inventory) : tes.inventory;
		sInv.openChest();
		
		int leftCol = 9;
		invRows = sInv.getSizeInventory()/9;
		int ySize = 132 + 18*invRows;

        for (int slt=0; slt<sInv.getSizeInventory(); slt++) {
        	addSlotToContainer(new Slot(sInv, slt, 8+18*(slt%9), 26+18*(slt/9)));
        }
		
		for (int slt=9; slt < pl.inventory.mainInventory.length; slt++){
			int row = (int)Math.floor(slt/9) -1;
			addSlotToContainer(new Slot(pl.inventory, slt, leftCol + (slt%9)*18, ySize - 83 + row*18));
		}

		for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
			addSlotToContainer(new Slot(pl.inventory, hotbarSlot, leftCol + hotbarSlot * 18, ySize - 25));
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return sInv.isUseableByPlayer(entityplayer);
	}
	
	@Override
	public void onCraftGuiClosed(EntityPlayer par1EntityPlayer) {
		super.onCraftGuiClosed(par1EntityPlayer);
		sInv.closeChest();
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
	{
		ItemStack var3 = null;
		Slot var4 = (Slot)this.inventorySlots.get(par2);

		if (var4 != null && var4.getHasStack())
		{
			ItemStack var5 = var4.getStack();
			var3 = var5.copy();
			if (par2 < sInv.getSizeInventory()){
				if (!mergeItemStack(var5, sInv.getSizeInventory(), inventorySlots.size(), true))
					return null;
			} else if (!mergeItemStack(var5, 0, sInv.getSizeInventory(), false)){
				return null;
			}

			if (var5.stackSize == 0)
			{
				var4.putStack((ItemStack)null);
			}
			else
			{
				var4.onSlotChanged();
			}
		}

		return var3;
	}
	
}
