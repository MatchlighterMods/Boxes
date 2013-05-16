package ml.boxes.tile;

import java.util.Arrays;
import java.util.List;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ml.boxes.Boxes;
import ml.boxes.network.packets.PacketDescribeSafe;
import ml.boxes.tile.safe.MechFallback;
import ml.boxes.tile.safe.SafeMechanism;
import ml.core.lib.BlockLib;
import ml.core.tile.IRotatableTE;
import ml.core.tile.TileEntityMultiBlock;
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

public class TileEntitySafe extends TileEntity implements IEventedTE, IRotatableTE, ISpecialInventory, IInventory {

	public ForgeDirection facing = ForgeDirection.NORTH;
	public ForgeDirection linkedDir = ForgeDirection.UNKNOWN;
		
	private ItemStack[] stacks;
	
	public SafeMechanism mech;
	
	public float doorAng = 0F;
	public float prevDoorAng = 0F;
	
	public boolean unlocked = false;
	public int users = 0;
	
	public TileEntitySafe() {
		stacks = new ItemStack[getSizeInventory()];
		mech = new MechFallback(this);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		
		facing = ForgeDirection.getOrientation(tag.getInteger("facing"));
		linkedDir = ForgeDirection.getOrientation(tag.getInteger("linked"));
		
		mech = SafeMechanism.tryInstantialize(tag.getString("mechType"), this);
		mech.loadNBT(tag.getCompoundTag("mechProps"));

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
		
		tag.setString("mechType", mech.getClass().getName());
		tag.setCompoundTag("mechProps", mech.saveNBT());

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
	
	public void unlock() {
		unlocked = true;
		worldObj.addBlockEvent(xCoord, yCoord, zCoord, Boxes.BlockMeta.blockID, 1, 1);
	}
	
	public void lock() {
		unlocked = false;
		worldObj.addBlockEvent(xCoord, yCoord, zCoord, Boxes.BlockMeta.blockID, 1, 0);
	}
	
	protected boolean canConnectWith(TileEntitySafe remoteTes) {
		return remoteTes != null && mech.getClass() == remoteTes.mech.getClass() && mech.matches(remoteTes.mech);
	}
	
//	@Override
//	public void onCreation(boolean ismaster) {
//		linkedDir = ForgeDirection.DOWN;
//		sendPacket();
//		super.onCreation(ismaster);
//	}
//	
//	@Override
//	public void onDestruction(boolean ismaster) {
//		linkedDir = ForgeDirection.UNKNOWN;
//		sendPacket();
//		super.onDestruction(ismaster);
//	}
//	
//	@Override
//	public void getRemotes(List<TileEntityMultiBlock> remotes) {
//		remotes.add((TileEntityMultiBlock)worldObj.getBlockTileEntity(xCoord, yCoord+1, zCoord));
//	}
//	
//	@Override
//	public boolean canCreateMulti() {
//		TileEntity rte = worldObj.getBlockTileEntity(xCoord, yCoord+1, zCoord);
//		if (rte instanceof TileEntitySafe) {
//			TileEntitySafe rtes = (TileEntitySafe)rte;
//			return rtes.facing == this.facing &&
//					(rtes.linkedDir == ForgeDirection.UNKNOWN || rtes.linkedDir == ForgeDirection.DOWN) &&
//					mech.getClass() == rtes.mech.getClass() && mech.matches(rtes.mech);
//		}
//		return false;
//	}
//
//	@Override
//	public boolean onMBRightClicked(EntityPlayer epl, int sx, int sy, int sz) {
//		if (!worldObj.isRemote) {
//			if (unlocked) {
//				playerOpened(epl);
//			} else {
//				mech.beginUnlock(epl);
//			}
//		}
//		return true;
//	}
//
//	@Override
//	public TileEntityMultiBlock getMaster() {
//		if (linkedDir == ForgeDirection.DOWN) {
//			TileEntity te = worldObj.getBlockTileEntity(xCoord, yCoord-1, zCoord);
//			if (te instanceof TileEntitySafe) {
//				return (TileEntitySafe)te;
//			}
//		}
//		return this;
//	}
	
	private boolean tryConnection(ForgeDirection fd){
		TileEntity te = worldObj.getBlockTileEntity(xCoord+fd.offsetX, yCoord+fd.offsetY, zCoord+fd.offsetZ);
		if (te instanceof TileEntitySafe){
			TileEntitySafe rtes = (TileEntitySafe)te;
			if (rtes.facing == this.facing &&
					rtes.linkedDir == ForgeDirection.UNKNOWN &&
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
		if (linkedDir != ForgeDirection.UNKNOWN) {
			TileEntity te = worldObj.getBlockTileEntity(xCoord, yCoord + linkedDir.offsetY, zCoord);
			if (!canConnectWith((TileEntitySafe)te) ||
					((TileEntitySafe)te).linkedDir != linkedDir.getOpposite()){
				linkedDir = ForgeDirection.UNKNOWN;
				sendPacket();
			}
		}
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		
		prevDoorAng = doorAng;
		float fc = 0.1F;
		if (unlocked && doorAng == 0F){
			// TODO Sound
		}
		
		float cangle = unlocked ? (users>0 ? 1F : 0.1F) : 0F;
		
		if (doorAng > cangle) {
			if (doorAng == 1F && cangle == 0.1F){
				// TODO Close sound
			} else if (doorAng == 0.1F && cangle == 0F) {
				// TODO Lock sound
			}
			
			doorAng -= fc;
			if (doorAng < cangle)
				doorAng = cangle;
		} else if (doorAng < cangle) {
			if (doorAng == 0 && cangle == 0.1F){
				// TODO Unlock sound
			} else if (doorAng == 0.1F && cangle == 1F) {
				// TODO Open sound
			}
			
			doorAng += fc;
			if (doorAng > cangle)
				doorAng = cangle;
		}
	}
	
	@Override
	public boolean receiveClientEvent(int evt, int arg) {
		switch (evt) {
		case 1:
			unlocked = arg==1;
			break;
		case 2:
			users = arg;
		}
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
		return "Boxes.safe";
	}

	@Override
	public boolean isInvNameLocalized() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void openChest() {
		users +=1;
		worldObj.addBlockEvent(xCoord, yCoord, zCoord, Boxes.BlockMeta.blockID, 2, users);
	}

	@Override
	public void closeChest() {
		users -=1;
		worldObj.addBlockEvent(xCoord, yCoord, zCoord, Boxes.BlockMeta.blockID, 2, users);
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
	
	public boolean canInteract() {
		return !worldObj.isBlockSolidOnSide(xCoord+facing.offsetX, yCoord+facing.offsetY, zCoord+facing.offsetZ, facing.getOpposite());
	}
	
	public void playerOpened(EntityPlayer pl) {
		pl.openGui(Boxes.instance, 4, worldObj, xCoord, yCoord, zCoord);
	}
	
	@Override
	public boolean onRightClicked(EntityPlayer epl, ForgeDirection side) {
		if (!worldObj.isRemote) {
			if (linkedDir == ForgeDirection.DOWN) {
				TileEntity rte = worldObj.getBlockTileEntity(xCoord, yCoord-1, zCoord);
				if (rte instanceof TileEntitySafe) {
					((TileEntitySafe)rte).onRightClicked(epl, side);
				}
			} else {
				TileEntity rte = worldObj.getBlockTileEntity(xCoord, yCoord+1, zCoord);
				if ((linkedDir!=ForgeDirection.UP || (rte instanceof TileEntitySafe && ((TileEntitySafe)rte).canInteract())) && canInteract()) {
					if (unlocked) {
						playerOpened(epl);
					} else {
						mech.beginUnlock(epl);
					}
				} else {
					epl.sendChatToPlayer("\u00A77\u00A7oThe door is blocked.");
				}
			}
		}
		return true;
	}

	@Override
	public void onLeftClicked(EntityPlayer pl) {}

	@Override
	public void onNeighborBlockChange() {
		refreshConnection();
		
		TileEntity rte = worldObj.getBlockTileEntity(xCoord, yCoord+1, zCoord);
		if ((linkedDir==ForgeDirection.UP && !((TileEntitySafe)rte).canInteract()) || !canInteract()) {
			lock();
		}
	}
	
	@Override
	public void hostBroken() {
		// TODO Drop Contents
	}
	
	@Override
	public void hostPlaced(EntityLiving pl, ItemStack is) {
		// TODO Copy mechanism data from ItemStack
		if (!worldObj.isRemote) {
			mech = new MechFallback(this);
			tryConnection();
		}
	}

	@Override
	public boolean onAttemptUpgrade(EntityPlayer pl, ItemStack is, int side) {
		return false;
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		AxisAlignedBB cbb = getBlockType().getCollisionBoundingBoxFromPool(worldObj, xCoord, yCoord, zCoord);
		if (unlocked){
			cbb = cbb.expand(1, 0, 1);
		}
		if (linkedDir == ForgeDirection.UP)
			cbb.maxY+=1;
		return cbb;
	}
}
