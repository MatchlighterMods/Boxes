package ml.boxes.item;

import java.util.List;

import ml.boxes.Boxes;
import ml.boxes.TileEntityBox;
import ml.boxes.data.BoxData;
import ml.boxes.data.ItemIBox;
import ml.core.lib.PlayerLib;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StringTranslate;
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
	
	public static void saveBoxData(ItemStack is, BoxData bd){
		NBTTagCompound isTag = is.hasTagCompound() ? is.getTagCompound() : new NBTTagCompound();
		isTag.setCompoundTag("boxData", bd.asNBTTag());
		is.setTagCompound(isTag);
	}

	@Override
	public void addInformation(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		// TODO Auto-generated method stub
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
	}

	@Override
	public String getItemDisplayName(ItemStack par1ItemStack) {
		ItemIBox iib = new ItemIBox(par1ItemStack);
		
		if (iib.getBoxData() != null){
			if (iib.getBoxData().boxName != null && !iib.getBoxData().boxName.isEmpty()){
				return iib.getBoxData().boxName;
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
				TileEntityBox te = (TileEntityBox)world.getBlockTileEntity(x, y, z);
				te.getBoxData().loadNBT(ItemBox.getBoxNBT(stack));
			}
		}
		
		return true;
	}

	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs,
			List par3List) {
		for (int i=0; i<15; i++){
			ItemStack is = new ItemStack(Boxes.BlockBox, 1);
			ItemIBox iib = new ItemIBox(is);
			iib.getBoxData().boxName = getColoredBoxName(i);
			iib.getBoxData().boxColor = ItemDye.dyeColors[i];
			iib.saveData();
			par3List.add(is);
		}
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, World par3World, int par4, int par5,
			int par6, int par7, float par8, float par9, float par10) {
		
		if (par2EntityPlayer.isSneaking() || !PlayerLib.isRealPlayer(par2EntityPlayer)){
			return super.onItemUse(par1ItemStack, par2EntityPlayer, par3World, par4, par5,
					par6, par7, par8, par9, par10);
		}

		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World,
			EntityPlayer par3EntityPlayer) {
		if (!par3EntityPlayer.isSneaking())
			par3EntityPlayer.openGui(Boxes.instance, 2, par2World, 0,0,0);
		return super.onItemRightClick(par1ItemStack, par2World, par3EntityPlayer);
	}
	
	public static String getColoredBoxName(int i){
		return StringTranslate.getInstance().translateKey("item.fireworksCharge." + ItemDye.dyeColorNames[i]) + " " + LanguageRegistry.instance().getStringLocalization("item.box.name", "en_US");
	}
}
