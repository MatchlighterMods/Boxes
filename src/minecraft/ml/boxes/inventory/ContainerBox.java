package ml.boxes.inventory;

import ml.boxes.BoxData;
import ml.boxes.IBox;
import ml.boxes.item.ItemBox;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBox extends Container {

	public final IBox box;
	public final EntityPlayer player;
	
	public ContainerBox(IBox box, EntityPlayer pl) {
		this.box = box;
		this.player = pl;
		box.boxOpen();
		
		int leftCol = 9;
		int ySize = 152;
		for (int slt=9; slt < pl.inventory.mainInventory.length; slt++){
			int row = (int)Math.floor(slt/9) -1;
			addSlotToContainer(new Slot(pl.inventory, slt, 9 + (slt%9)*18, ySize - 83 + row*18));
		}

        for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++)
        {
            addSlotToContainer(new Slot(pl.inventory, hotbarSlot, leftCol + hotbarSlot * 18, ySize - 25));
        }
        
        for (int sln = 0; sln < box.getBoxData().getSizeInventory(); sln++){
        	addSlotToContainer(new SlotBox(box.getBoxData(), sln, 8 + (sln%9)*18, 10 + (int)Math.floor(sln/9)*18));
        }
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		return box.getBoxData().isUseableByPlayer(var1);
	}
	
	@Override
	public ItemStack slotClick(int slotNum, int mouseBtn, int action,
			EntityPlayer par4EntityPlayer) {
		box.saveData();
		return super.slotClick(slotNum, mouseBtn, action, par4EntityPlayer);
	}

	@Override
	public void onCraftGuiClosed(EntityPlayer par1EntityPlayer) {
		super.onCraftGuiClosed(par1EntityPlayer);
		box.saveData();
		box.boxClose();
	}
	
	protected class SlotBox extends Slot{
		public SlotBox(IInventory par1iInventory, int par2, int par3, int par4) {
			super(par1iInventory, par2, par3, par4);
			
		}

		@Override
		public boolean isItemValid(ItemStack par1ItemStack) {
			if (par1ItemStack.getItem() instanceof ItemBox)
				return false;
			return true;
		}
	}
}
