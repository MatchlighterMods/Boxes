package ml.boxes.client.render.tile;

import ml.boxes.tile.TileEntityDisplayCase;
import ml.core.world.WorldRenderUtils;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class DisplayCaseTESR extends TileEntitySpecialRenderer {
	
	public static ResourceLocation texMain = new ResourceLocation("Boxes:textures/models/case.png");
	public IModelCustom caseModel = AdvancedModelLoader.loadModel("/assets/boxes/models/displayCase.obj");
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
		
		render(90);
		
		GL11.glPushMatrix();
		GL11.glTranslatef(0.5F, 0.125F, 0.5F);
		GL11.glRotatef(90F, 1F, 0F, 0F);
		GL11.glRotatef(0F, 0F, 0F, 1F);
		if (tedc.rItem != null) {
			IItemRenderer cstm = MinecraftForgeClient.getItemRenderer(tedc.rItem, ItemRenderType.ENTITY);
			if (tedc.rItem.getItem() instanceof ItemBlock || (cstm != null && cstm.shouldUseRenderHelper(ItemRenderType.ENTITY, tedc.rItem, ItemRendererHelper.BLOCK_3D)))
				GL11.glScalef(0.75F, 0.75F, 0.75F);
			
			WorldRenderUtils.renderItemIntoWorldCenteredAt(tedc.rItem, true);
		}
		GL11.glPopMatrix();
		
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

	public void render(float lidAng) {
		GL11.glPushMatrix();
		GL11.glScalef(0.0625F, 0.0625F, 0.0625F);
		
		this.bindTexture(testLiner);
		caseModel.renderPart("Liner");
		
		this.bindTexture(texMain);
		caseModel.renderPart("Base");
		
		GL11.glTranslatef(14F, 4F, 0);
		GL11.glRotatef(-lidAng, 0, 0, 1);
		GL11.glTranslatef(-14F, -4F, 0);
		caseModel.renderPart("Lid_Frame");
		caseModel.renderPart("Lid_Extras");
		GL11.glPopMatrix();
	}
	
}
