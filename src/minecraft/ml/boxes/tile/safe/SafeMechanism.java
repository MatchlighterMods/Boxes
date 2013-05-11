package ml.boxes.tile.safe;

import ml.boxes.tile.TileEntitySafe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public abstract class SafeMechanism {

	public final TileEntitySafe safe;
	
	public SafeMechanism(TileEntitySafe tsafe) {
		this.safe = tsafe;
	}
	
	public abstract NBTTagCompound saveNBT();
	public abstract void loadNBT(NBTTagCompound mechKey);
	
	public abstract void beginUnlock(EntityPlayer epl);
	
	public abstract boolean matches(SafeMechanism tmech);
	
	public void onClosed(){};
}
