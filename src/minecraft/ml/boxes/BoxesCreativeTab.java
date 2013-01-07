package ml.boxes;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BoxesCreativeTab extends CreativeTabs {

	public BoxesCreativeTab(String label) {
		super(label);
	}

	@Override
	public ItemStack getIconItemStack() {
		return new ItemStack(Boxes.BlockBox, 1, 1);
	}
	
}
