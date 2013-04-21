package ml.boxes.data;

import net.minecraft.item.ItemStack;

public interface IBoxContainer {

	public void saveData();
	
	public void boxOpen();
	
	public void boxClose();
	
	public Box getBox();
	
	public void ejectItem(ItemStack is);
}
