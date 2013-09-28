package ml.boxes.api.safe;

import net.minecraft.nbt.NBTTagCompound;

public interface ISafe {

	public void doUnlock();
	
	public NBTTagCompound getMechTag();
	
}
