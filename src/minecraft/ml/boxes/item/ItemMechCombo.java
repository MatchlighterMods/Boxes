package ml.boxes.item;

import java.util.List;

import ml.boxes.api.safe.ItemMechanism;
import ml.boxes.api.safe.SafeMechanism;
import ml.boxes.tile.safe.MechCombo;
import ml.core.item.StackUtils;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMechCombo extends ItemMechanism {

	@SideOnly(Side.CLIENT)
	private Icon[] comboTiles;
	
	public ItemMechCombo(int par1, SafeMechanism safeMechanism) {
		super(par1, safeMechanism);
	}
	
	public boolean isValid(ItemStack is) {
		int[] combo = getCombinationArray(is);
		if (combo.length != 3) return false;
		for (int i : combo) {
			if (i<0 || i > 15) return false;
		}
		return true;
	}
	
	public int[] getCombinationArray(ItemStack is) {
		return StackUtils.getTag(is, new int[]{0,0,0}, MechCombo.comboTagName);
	}

	@Override
	public void addInformation(ItemStack is, EntityPlayer par2EntityPlayer, List lst, boolean par4) {
		if (!isValid(is)) {
			lst.add(EnumChatFormatting.DARK_RED.toString() + EnumChatFormatting.ITALIC.toString() + "Invalid");
			return;
		}
		super.addInformation(is, par2EntityPlayer, lst, par4);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister ir) {
		this.itemIcon = ir.registerIcon("Boxes:combo_mech");
		comboTiles = new Icon[3];
		for (int i=0; i<3; i++) {
			comboTiles[i] = ir.registerIcon(String.format("Boxes:combo_mech_c%d", i));
		}
	}
	
	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}
	
	@Override
	public int getRenderPasses(int metadata) {
		return 4;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(ItemStack stack, int pass) {
		return (pass == 3 ? itemIcon : comboTiles[pass]);
	}
	
	@Override
	public int getColorFromItemStack(ItemStack stack, int pass) {
		if (!isValid(stack)) return 0xFFFFFF;
		int[] combination = getCombinationArray(stack);
		return (pass == 3 ? 0xFFFFFF : ItemDye.dyeColors[combination[pass]]);
	}
}
