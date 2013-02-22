package ml.boxes;

import ml.boxes.data.BoxData;
import net.minecraft.item.ItemStack;

public interface IBox {

	public void saveData();
	
	public void boxOpen();
	
	public void boxClose();
	
	public BoxData getBoxData();
	
	public void ejectItem(ItemStack is);
}
