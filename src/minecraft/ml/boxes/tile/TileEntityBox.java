package ml.boxes.tile;

import ml.boxes.Registry;
import ml.boxes.data.Box;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;

public class TileEntityBox extends TileEntityAbstractBox {

	public float prevAngleOuter = 0F; //Used for smoothness when FPS > 1 tick
	public float flapAngleOuter = 0F;
	
	public float prevAngleInner = 0F; //Used for smoothness when FPS > 1 tick
	public float flapAngleInner = 0F;
	
	public ForgeDirection facing = ForgeDirection.NORTH;
	private int syncTime = 0;
	private int users = 0;

	@Override
	protected void init() {
		data = new Box(this);
	}
	
	@Override
	protected void loadBoxData(NBTTagCompound boxTag) {
		this.data = new Box(boxTag, this);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readFromNBT(par1nbtTagCompound);
		facing=ForgeDirection.getOrientation(par1nbtTagCompound.getInteger("faceDir"));
	}
	
	@Override
	public void writeToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeToNBT(par1nbtTagCompound);
		par1nbtTagCompound.setInteger("faceDir", facing.ordinal());
	}
	
	@Override
	public void updateEntity() {
		
		super.updateEntity();
		if ((++syncTime % 20) == 0)
			worldObj.addBlockEvent(xCoord, yCoord, zCoord, Registry.config.boxBlockID, 2, facing.ordinal());
		
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
	public boolean receiveClientEvent(int par1, int par2) {
		switch (par1) {
		case 1:
			users = par2;
			break;
		case 2:
			facing = ForgeDirection.getOrientation(par2);
			break;
		}
		return true;
	}

	public void setFacing(ForgeDirection f){
		facing = f;
		onInventoryChanged();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this && data.isUseableByPlayer(var1);
	}
	
	@Override
	public void openChest() {
		users++;
		worldObj.addBlockEvent(xCoord, yCoord, zCoord, Registry.config.boxBlockID, 1, users);
	}

	@Override
	public void closeChest() {
		users--;
		worldObj.addBlockEvent(xCoord, yCoord, zCoord, Registry.config.boxBlockID, 1, users);
	}

	@Override
	public boolean isInvNameLocalized() {
		return getBox().isInvNameLocalized();
	}
}
