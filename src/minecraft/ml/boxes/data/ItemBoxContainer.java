package ml.boxes.data;

import ml.boxes.item.ItemBox;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemBoxContainer implements IBoxContainer {

	public final ItemStack stack;
	private final Box data;
	
	public ItemBoxContainer(ItemStack stack) {
		this.stack = stack;
		data = new Box(ItemBox.getBoxNBT(stack), this);
	}
	
	@Override
	public void saveData() {
		ItemBox.saveBoxData(stack, getBox());
	}

	@Override
	public void boxOpen() {}

	@Override
	public void boxClose() {}

	@Override
	public Box getBox() {
		return data;
	}

	@Override
	public void ejectItem(ItemStack is) {} //Should never be called

}
