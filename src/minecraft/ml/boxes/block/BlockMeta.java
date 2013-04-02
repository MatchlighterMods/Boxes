package ml.boxes.block;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ml.boxes.Boxes;
import ml.boxes.tile.TileEntityCrate;
import ml.boxes.tile.TileEntitySafe;
import ml.boxes.tile.TileEntityBox;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockMeta extends BlockContainer {
	
	public static enum types {
		UNKNOWN,
		Crate,
		Safe;
		
		public static types fromMeta(int meta){
			if (meta >-1 && meta<types.values().length){
				return types.values()[meta];
			}
			return types.UNKNOWN;
		}
	}
	
	public Map<types, Icon> icons = new HashMap<BlockMeta.types, Icon>();

	public BlockMeta(int par1) {
		super(par1, Material.iron);
		setCreativeTab(Boxes.BoxTab);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return null;
	}
	
	@Override
	public TileEntity createTileEntity(World world, int meta) {
		switch(types.fromMeta(meta)){
		case Crate:
			return new TileEntityCrate();
		case Safe:
			return new TileEntitySafe();
		}
		return null;
	}
	
	@Override
	public int damageDropped(int meta) {
		return meta;
	}
	
	@Override
	public int getRenderType() {
		return Boxes.nullRendererID;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean onBlockActivated(World par1World, int x, int y,
			int z, EntityPlayer player, int par6, float par7,
			float par8, float par9) {
		
		TileEntity te = par1World.getBlockTileEntity(x, y, z);
		if (te == null || !(te instanceof TileEntityBox))
			return true;
		
		if (par1World.isBlockSolidOnSide(x, y+1, z, ForgeDirection.DOWN))
			return true;
		
		if (par1World.isRemote)
			return true;
		
		player.openGui(Boxes.instance, 1, par1World, x, y, z);
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z,
			EntityLiving entity, ItemStack is) {
	
		int rot = Math.round((entity.rotationYaw*4F)/360F);
		TileEntity te = world.getBlockTileEntity(x, y, z);
		
		if (te instanceof TileEntityBox){
			((TileEntityBox) te).facing = 4-rot;
			world.markBlockForUpdate(x, y, z);
		}
	}
	
	@Override
	public float getExplosionResistance(Entity par1Entity, World world, int x,
			int y, int z, double explosionX, double explosionY,
			double explosionZ) {

		int meta = world.getBlockMetadata(x, y, z);
		switch(types.fromMeta(meta)){
		case Crate:
			return 20F;
		case Safe:
			return 18000000F;
		}
		
		return 60F;
	}
	
	@Override
	public float getPlayerRelativeBlockHardness(EntityPlayer par1EntityPlayer,
			World world, int x, int y, int z) {

		TileEntity te = world.getBlockTileEntity(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);
		switch(types.fromMeta(meta)){
		case Crate:
			return 2.0F;
		case Safe:
			if (te instanceof TileEntitySafe && ((TileEntitySafe)te).safeOpen){
				return 5.0F;
			}
			return -1;
		}
		return 2.0F;
	}
	
	@Override
	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y,
			int z, int metadata, int fortune) {
		
		ArrayList<ItemStack> iss = new ArrayList<ItemStack>();
		ItemStack is = new ItemStack(world.getBlockId(x, y, z), 1, metadata);
		TileEntity te = world.getBlockTileEntity(x, y, z);
		
		iss.add(is);
		return iss;		
	}
	
	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4,
			int par5, int par6) {
		
		// TODO Drop contents
		
		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}
	
	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		icons.put(types.Crate, par1IconRegister.registerIcon("Boxes:crate"));
		icons.put(types.Safe, par1IconRegister.registerIcon("Boxes:safe"));
	};
	
	@Override
	public Icon getBlockTextureFromSideAndMetadata(int side, int meta) {
		return icons.get(types.fromMeta(meta));
	}
}
