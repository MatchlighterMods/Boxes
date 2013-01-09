package ml.boxes.inventory;

import ml.boxes.BoxData;
import ml.boxes.item.ItemBox;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBox extends Container {

	public final BoxData box;
	public final EntityPlayer player;
	
	public ContainerBox(BoxData box, EntityPlayer pl) {
		this.box = box;
		this.player = pl;
		
		int leftCol = 0;
		int ySize = 100;
        for (int playerInvRow = 0; playerInvRow < 3; playerInvRow++)
        {
            for (int playerInvCol = 0; playerInvCol < 9; playerInvCol++)
            {
                addSlotToContainer(new Slot(pl.inventory, playerInvCol + playerInvRow * 9 + 9, leftCol + playerInvCol * 18, ySize - (4 - playerInvRow) * 18
                        - 10));
            }

        }

        for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++)
        {
            addSlotToContainer(new Slot(pl.inventory, hotbarSlot, leftCol + hotbarSlot * 18, ySize - 24));
        }
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		return box.isUseableByPlayer(var1);
	}
	
	@Override
	public ItemStack slotClick(int slotNum, int mouseBtn, int action,
			EntityPlayer par4EntityPlayer) {
		saveInventory();
		return super.slotClick(slotNum, mouseBtn, action, par4EntityPlayer);
	}

	@Override
	public void onCraftGuiClosed(EntityPlayer par1EntityPlayer) {
		super.onCraftGuiClosed(par1EntityPlayer);
		saveInventory();
	}

	public void saveInventory(){
		
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
