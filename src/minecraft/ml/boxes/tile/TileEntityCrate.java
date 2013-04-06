package ml.boxes.tile;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ml.boxes.client.render.tile.CrateTESR;
import ml.boxes.network.packets.PacketDescribeCrate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class TileEntityCrate extends TileEntity implements ISidedInventory, IRotatableTE {

	public int CRATE_SIZE = 64*72;
	public ForgeDirection facing = ForgeDirection.NORTH;

	public ItemStack cItem;

	public int itemCount = 0;

	private ItemStack[] stacks;

	public TileEntityCrate() {
		stacks = new ItemStack[2];
	}

	public void testTriggerPacket(){
		if (cItem != stacks[0]) {
			cItem = stacks[0];
			PacketDispatcher.sendPacketToAllInDimension(getDescriptionPacket(), worldObj.getWorldInfo().getDimension());
		}
	}

	@Override
	public Packet getDescriptionPacket() {
		return new PacketDescribeCrate(this).convertToPkt250();
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("containedCount", itemCount);
		tag.setInteger("facing", facing.ordinal());

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
		facing = ForgeDirection.getOrientation(tag.getInteger("facing"));

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

	public void consolidateStacks(){

		int tItems = getTotalItems();
		if (tItems > 0 && (stacks[0] != null || stacks[1] != null)){
			ItemStack tis = stacks[0] != null ? stacks[0].copy() : stacks[1].copy();
			tis.stackSize = 0;

			if (stacks[0] == null)
				stacks[0] = tis.copy();
			if (stacks[1] == null)
				stacks[1] = tis.copy();

			stacks[0].stackSize = 0;
			stacks[1].stackSize = Math.min(tItems, stacks[1].getMaxStackSize());
			tItems -= stacks[1].stackSize;
			itemCount = tItems;
		} else {
			stacks[0] = null;
			stacks[1] = null;
		}
		testTriggerPacket();
	}

	@Override
	public void updateEntity() {
		if (!worldObj.isRemote){
			consolidateStacks();
		}
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

	@Override
	public void setFacing(ForgeDirection fd) {
		facing = fd;
	}

}
