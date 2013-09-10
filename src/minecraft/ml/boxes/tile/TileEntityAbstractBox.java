package ml.boxes.tile;

import ml.boxes.api.box.IBoxContainer;
import ml.boxes.data.Box;
import ml.boxes.inventory.ContainerBox;
import ml.boxes.network.packets.PacketUpdateData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityAbstractBox extends TileEntity implements IInventory, IBoxContainer {

	protected Box data;
	
	public TileEntityAbstractBox() {
		super();
		init();
	}
	
	protected abstract void init();
	
	protected abstract void loadBoxData(NBTTagCompound boxTag);

	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readFromNBT(par1nbtTagCompound);
		loadBoxData(par1nbtTagCompound.getCompoundTag("box"));
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
	public int getInventoryStackLimit() {
		return data.getInventoryStackLimit();
	}

	@Override
	public String getInvName() {
		return data.getInvName();
	}
	
	@Override
	public boolean boxUseableByPlayer(EntityPlayer epl) {return isUseableByPlayer(epl);} //Required to prevent a reobfuscation error

	@Override
	public void boxOpen() {
		openChest();
	}

	@Override
	public void boxClose() {
		closeChest();
	}

	@Override
	public Box getBox() {
		return data;
	}
	
	@Override
	public void saveData() {
		onInventoryChanged();
	}

	@Override
	public Packet getDescriptionPacket() {
		return (new PacketUpdateData(this)).convertToPkt250();
	}

	@Override
	public void ejectItem(ItemStack is) {
		if (!worldObj.isRemote){
			EntityItem ei = new EntityItem(worldObj, 0.5F + xCoord, 1F + yCoord, 0.5F + zCoord, is);
			worldObj.spawnEntityInWorld(ei);
		}
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return getBox().isItemValidForSlot(i, itemstack);
	}

	@Override
	public boolean slotPreClick(ContainerBox cb, int slotNum, int mouseBtn, int action, EntityPlayer par4EntityPlayer) {
		return true;
	}

}