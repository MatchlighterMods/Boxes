package ml.boxes.client.render.tile;

import ml.boxes.tile.TileEntityDisplayCase;
import ml.core.render.WorldRenderLib;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class DisplayCaseTESR extends TileEntitySpecialRenderer {
	
	public static ResourceLocation texMain = new ResourceLocation("Boxes:textures/models/case.png");
	public IModelCustom caseModel = AdvancedModelLoader.loadModel("/assets/boxes/models/displayCase.obj");
	public static DisplayCaseTESR INSTANCE = new DisplayCaseTESR();
	
	public static ResourceLocation testLiner = new ResourceLocation("textures/blocks/wool_colored_light_blue.png");

	ItemStack is = new ItemStack(Item.swordDiamond);
	
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
			WorldRenderLib.renderItemIntoWorldCenteredAt(tedc.rItem, true);
		}
		GL11.glPopMatrix();
		
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

	public void render(float lidAng) {
		GL11.glPushMatrix();
		GL11.glScalef(0.0625F, 0.0625F, 0.0625F);
		
		this.func_110628_a(testLiner);
		caseModel.renderPart("Liner");
		
		this.func_110628_a(texMain);
		caseModel.renderPart("Base");
		
		GL11.glTranslatef(14F, 4F, 0);
		GL11.glRotatef(-lidAng, 0, 0, 1);
		GL11.glTranslatef(-14F, -4F, 0);
		caseModel.renderPart("Lid_Frame");
		caseModel.renderPart("Lid_Extras");
		GL11.glPopMatrix();
	}
	
}
