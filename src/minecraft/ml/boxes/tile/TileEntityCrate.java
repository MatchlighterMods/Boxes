package ml.boxes.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class TileEntityCrate extends TileEntity implements ISidedInventory {
	
	public int CRATE_SIZE = 64*72;
	
	public int itemCount = 0;
	
	private ItemStack[] stacks;
	
	public TileEntityCrate() {
		stacks = new ItemStack[2];
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("containedCount", itemCount);
		
		NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < stacks.length; i++)
        {
            if (stacks[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                stacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
        
        tag.setTag("Items", nbttaglist);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		itemCount = tag.getInteger("containedCount");
		
		NBTTagList nbttaglist = tag.getTagList("Items");
		for (int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if (j >= 0 && j < stacks.length)
            {
            	stacks[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
	}
	
	public int getTotalItems(){
		return itemCount + (stacks[0] != null ? stacks[0].stackSize : 0) + (stacks[1] != null ? stacks[1].stackSize : 0);
	}
	
	public void updateFilter(){
		if (stacks[1] != null && stacks[0] == null){
			ItemStack fstack = stacks[1].copy();
			fstack.stackSize = 0;
			stacks[0] = fstack;
		}
		if (stacks[1] == null && itemCount == 0 && (stacks[0] == null || stacks[0].stackSize == 0)){
			stacks[0] = null;
		}
	}
	
	public void consolidateStacks(){
		if (stacks[0] != null){
			int amax = CRATE_SIZE-(2*stacks[0].getMaxStackSize());
			
			if (stacks[1] == null){
				stacks[1] = stacks[0].copy();
				stacks[1].stackSize = 0;
			}
			if (stacks[0].stackSize>0 && stacks[1].stackSize<stacks[1].getMaxStackSize()){
				int chg = Math.min(stacks[0].stackSize, stacks[1].getMaxStackSize()-stacks[1].stackSize);
				stacks[1].stackSize += chg;
				stacks[0].stackSize -= chg;
			}
			if (stacks[0].stackSize > 0 && itemCount<amax){
				int chg = Math.min(amax-itemCount, stacks[0].stackSize);
				itemCount += chg;
				stacks[0].stackSize -= chg;
			}
			if (stacks[1].stackSize < stacks[1].getMaxStackSize()){
				int chg = Math.min(itemCount, stacks[1].getMaxStackSize()-stacks[1].stackSize);
				stacks[1].stackSize += chg;
				itemCount -= chg;
			}
		}
	}
	
	@Override
	public void updateEntity() {
		updateFilter();
		consolidateStacks();
		super.updateEntity();
	}
	
	@Override
	public int getSizeInventory() {
		return 2;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return stacks[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
        if (this.stacks[i] != null)
        {
            ItemStack var3;

            if (this.stacks[i].stackSize <= j)
            {
                var3 = this.stacks[i];
                this.stacks[i] = null;
                this.onInventoryChanged();
                return var3;
            }
            else
            {
                var3 = this.stacks[i].splitStack(j);

                if (this.stacks[i].stackSize == 0)
                {
                    this.stacks[i] = null;
                }

                this.onInventoryChanged();
                return var3;
            }
        }
        else
        {
            return null;
        }
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		stacks[i] = itemstack;
	}

	@Override
	public String getInvName() {
		return "crate";
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return false;
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
		return stacks[0] == null || (ItemStack.areItemStackTagsEqual(stacks[0], itemstack) && stacks[0].isItemEqual(itemstack));
	}

	@Override
	public int[] getSizeInventorySide(int var1) { //Get slots accessible from side
		switch (ForgeDirection.getOrientation(var1)) {
		case UP:
			return new int[]{0};
		case DOWN:
			return new int[]{1};
		}
		return new int[]{};
	}

	@Override
	public boolean func_102007_a(int i, ItemStack itemstack, int j) { //Can insert to slot from side
		return j==1;
	}

	@Override
	public boolean func_102008_b(int i, ItemStack itemstack, int j) { //Can extract from slot from side
		return j==0;
	}

}
