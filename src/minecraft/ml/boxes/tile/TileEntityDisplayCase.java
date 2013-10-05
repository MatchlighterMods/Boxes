package ml.boxes.tile;

import ml.boxes.Boxes;
import ml.boxes.network.packets.PacketDescribeDisplay;
import ml.core.block.BlockUtils;
import ml.core.vec.Vector3d;
import ml.core.vec.transform.Rotation;
import ml.core.vec.transform.Transformation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityDisplayCase extends TileEntityEvented implements IInventory {

	public ForgeDirection facing = ForgeDirection.UP;
	public ForgeDirection rotation = ForgeDirection.NORTH;
	
	public @SideOnly(Side.CLIENT) ItemStack rItem;
	
	private ItemStack[] inventory;
	
	public TileEntityDisplayCase() {
		inventory = new ItemStack[getSizeInventory()];
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		facing = ForgeDirection.getOrientation(tag.getInteger("facing"));
		rotation = ForgeDirection.getOrientation(tag.getInteger("rot"));
		
		NBTTagList nbttaglist = tag.getTagList("Items");
		inventory = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++)
		{
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 0xff;
			if (j >= 0 && j < inventory.length)
			{
				inventory[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("facing", facing.ordinal());
		tag.setInteger("rot", rotation.ordinal());
		
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < inventory.length; i++)
		{
			if (inventory[i] != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				inventory[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		tag.setTag("Items", nbttaglist);
	}
	
	@Override
	public Packet getDescriptionPacket() {
		return new PacketDescribeDisplay(this).convertToPkt250();
	}
	
	@Override
	public void onInventoryChanged() {
		super.onInventoryChanged();
		PacketDispatcher.sendPacketToAllInDimension(getDescriptionPacket(), worldObj.provider.dimensionId);
	}
	
	public Transformation getTransformation() {
		Transformation t;
		
		if (facing != ForgeDirection.UP) {
			t = new Rotation(new Vector3d(1, 0, 0), -90);
			t = t.append(new Rotation(new Vector3d(0,1,0), BlockUtils.getRotationFromDirection(facing)));
		} else {
			t = new Rotation(new Vector3d(0,1,0), BlockUtils.getRotationFromDirection(rotation)-90);
		}
		t = t.localize(new Vector3d(0.5D, 0.5D, 0.5D));
		
		return t;
	}

	@Override
	public boolean onRightClicked(EntityPlayer pl, ForgeDirection side) {
		if (worldObj.isRemote) return true;
		
		pl.openGui(Boxes.instance, 4, worldObj, xCoord, yCoord, zCoord);
		return true;
	}
	
	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return inventory[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (this.inventory[i] != null)
		{
			ItemStack var3;

			if (this.inventory[i].stackSize <= j)
			{
				var3 = this.inventory[i];
				this.inventory[i] = null;
				this.onInventoryChanged();
				return var3;
			}
			else
			{
				var3 = this.inventory[i].splitStack(j);

				if (this.inventory[i].stackSize == 0)
				{
					this.inventory[i] = null;
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
		return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		if (isItemValidForSlot(i, itemstack)){
			inventory[i] = itemstack;
			this.onInventoryChanged();
		}else{
			
		}
	}

	@Override
	public String getInvName() {
		return "";
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public void openChest() {
		
	}

	@Override
	public void closeChest() {
		
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
	}
	
}
