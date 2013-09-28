package ml.boxes.api.safe;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface IMechHandler {
	
	public SafeMechanism createMechanism(ISafe safe, NBTTagCompound data);
	
	public boolean handlesMechForStack(ItemStack is);
	
	public void addInfoToSafe(ItemStack mechanism, ItemStack safe, List lst);
	
}
