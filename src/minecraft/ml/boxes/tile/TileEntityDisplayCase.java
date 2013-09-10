package ml.boxes.tile;

import ml.boxes.Boxes;
import ml.boxes.network.packets.PacketDescribeDisplay;
import ml.core.block.BlockUtils;
import ml.core.vec.Vector3;
import ml.core.vec.transform.Matrix4d;
import ml.core.vec.transform.Rotation;
import ml.core.vec.transform.Scale;
import ml.core.vec.transform.Transformation;
import ml.core.vec.transform.Translation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class TileEntityDisplayCase extends TileEntityEvented implements IInventory {

	public ForgeDirection facing = ForgeDirection.UP;
	public ForgeDirection rotation = ForgeDirection.NORTH;
	
	public TileEntityDisplayCase() {
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		facing = ForgeDirection.getOrientation(tag.getInteger("facing"));
		rotation = ForgeDirection.getOrientation(tag.getInteger("rot"));
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("facing", facing.ordinal());
		tag.setInteger("rot", rotation.ordinal());
	}
	
	@Override
	public Packet getDescriptionPacket() {
		return new PacketDescribeDisplay(this).convertToPkt250();
	}
	
	public Transformation getTransformation() {
		Transformation t;
		
		if (facing != ForgeDirection.UP) {
			t = new Rotation(new Vector3(1, 0, 0), -90);
			t = t.append(new Rotation(new Vector3(0,1,0), BlockUtils.getRotationFromDirection(facing)));
		} else {
			t = new Rotation(new Vector3(0,1,0), BlockUtils.getRotationFromDirection(rotation)-90);
		}
		t = t.localize(new Vector3(0.5D, 0.5D, 0.5D));
		
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
		// TODO Auto-generated method stub
		return 1;
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
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
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
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
	}
	
}
