package ml.boxes.item;

import java.util.List;

import ml.boxes.Boxes;
import ml.boxes.Registry;
import ml.boxes.data.Box;
import ml.boxes.data.ItemBoxContainer;
import ml.boxes.tile.TileEntityAbstractBox;
import ml.core.PlayerUtils;
import ml.core.util.StringUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ItemBox extends ItemBlock {

	private int blockID;
	
	public ItemBox(int par1) {
		super(par1);
		blockID = par1+256;
		setMaxStackSize(1);
		setHasSubtypes(true);
		setCreativeTab(Boxes.BoxTab);
		setUnlocalizedName("box");
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 1;
	}
	
	public static NBTTagCompound getBoxNBT(ItemStack is){
		if (is.hasTagCompound())
			return is.getTagCompound().getCompoundTag("boxData");
		return new NBTTagCompound();
	}
	
	public static void saveBoxData(ItemStack is, Box bd){
		NBTTagCompound isTag = is.hasTagCompound() ? is.getTagCompound() : new NBTTagCompound();
		isTag.setCompoundTag("boxData", bd.asNBTTag());
		is.setTagCompound(isTag);
	}

	@Override
	public void addInformation(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		// TODO Add information for the box
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
	}

	@Override
	public String getItemDisplayName(ItemStack par1ItemStack) {
		ItemBoxContainer iib = new ItemBoxContainer(par1ItemStack);
		
		if (iib.getBox() != null){
			if (iib.getBox().boxName != null && !iib.getBox().boxName.isEmpty()){
				return iib.getBox().boxName;
			}
		}
		
		return LanguageRegistry.instance().getStringLocalization("item.box.name", "en_US");
	}

	@Override
	public int getMetadata(int par1) {
		return par1;
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player,
			World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ, int metadata) {
		
		if (super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata)){
			if (world.getBlockId(x, y, z) == this.blockID)
			{
				TileEntityAbstractBox te = (TileEntityAbstractBox)world.getBlockTileEntity(x, y, z);
				te.getBox().loadNBT(ItemBox.getBoxNBT(stack));
			}
		}
		
		return true;
	}

	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		for (int i=0; i<15; i++){
			ItemStack is = new ItemStack(Registry.BlockBox, 1);
			ItemBoxContainer iib = new ItemBoxContainer(is);
			iib.getBox().boxName = getColoredBoxName(i);
			iib.getBox().boxColor = ItemDye.dyeColors[i];
			iib.saveData();
			par3List.add(is);
		}
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, World par3World, int par4, int par5,
			int par6, int par7, float par8, float par9, float par10) {
		
		if (par2EntityPlayer.isSneaking() || !PlayerUtils.isRealPlayer(par2EntityPlayer)){
			return super.onItemUse(par1ItemStack, par2EntityPlayer, par3World, par4, par5,
					par6, par7, par8, par9, par10);
		}

		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World,
			EntityPlayer par3EntityPlayer) {
		
		if (!par3EntityPlayer.isSneaking())
			par3EntityPlayer.openGui(Boxes.instance, 2<<4, par2World, 0,0,0);
		
		return super.onItemRightClick(par1ItemStack, par2World, par3EntityPlayer);
	}
	
	public static String getColoredBoxName(int i){
		return StringUtils.getLColorName(i) + " " + StatCollector.translateToLocal("item.box.name");
	}
}
