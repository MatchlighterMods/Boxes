package ml.boxes.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemCardboard extends Item {

	public ItemCardboard(int par1) {
		super(par1);
		setUnlocalizedName("cardboard");
		setCreativeTab(CreativeTabs.tabMaterials);
	}

	@Override
	public void updateIcons(IconRegister iconRegister){
		iconIndex = iconRegister.registerIcon("Boxes:cardboard");
	}
}
