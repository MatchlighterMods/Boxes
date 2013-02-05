package ml.boxes;

import ml.boxes.network.packets.PacketUpdateData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBox extends TileEntity implements IInventory, IBox {

	public float prevAngleOuter = 0F; //Used for smoothness when FPS > 1 tick
	public float flapAngleOuter = 0F;
	
	public float prevAngleInner = 0F; //Used for smoothness when FPS > 1 tick
	public float flapAngleInner = 0F;
	
	public int facing = 0;
	private int syncTime = 0;
	private int users = 0;
	
	public BoxData data = new BoxData();
	
	public TileEntityBox() {
		
	}
	
	@Override
	public void updateEntity() {
		
		super.updateEntity();
		if ((++syncTime % 20) == 0)
			worldObj.addBlockEvent(xCoord, yCoord, zCoord, Boxes.config.boxBlockID, 2, facing);
		
		prevAngleOuter = flapAngleOuter;
		prevAngleInner = flapAngleInner;
		float fc = 0.1F;
		if (users > 0 && flapAngleOuter == 0){
			// TODO Sound
		}
		if (users == 0 && flapAngleOuter > 0 || users > 0 && flapAngleInner < 1F){
						
			if (users>0){
				flapAngleOuter += fc;
				if (flapAngleOuter>0.5)
					flapAngleInner += fc;
			} else {
				flapAngleInner -= fc;
				if (flapAngleInner<0.5)
					flapAngleOuter -= fc;
			}
			
			if (flapAngleOuter>1.0F)
				flapAngleOuter = 1.0F;
			if (flapAngleInner>1.0F)
				flapAngleInner = 1.0F;
			
			if (flapAngleOuter < 0.5 && prevAngleOuter >= 0.5){
				// TODO Sound
			}
			
			if (flapAngleOuter<0F)
				flapAngleOuter = 0F;
			if (flapAngleInner<0F)
				flapAngleInner = 0F;
		}
	}

	@Override
	public void receiveClientEvent(int par1, int par2) {
		switch (par1) {
		case 1:
			users = par2;
			break;
		case 2:
			facing = par2;
			break;
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readFromNBT(par1nbtTagCompound);
		data=new BoxData(par1nbtTagCompound.getCompoundTag("box"));
		facing=par1nbtTagCompound.getInteger("faceDir");
	}

	@Override
	public void writeToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeToNBT(par1nbtTagCompound);
		par1nbtTagCompound.setCompoundTag("box", data.asNBTTag());
		par1nbtTagCompound.setInteger("faceDir", facing);
	}

	public void setFacing(int f){
		facing = f;
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
		users++;
		worldObj.addBlockEvent(xCoord, yCoord, zCoord, Boxes.config.boxBlockID, 1, users);
	}

	@Override
	public void closeChest() {
		users--;
		worldObj.addBlockEvent(xCoord, yCoord, zCoord, Boxes.config.boxBlockID, 1, users);
	}

	@Override
	public void saveData() {}

	@Override
	public void boxOpen() {
		openChest();
	}

	@Override
	public void boxClose() {
		closeChest();
	}

	@Override
	public BoxData getBoxData() {
		return data;
	}

	@Override
	public Packet getDescriptionPacket() {
		return (new PacketUpdateData(this)).convertToPkt250();
	}

}
