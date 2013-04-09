package ml.boxes.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class ItemResources extends Item {

	public ItemResources(int par1) {
		super(par1);
		setCreativeTab(CreativeTabs.tabMaterials);
	}

	@Override
	public void updateIcons(IconRegister iconRegister){
		for (ItemType ir : ItemType.values()){
			ir.ico = iconRegister.registerIcon(ir.iconRef);
		}
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}
	
	@Override
	public int getRenderPasses(int metadata) {
		return 1;
	}
	
	@Override
	public Icon getIcon(ItemStack stack, int renderPass) {
		ItemType ir = ItemType.fromMeta(stack.getItemDamage());
		if (ir!=null){
			return ir.ico;
		}
		return null;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		ItemType ir = ItemType.fromMeta(stack.getItemDamage());
		if (ir!=null){
			return "item." + ir.name().toLowerCase() + "";
		}
		return "";
	}
	
	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs,
			List par3List) {
		for (ItemType ir : ItemType.values()){
			par3List.add(new ItemStack(this, 1, ir.ordinal()));
		}
	}
}
