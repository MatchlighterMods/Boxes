package ml.boxes;

import ml.boxes.item.ItemBox;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;

public class BoxesCreativeTab extends CreativeTabs {

	public BoxesCreativeTab(String label) {
		super(label);
	}

	@Override
	public ItemStack getIconItemStack() {
		BoxData bd = new BoxData();
		bd.boxColor = ItemDye.dyeColors[12];
		ItemStack is = new ItemStack(Boxes.BlockBox);
		ItemBox.setBoxDataToIS(is, bd);
		return is;
	}
	
}
