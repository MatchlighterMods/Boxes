package ml.boxes.client.render.tile;

import ml.boxes.Registry;
import ml.boxes.block.BlockMeta;
import ml.boxes.block.MetaType;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class BoxesBlockRenderer implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		GL11.glPushMatrix();
		
		if (block instanceof BlockMeta) {
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			switch (MetaType.fromMeta(metadata)){
			case Crate:
				CrateTESR.INSTANCE.render();
				break;
//			case Safe: //In the MetaItemRenderer
//				GL11.glTranslatef(0.5F, 0.5F, 0.5F);
//				GL11.glRotatef(-90F, 0F, 1F, 0F);
//				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
//				
//				SafeTESR.INSTANCE.renderAsItem();
//				break;
			case DisplayCase:
				GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				GL11.glRotatef(180F, 0F, 1F, 0F);
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
					
				DisplayCaseTESR.INSTANCE.render(10);
				break;
			}
		}
		GL11.glPopMatrix();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {
		return false;
	}

	@Override
	public boolean shouldRender3DInInventory() {
		return true;
	}

	@Override
	public int getRenderId() {
		return Registry.MetaBlockRenderID;
	}

}
