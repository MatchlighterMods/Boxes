package ml.boxes.item;

import java.util.List;

import ml.boxes.Boxes;
import ml.boxes.block.BlockMeta;
import ml.boxes.block.MetaType;
import ml.boxes.tile.safe.MechRegistry;
import ml.core.item.StackUtils;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemBoxBlocks extends ItemBlock {

	public int blkID;
	
	public ItemBoxBlocks(int par1) {
		super(par1);
		blkID = par1 + 256;
		setHasSubtypes(true);
		setMaxDamage(0);
		setCreativeTab(Boxes.BoxTab);
	}

	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List lst) {
		for (MetaType mt : MetaType.values()){
			if (!mt.hidden()){
				lst.add(new ItemStack(this, 1, mt.ordinal()));
			}
		}
	}
	
	@Override
	public String getItemDisplayName(ItemStack is) {
		MetaType mt = MetaType.fromMeta(is.getItemDamage());
		return mt != null ? StatCollector.translateToLocal("block."+mt.toString().toLowerCase()+".name") : "";
	}
	
	@Override
	public void addInformation(ItemStack is, EntityPlayer par2EntityPlayer, List lst, boolean par4) {
		MetaType mt = MetaType.fromMeta(is.getItemDamage());
		NBTTagCompound stackTag = StackUtils.getStackTag(is);
		
		switch(mt){
		case Safe:
			MechRegistry.addInfoForSafe(stackTag.getCompoundTag("mech_data"), is, lst);
			return;
		}
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
		
		boolean ret = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
		((BlockMeta)Block.blocksList[this.blkID]).onBlockPlacedByFull(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
		return ret;
	}

	@Override
	public int getMetadata(int par1) {
		return par1;
	}
	
}
