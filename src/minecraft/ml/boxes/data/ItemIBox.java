package ml.boxes.data;

import ml.boxes.IBox;
import ml.boxes.item.ItemBox;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemIBox implements IBox {

	public final ItemStack stack;
	private final BoxData data;
	
	public ItemIBox(ItemStack stack) {
		this.stack = stack;
		data = new BoxData(ItemBox.getBoxNBT(stack), this);
	}
	
	@Override
	public void saveData() {
		ItemBox.saveBoxData(stack, getBoxData());
	}

	@Override
	public void boxOpen() {}

	@Override
	public void boxClose() {}

	@Override
	public BoxData getBoxData() {
		return data;
	}

	@Override
	public void ejectItem(ItemStack is) {} //Should never be called

}
