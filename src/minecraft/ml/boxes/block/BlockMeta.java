package ml.boxes.block;

import ml.boxes.Boxes;
import ml.boxes.tile.IEventedTE;
import ml.boxes.tile.TileEntityCrate;
import ml.boxes.tile.TileEntitySafe;
import ml.core.lib.BlockLib;
import ml.core.tile.IRotatableTE;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockMeta extends BlockContainer {
	
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
		switch(MetaType.fromMeta(meta)){
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
	public void onBlockClicked(World world, int x, int y, int z,
			EntityPlayer par5EntityPlayer) {
		
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (te instanceof IEventedTE){
			((IEventedTE)te).onLeftClicked(par5EntityPlayer);
		}
	}
	
	@Override
	public boolean onBlockActivated(World par1World, int x, int y,
			int z, EntityPlayer player, int par6, float par7,
			float par8, float par9) {
		
		TileEntity te = par1World.getBlockTileEntity(x, y, z);
		if (te instanceof IEventedTE){
			if (((IEventedTE)te).onRightClicked(player, ForgeDirection.getOrientation(par6)))
				return true;
		}
		
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z,
			EntityLiving entity, ItemStack is) {
		
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (te instanceof IRotatableTE){
			((IRotatableTE) te).setFacing(BlockLib.getPlacedForgeDir(entity, x, y, z, ((IRotatableTE) te).getValidFacingDirections()));
			world.markBlockForUpdate(x, y, z);
		}
		if (te instanceof IEventedTE){
			((IEventedTE)te).hostPlaced(entity, is);
		}
	}
	
	@Override
	public float getExplosionResistance(Entity par1Entity, World world, int x,
			int y, int z, double explosionX, double explosionY,
			double explosionZ) {

		int meta = world.getBlockMetadata(x, y, z);
		switch(MetaType.fromMeta(meta)){
		case Crate:
			return 4.0F;
		case Safe:
			return 18000000F;
		}
		
		return 12F;
	}
	
	@Override
	public float getPlayerRelativeBlockHardness(EntityPlayer par1EntityPlayer,
			World world, int x, int y, int z) {
		
		TileEntity te = world.getBlockTileEntity(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);
		switch(MetaType.fromMeta(meta)){
		case Crate:
			return 0.05F;
		case Safe:
			if (te instanceof TileEntitySafe && ((TileEntitySafe)te).safeOpen){
				return 0.01F;
			}
			return 0F;
		}
		return 0.1F;
	}
	
	@Override
	public boolean canHarvestBlock(EntityPlayer player, int meta) {
		return true;
	}
	
//	@Override
//	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y,
//			int z, int metadata, int fortune) {
//		
//		ArrayList<ItemStack> iss = new ArrayList<ItemStack>();
//		ItemStack is = new ItemStack(world.getBlockId(x, y, z), 1, metadata);
//		TileEntity te = world.getBlockTileEntity(x, y, z);
//		
//		iss.add(is);
//		return iss;		
//	}
	
	@Override
	public void onNeighborBlockChange(World par1World, int x, int y,
			int z, int neighborID) {
		super.onNeighborBlockChange(par1World, x, y, z, neighborID);
		
		TileEntity te = par1World.getBlockTileEntity(x, y, z);
		if (te instanceof IEventedTE){
			((IEventedTE)te).onNeighborBlockChange();
		}
	}
	
	@Override
	public void breakBlock(World par1World, int x, int y, int z,
			int par5, int meta) {
		
		TileEntity te = par1World.getBlockTileEntity(x, y, z);
		if (te instanceof IEventedTE){
			((IEventedTE)te).hostBroken();
		}
		
		super.breakBlock(par1World, x, y, z, par5, meta);
	}
	
	@Override
	public MovingObjectPosition collisionRayTrace(World par1World, int par2,
			int par3, int par4, Vec3 par5Vec3, Vec3 par6Vec3) {
		// TODO Auto-generated method stub
		return super.collisionRayTrace(par1World, par2, par3, par4, par5Vec3, par6Vec3);
	}
	
	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		for (MetaType typ : MetaType.values()){
			typ.ricon = par1IconRegister.registerIcon(typ.icon);
		}
	};
	
	@Override
	public Icon getIcon(int side, int meta) {
		return MetaType.fromMeta(meta).ricon;
	}
}
