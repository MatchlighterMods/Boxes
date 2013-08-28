package ml.boxes.client.render.tile;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import ml.boxes.tile.TileEntityDisplayCase;
import ml.core.block.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.common.ForgeDirection;

public class DisplayCaseTESR extends TileEntitySpecialRenderer {
	
	protected static ResourceLocation texMain = new ResourceLocation("Boxes:textures/models/case.png");
	private IModelCustom caseModel = AdvancedModelLoader.loadModel("/assets/boxes/models/displayCase.obj");
	public static DisplayCaseTESR INSTANCE = new DisplayCaseTESR();
	
	public static ResourceLocation testLiner = new ResourceLocation("textures/blocks/wool_colored_light_blue.png");

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1,
			double d2, float f) {

		TileEntityDisplayCase tedc = (TileEntityDisplayCase)tileentity;
		
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glTranslatef((float)d0, (float)d1, (float)d2);
		
		tedc.getTransformation().glTransform();
		
//		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
//		GL11.glRotatef(BlockUtils.getRotationFromDirection(tedc.rotation), 0F, 1F, 0F);
//		if (tedc.facing != ForgeDirection.UP) {
//			
//			GL11.glRotatef(BlockUtils.getRotationFromDirection(tedc.facing), 0F, 1F, 0F);
//			GL11.glRotatef(-90F, 1F, 0F, 0F);
//		}
//		
//		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		
		render(0);
		
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

	public void render(int LideAng) {
		GL11.glScalef(0.0625F, 0.0625F, 0.0625F);

		this.func_110628_a(testLiner);
		caseModel.renderPart("Liner");
		
		this.func_110628_a(texMain);
		caseModel.renderPart("Base");
		caseModel.renderPart("Lid_Frame");
		caseModel.renderPart("Lid_Extras");
	}
	
}
