package ml.boxes.tile;

import java.util.Arrays;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ml.boxes.Boxes;
import ml.boxes.network.packets.PacketDescribeSafe;
import ml.core.lib.BlockLib;
import ml.core.tile.IRotatableTE;
import net.minecraft.entity.EntityLiving;
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

public abstract class TileEntitySafe extends TileEntity implements IEventedTE, IRotatableTE, ISpecialInventory, IInventory {

	public boolean safeOpen = true;
	public ForgeDirection facing = ForgeDirection.NORTH;
	public ForgeDirection linkedDir = ForgeDirection.UNKNOWN;
		
	private ItemStack[] stacks;
	
	@SideOnly(Side.CLIENT)
	public float doorAng = 0F;
	@SideOnly(Side.CLIENT)
	public float prevDoorAng = 0F;
	
	public TileEntitySafe() {
		stacks = new ItemStack[getSizeInventory()];
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		
		facing = ForgeDirection.getOrientation(tag.getInteger("facing"));
		linkedDir = ForgeDirection.getOrientation(tag.getInteger("linked"));

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
		tag.setInteger("linked", linkedDir.ordinal());

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
	
	public void sendPacket(){
		PacketDispatcher.sendPacketToAllInDimension(getDescriptionPacket(), worldObj.getWorldInfo().getDimension());
	}
	
	@Override
	public Packet getDescriptionPacket() {
		return new PacketDescribeSafe(this).convertToPkt250();
	}
	
	protected abstract boolean canConnectWith(TileEntitySafe remoteTes);
	
	private boolean tryConnection(ForgeDirection fd){
		TileEntity te = worldObj.getBlockTileEntity(xCoord+fd.offsetX, yCoord+fd.offsetY, zCoord+fd.offsetZ);
		if (te instanceof TileEntitySafe){
			TileEntitySafe rtes = (TileEntitySafe)te;
			if (rtes.facing == this.facing &&
					rtes.linkedDir == ForgeDirection.UNKNOWN &&
					rtes.getClass() == this.getClass() &&
					canConnectWith(rtes)){
				
				linkedDir = fd;
				rtes.linkedDir = fd.getOpposite();
				rtes.sendPacket();
				return true;
			}
		}
		
		return false;
	}
	
	public void tryConnection(){
		if (!tryConnection(ForgeDirection.UP)) tryConnection(ForgeDirection.DOWN);
		sendPacket();
	}
	
	public void refreshConnection(){
		TileEntity te = worldObj.getBlockTileEntity(xCoord, yCoord + linkedDir.offsetY, zCoord);
		if (te.getClass() != this.getClass() ||
				!canConnectWith((TileEntitySafe)te) ||
				((TileEntitySafe)te).linkedDir != linkedDir.getOpposite()){
			linkedDir = ForgeDirection.UNKNOWN;
		}
		
		sendPacket();
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		
		prevDoorAng = doorAng;
		float fc = 0.1F;
		if (safeOpen && doorAng == 0F){
			// TODO Sound
		}
		if (!safeOpen && doorAng > 0 || safeOpen && doorAng < 1F){
						
			if (safeOpen){
				doorAng += fc;
			} else {
				doorAng -= fc;
			}
			
			if (doorAng>1.0F)
				doorAng = 1.0F;
			
			if (doorAng < 0.5 && prevDoorAng >= 0.5){
				// TODO Sound
			}
			
			if (doorAng<0F)
				doorAng = 0F;
		}
	}
	
	@Override
	public boolean receiveClientEvent(int par1, int par2) {
		
		return true;
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
	public void openChest() {}

	@Override
	public void closeChest() {}

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
	public abstract boolean onRightClicked(EntityPlayer pl, ForgeDirection side);

	@Override
	public void onLeftClicked(EntityPlayer pl) {}

	@Override
	public void onNeighborBlockChange() {
		refreshConnection();
	}
	
	@Override
	public void hostBroken() {
		// TODO Drop Contents
	}
	
	@Override
	public void hostPlaced(EntityLiving pl, ItemStack is) {
		// TODO copy combo from IS
		tryConnection();
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
		if (linkedDir == ForgeDirection.UP)
			cbb.maxY+=1;
		return cbb;
	}
}
