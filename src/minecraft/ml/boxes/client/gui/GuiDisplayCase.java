package ml.boxes.client.gui;

import ml.boxes.client.render.tile.DisplayCaseTESR;
import ml.boxes.inventory.ContainerDisplayCase;
import ml.core.gui.GuiContainerControl;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiDisplayCase extends GuiContainerControl {
	
	public static ResourceLocation bgRes = new ResourceLocation("Boxes:textures/gui/dispCaseGui.png");

	public GuiDisplayCase(ContainerDisplayCase par1Container) {
		super(par1Container);
	}

	@Override
	protected void initControls() {
		
	}

	@Override
	protected void drawBackgroundLayer(float f, int i, int j) {
		GL11.glColor3f(1F, 1F, 1F);
		
		this.mc.getTextureManager().bindTexture(bgRes);
		drawTexturedModalRect(0, 0, 0, 0, xSize, ySize);
		
		GL11.glPushMatrix();
		GL11.glTranslatef(80F, 30F, 10F);
		GL11.glScalef(2F, 2F, 2F);
		GL11.glTranslatef(12F, 12F, 0F);
		GL11.glRotatef(90F, 1F, 0F, 0F);
		GL11.glRotatef(180F, 0F, 0F, 1F);
		
		mc.getTextureManager().bindTexture(DisplayCaseTESR.testLiner);
		DisplayCaseTESR.INSTANCE.caseModel.renderPart("Liner");
		
		mc.getTextureManager().bindTexture(DisplayCaseTESR.texMain);
		DisplayCaseTESR.INSTANCE.caseModel.renderPart("Base");
		
		GL11.glPopMatrix();
	}

}
