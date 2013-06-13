package ml.boxes.item;

import java.util.List;

import ml.boxes.Boxes;
import ml.boxes.block.MetaType;
import ml.boxes.tile.safe.MechsHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ItemBoxBlocks extends ItemBlock {

	public ItemBoxBlocks(int par1) {
		super(par1);
		setHasSubtypes(true);
		setMaxDamage(0);
		setCreativeTab(Boxes.BoxTab);
	}

	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List lst) {
		for (MetaType mt : MetaType.values()){
			if (!mt.hidden){
				lst.add(new ItemStack(this, 1, mt.ordinal()));
			}
		}
	}
	
	@Override
	public String getItemDisplayName(ItemStack is) {
		MetaType mt = MetaType.fromMeta(is.getItemDamage());
		return mt != null ? LanguageRegistry.instance().getStringLocalization(mt.ulName, "en_US") : "";
	}
	
	@Override
	public void addInformation(ItemStack is, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		MetaType mt = MetaType.fromMeta(is.getItemDamage());
		switch(mt){
		case Safe:
			MechsHelper.attemptGetISInfo(is, par3List);
			return;
		}
	}

	@Override
	public int getMetadata(int par1) {
		return par1;
	}
	
}
