package ml.boxes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBox extends TileEntity implements IInventory {

	public int flapAngle = 120;
	
	public BoxData data = new BoxData();
	
	public TileEntityBox() {
		
	}
	
	@Override
	public void updateEntity() {
		
		super.updateEntity();
	}

	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readFromNBT(par1nbtTagCompound);
		data=new BoxData(par1nbtTagCompound.getCompoundTag("box"));
	}

	@Override
	public void writeToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeToNBT(par1nbtTagCompound);
		par1nbtTagCompound.setCompoundTag("box", data.asNBTTag());
	}

	
	@Override
	public int getSizeInventory() {
		return data.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return data.getStackInSlot(var1);
	}

	@Override
	public ItemStack decrStackSize(int var1, int var2) {
		return data.decrStackSize(var1, var2);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		return data.getStackInSlotOnClosing(var1);
	}

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
		data.setInventorySlotContents(var1, var2);
	}

	@Override
	public String getInvName() {
		return data.getInvName();
	}

	@Override
	public int getInventoryStackLimit() {
		return data.getInventoryStackLimit();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return data.isUseableByPlayer(var1);
	}

	@Override
	public void openChest() {
		
	}

	@Override
	public void closeChest() {
		
	}

}
