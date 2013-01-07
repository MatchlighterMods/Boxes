package ml.boxes;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockBox extends BlockContainer {

	public BlockBox(int par1) {
		super(par1, Material.wood);
		setBlockBounds(0.0625F, 0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
		setRequiresSelfNotify();
		setCreativeTab(Boxes.BoxTab);
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		// TODO Auto-generated method stub
		return new TileEntityBox();
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return null;
	}

	@Override
	public int getRenderType() {
		return Boxes.boxRendererID;
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
	public int getBlockTextureFromSideAndMetadata(int par1, int par2) {
		return 64;
	}

	@Override
	public String getTextureFile() {
		return "/ml/boxes/gfx/sprites.png";
	}

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3,
			int par4, EntityPlayer par5EntityPlayer, int par6, float par7,
			float par8, float par9) {
		// TODO Auto-generated method stub

		
		return super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer,
				par6, par7, par8, par9);
	}

	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4,
			EntityLiving par5EntityLiving) {
		// TODO Auto-generated method stub
		super.onBlockPlacedBy(par1World, par2, par3, par4, par5EntityLiving);
	}
	
}
