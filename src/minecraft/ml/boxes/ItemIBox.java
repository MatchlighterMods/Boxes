package ml.boxes;

import ml.boxes.item.ItemBox;
import net.minecraft.item.ItemStack;

public class ItemIBox implements IBox {

	public final ItemStack stack;
	private final BoxData data;
	public ItemIBox(ItemStack stack) {
		this.stack = stack;
		data = ItemBox.getDataFromIS(stack);
	}
	
	@Override
	public void saveData() { //TODO Fix this
		System.out.println(data.getStackInSlot(0));
		ItemBox.setBoxDataToIS(stack, data);
	}

	@Override
	public void boxOpen() {}

	@Override
	public void boxClose() {}

	@Override
	public BoxData getBoxData() {
		return data;
	}

}
