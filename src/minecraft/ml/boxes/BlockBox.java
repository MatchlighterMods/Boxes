package ml.boxes;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockBox extends BlockContainer {

	public BlockBox(int par1) {
		super(par1, Material.wood);
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
	
}
