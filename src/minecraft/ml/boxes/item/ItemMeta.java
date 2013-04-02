package ml.boxes.item;

import java.util.List;

import cpw.mods.fml.common.registry.LanguageRegistry;

import ml.boxes.Boxes;
import ml.boxes.data.ItemIBox;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemMeta extends ItemBlock {

	public ItemMeta(int par1) {
		super(par1);
		setHasSubtypes(true);
		setCreativeTab(Boxes.BoxTab);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		par3List.add(new ItemStack(this, 1, 0));
		par3List.add(new ItemStack(this, 1, 1));
	}
	
	@Override
	public String getItemDisplayName(ItemStack par1ItemStack) {

		return LanguageRegistry.instance().getStringLocalization("item.box.name", "en_US");
	}

	@Override
	public int getMetadata(int par1) {
		return par1;
	}
	
}
