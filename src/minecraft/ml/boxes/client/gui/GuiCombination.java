package ml.boxes.client.gui;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import ml.boxes.Boxes;
import ml.boxes.client.render.tile.SafeTESR;
import ml.core.gui.GuiRenderUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GuiCombination extends GuiScreen {

	public int xSize = 126;
	public int ySize = 57;
	public int guiLeft;
	public int guiTop;

	@Override
	public void initGui() {
		super.initGui();
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
		
		for (int i=0; i<3; i++){
			
		}
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		drawDefaultBackground();

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		this.mc.renderEngine.bindTexture("/mods/Boxes/textures/gui/comboBg.png");
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glPushMatrix();
		GL11.glTranslatef(guiLeft+xSize/2, guiTop+ySize/2 + 0.5F, 50.0F);

		GL11.glScalef(-30F, 30F, 30F);
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0F);

		GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(2F, 0, 0);

		GL11.glPushMatrix();			
		mc.renderEngine.bindTexture("/mods/Boxes/textures/models/dials.png");
		SafeTESR.instance.sModel.renderPart("ComboBack");
		for (int i=0; i<3; i++){
			GL11.glPushMatrix();
			GL11.glTranslatef(-0.75F*(float)i, 0F, 0F);
			GL11.glRotatef(-36*(i+1), 1F, 0, 0);

			SafeTESR.instance.sModel.renderPart("Wheel_Sides");
			SafeTESR.instance.sModel.renderPart("Wheel_Num");
			GL11.glPopMatrix();
		}
		GL11.glPopMatrix();

		GL11.glPopMatrix();
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);

		super.drawScreen(par1, par2, par3);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
