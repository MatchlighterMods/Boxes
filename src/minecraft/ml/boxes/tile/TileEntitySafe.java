package ml.boxes.tile;

import ml.boxes.network.packets.PacketDescribeSafe;
import ml.core.tile.IRotatableTE;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;
import buildcraft.api.inventory.ISpecialInventory;

public class TileEntitySafe extends TileEntity implements IEventedTE, IRotatableTE, ISpecialInventory, IInventory {

	public boolean safeOpen = true;
	public ForgeDirection facing = ForgeDirection.NORTH;
	
	private ItemStack[] stacks;
	
	public int[] combination;
	public int[] dispCombination;
	
	public TileEntitySafe() {
		stacks = new ItemStack[getSizeInventory()];
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		
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
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		
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
	public Packet getDescriptionPacket() {
		return new PacketDescribeSafe(this).convertToPkt250();
	}
	
	@Override
	public int getSizeInventory() {
		return 27;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getInvName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isInvNameLocalized() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void openChest() {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeChest() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int addItem(ItemStack stack, boolean doAdd, ForgeDirection from) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ItemStack[] extractItem(boolean doRemove, ForgeDirection from,
			int maxItemCount) {
		doRemove = false;
		return null;
	}

	@Override
	public ForgeDirection getFacing() {
		return facing;
	}
	
	@Override
	public void setFacing(ForgeDirection fd) {
		facing = fd;
	}

	@Override
	public ForgeDirection[] getValidFacingDirections() {
		return new ForgeDirection[]{ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.EAST, ForgeDirection.WEST,};
	}
	
	@Override
	public boolean onRightClicked(EntityPlayer pl, ForgeDirection side) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLeftClicked(EntityPlayer pl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hostBroken() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onAttemptUpgrade(EntityPlayer pl, ItemStack is, int side) {
		return false;
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		AxisAlignedBB cbb = getBlockType().getCollisionBoundingBoxFromPool(worldObj, xCoord, yCoord, zCoord);
		if (safeOpen){
			cbb = cbb.expand(1, 0, 1);
		}
		return cbb;
	}
}
