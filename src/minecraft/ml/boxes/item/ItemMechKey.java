package ml.boxes.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import ml.boxes.api.safe.ItemMechanism;
import ml.boxes.api.safe.SafeMechanism;
import ml.core.item.StackUtils;

public class ItemMechKey extends ItemMechanism {

	@SideOnly(Side.CLIENT)
	private Icon colorIcon;
	
	public ItemMechKey(int par1, SafeMechanism safeMechanism) {
		super(par1, safeMechanism);
	}
	
	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}
	
	@Override
	public int getRenderPasses(int metadata) {
		return 2;
	}
	
	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		itemIcon = par1IconRegister.registerIcon("Boxes:key_lock");
		colorIcon = par1IconRegister.registerIcon("Boxes:key_lock_color");
	}
	
	@Override
	public Icon getIcon(ItemStack stack, int pass) {
		return (pass>0 && StackUtils.hasTagAt(stack, "color")) ? colorIcon : itemIcon;
	}
	
	@Override
	public int getColorFromItemStack(ItemStack stack, int pass) {
		return (pass>0 && StackUtils.hasTagAt(stack, "color")) ? StackUtils.getTag(stack, 0x000000, "color") : 0xFFFFFF;
	}

}
