package ml.boxes.item;

import java.util.List;

import ml.boxes.BoxData;
import ml.boxes.Boxes;
import ml.boxes.Lib;
import ml.boxes.TileEntityBox;
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
		setTextureFile("");
		setIconIndex(0);
		setHasSubtypes(true);
		setCreativeTab(Boxes.BoxTab);
		setItemName("Box");
	}
	
	public static BoxData getDataFromIS(ItemStack is){
		if (is.hasTagCompound()){
			return new BoxData(is.getTagCompound().getCompoundTag("boxData"));
		}
		return new BoxData();
	}
	
	public static void setBoxDataToIS(ItemStack is, BoxData data){
		NBTTagCompound isTag = is.hasTagCompound() ? is.getTagCompound() : new NBTTagCompound();
		isTag.setCompoundTag("boxData", data.asNBTTag());
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
		BoxData box = ItemBox.getDataFromIS(par1ItemStack);
		String nameBuild = "";
		
		if (box != null){
			if (box.boxName != null && !box.boxName.isEmpty())
				return box.boxName;
		}
		
		if (par1ItemStack.getItemDamage()>-1){
			nameBuild += StringTranslate.getInstance().translateKey("item.fireworksCharge." + ItemDye.dyeColorNames[par1ItemStack.getItemDamage()]) + " ";
		}
		nameBuild += LanguageRegistry.instance().getStringLocalization("item.box.name", "en_US");
		return nameBuild;
	}

	@Override
	public int getMetadata(int par1) {
		return par1;
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player,
			World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ, int metadata) {
		
		BoxData box = ItemBox.getDataFromIS(stack);
		
		if (super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata)){
			if (world.getBlockId(x, y, z) == this.blockID)
			{
				TileEntityBox te = (TileEntityBox)world.getBlockTileEntity(x, y, z);
				te.data = box;
			}
		}
		
		return true;
	}

	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs,
			List par3List) {
		for (int i=0; i<15; i++){
			par3List.add(new ItemStack(Boxes.BlockBox, 1, i));
		}
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, World par3World, int par4, int par5,
			int par6, int par7, float par8, float par9, float par10) {
		
		if (par2EntityPlayer.isSneaking() || !Lib.isRealPlayer(par2EntityPlayer)){
			return super.onItemUse(par1ItemStack, par2EntityPlayer, par3World, par4, par5,
					par6, par7, par8, par9, par10);
		}
		
		//par2EntityPlayer.openGui(Boxes.instance, 2, par3World, 0,0,0);
		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World,
			EntityPlayer par3EntityPlayer) {
		par3EntityPlayer.openGui(Boxes.instance, 2, par2World, 0,0,0);
		return super.onItemRightClick(par1ItemStack, par2World, par3EntityPlayer);
	}
	
}
