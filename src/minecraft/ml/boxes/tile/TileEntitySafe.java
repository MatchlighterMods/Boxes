package ml.boxes.tile;

import ml.boxes.Boxes;
import ml.boxes.Registry;
import ml.boxes.network.packets.PacketDescribeSafe;
import ml.boxes.tile.safe.MechFallback;
import ml.boxes.tile.safe.MechsHelper;
import ml.boxes.tile.safe.SafeMechanism;
import ml.core.item.ItemUtils;
import ml.core.tile.IRotatableTE;
import ml.core.tile.TileEntityConnectable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatMessageComponent;
import net.minecraftforge.common.ForgeDirection;

public class TileEntitySafe extends TileEntityConnectable implements IEventedTE, IRotatableTE {
	
	public SafeInventory inventory;

	public String mechId = "";
	public SafeMechanism mech;

	public float doorAng = 0F;
	public float prevDoorAng = 0F;

	public boolean unlocked = false;
	public int users = 0;

	public TileEntitySafe() {
		inventory = new SafeInventory();
		mech = new MechFallback(this);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);

		mechId = tag.getString("mechType");
		mech = MechsHelper.tryInstantiate(mechId, this);
		mech.loadNBT(tag.getCompoundTag("mechProps"));

		unlocked = tag.getBoolean("unlocked");

		NBTTagList nbttaglist = tag.getTagList("Items");
		for (int i = 0; i < nbttaglist.tagCount(); i++)
		{
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 0xff;
			if (j >= 0 && j < inventory.stacks.length)
			{
				inventory.stacks[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);

		tag.setString("mechType", mechId);
		tag.setCompoundTag("mechProps", mech.saveNBT());

		tag.setBoolean("unlocked", unlocked);

		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < inventory.stacks.length; i++)
		{
			if (inventory.stacks[i] != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				inventory.stacks[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		tag.setTag("Items", nbttaglist);
	}

	@Override
	public Packet getDescriptionPacket() {
		return new PacketDescribeSafe(this).convertToPkt250();
	}

	public void unlock() {
		unlocked = true;
		if (isMaster() && isConnected()) ((TileEntitySafe)getConnected()).unlock();
		worldObj.addBlockEvent(xCoord, yCoord, zCoord, Registry.BlockMeta.blockID, 1, 1);
	}

	public void lock() {
		unlocked = false;
		if (isMaster() && isConnected()) ((TileEntitySafe)getConnected()).lock();
		worldObj.addBlockEvent(xCoord, yCoord, zCoord, Registry.BlockMeta.blockID, 1, 0);
	}

	@Override
	public boolean canConnectWith(TileEntityConnectable remoteTec) {
		TileEntitySafe rtes = (TileEntitySafe)remoteTec;
		return mech.getClass() == rtes.mech.getClass() && mech.canConnectWith(rtes.mech);
	}

	@Override
	public void onConnect(boolean isMaster, TileEntityConnectable remote) {
		
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
		pl.openGui(Boxes.instance, 3, worldObj, xCoord, yCoord, zCoord);
	}

	public boolean onMasterRightClicked(EntityPlayer epl, ForgeDirection side) {
		TileEntitySafe connected = (TileEntitySafe)getConnected();

		if (facing==side) {
			if (canInteract() && (connected == null || connected.canInteract())) {
				if (unlocked) {
					playerOpened(epl);
				} else {
					mech.beginUnlock(epl);
				}
			} else {
				epl.sendChatToPlayer(ChatMessageComponent.func_111066_d("\u00A77\u00A7oThe door is blocked."));
			}
		} else {
			//epl.sendChatToPlayer("\u00A77\u00A7oNever seen anyone open a safe from a side without a door.");
		}
		return true;
	}

	@Override
	public boolean onRightClicked(EntityPlayer epl, ForgeDirection side) {
		if (!worldObj.isRemote) {
			TileEntitySafe master = (TileEntitySafe)getMaster();
			return master.onMasterRightClicked(epl, side);
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
		for (int i=0; i<this.inventory.getSizeInventory(); i++){
			if (inventory.getStackInSlot(i) != null)
				ItemUtils.dropItemIntoWorld(worldObj, xCoord, yCoord, zCoord, inventory.getStackInSlot(i));
		}
	}

	@Override
	public void hostPlaced(EntityLivingBase pl, ItemStack is) {
		if (!worldObj.isRemote) {
			NBTTagCompound tag = is.getTagCompound() != null ? is.getTagCompound() : new NBTTagCompound();
			mech = MechsHelper.tryInstantiate(tag.getString("mechType"), this);
			mech.loadNBT(tag.getCompoundTag("mechProps"));
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
	
	protected class SafeInventory implements IInventory { //Secure the inventory from mods that don't respect ISidedInventory

		private ItemStack[] stacks;
		
		public SafeInventory() {
			stacks = new ItemStack[getSizeInventory()];
		}
		
		@Override
		public int getSizeInventory() {
			return 27;
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
			return null;
		}

		@Override
		public void setInventorySlotContents(int i, ItemStack itemstack) {
			stacks[i] = itemstack;
			onInventoryChanged();
		}

		@Override
		public String getInvName() {
			return "Boxes.safe";
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
			return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == TileEntitySafe.this && isMaster() ? unlocked : ((TileEntitySafe)getMaster()).unlocked;
		}

		@Override
		public void openChest() {
			users +=1;
			worldObj.addBlockEvent(xCoord, yCoord, zCoord, Registry.BlockMeta.blockID, 2, users);
		}

		@Override
		public void closeChest() {
			users -=1;
			worldObj.addBlockEvent(xCoord, yCoord, zCoord, Registry.BlockMeta.blockID, 2, users);
		}

		@Override
		public boolean isItemValidForSlot(int i, ItemStack itemstack) {
			return true;
		}

		@Override
		public void onInventoryChanged() {
			TileEntitySafe.this.onInventoryChanged();
		}

//		@Override
//		public int[] getAccessibleSlotsFromSide(int var1) {
//			return new int[]{};
//		}
//
//		@Override
//		public boolean canExtractItem(int i, ItemStack itemstack, int j) {
//			return false;
//		}
//
//		@Override
//		public boolean canInsertItem(int i, ItemStack itemstack, int j) {
//			return false;
//		}
	}
}
