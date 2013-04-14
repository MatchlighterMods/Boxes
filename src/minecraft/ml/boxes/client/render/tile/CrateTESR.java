package ml.boxes.client.render.tile;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import ml.boxes.Boxes;
import ml.boxes.BoxesConfig;
import ml.boxes.client.ModelBox;
import ml.boxes.tile.TileEntityCrate;
import ml.core.lib.BlockLib;
import ml.core.lib.RenderLib;
import ml.core.model.ObjModel;
import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.ForgeDirection;

@SideOnly(Side.CLIENT)
public class CrateTESR extends TileEntitySpecialRenderer {

	public ObjModel crateModel = ObjModel.loadFromResource("/mods/Boxes/models/crate.obj");
	public static CrateTESR instance = new CrateTESR();
	private static EntityItem renderEnt;
	private RenderItem renderItem = new RenderItem() {
		@Override
		public boolean shouldBob() {
			return false;
		}
		@Override
		public boolean shouldSpreadItems() {
			return false;
		};
	};
	private RenderBlocks renderBlocks = new RenderBlocks();

	public CrateTESR() {
		renderItem.setRenderManager(RenderManager.instance);
		renderEnt = new EntityItem(null);
		renderEnt.hoverStart = 0F;
	}

	private void renderItem(ItemStack is, boolean render3D){
		if (!render3D){
			GL11.glPushMatrix();
			GL11.glScalef(0.03125F, 0.03125F, -0.0004F);
			GL11.glTranslatef(8, 8, 0);
			GL11.glRotatef(180F, 0, 0, 1.0F);

			if (!ForgeHooksClient.renderInventoryItem(renderBlocks, RenderManager.instance.renderEngine, is, true, 0, 0F, 0F))
			{
				renderItem.renderItemIntoGUI(getFontRenderer(), RenderManager.instance.renderEngine, is, 0, 0);
			}
			GL11.glPopMatrix();
		} else {
			renderEnt.setEntityItemStack(is);
			renderItem.doRenderItem(renderEnt, 0, 0, 0, 0, 0);
		}
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double d0, double d1,
			double d2, float f) {

		TileEntityCrate tec = (TileEntityCrate)te;
		Tessellator tes = Tessellator.instance;

		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glTranslatef((float)d0, (float)d1, (float)d2);

		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		if (tec.cItem != null){
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			GL11.glTranslatef(tec.facing.offsetX*0.5F, tec.facing.offsetY*0.6F, tec.facing.offsetZ*0.5F);

			GL11.glRotatef(BlockLib.getRotationFromDirection(tec.facing), 0, 1.0F, 0F);

			boolean isBlock = tec.containedIsBlock;
			boolean upOrDwn = tec.facing == ForgeDirection.UP || tec.facing == ForgeDirection.DOWN;

			int rendMode = isBlock ? Boxes.config.crateBlockRenderMode : Boxes.config.crateItemRenderMode;

			FontRenderer fr = getFontRenderer();
			if (upOrDwn){
				GL11.glRotatef(FMLClientHandler.instance().getClient().thePlayer.rotationYaw, 0F, -1.0F, 0F);
			}

			GL11.glPushMatrix();
			if (rendMode == 0 || (rendMode == 1 && !upOrDwn)){
				if (!upOrDwn) GL11.glTranslatef(0, 0, 0.0625F);
				renderItem(tec.cItem, false);
			} else {
				if (isBlock){
					GL11.glScalef(1.2F, 1.2F, 1.2F);
					GL11.glRotatef(90F, 0, 1F, 0);
				} else {
					GL11.glTranslatef(0F, -0.125F, 0F);
				}
				renderItem(tec.cItem, true);
			}
			GL11.glPopMatrix();

			if (tec.renderContentText){
				if (upOrDwn){
					GL11.glPushMatrix();
					GL11.glTranslatef(0F, 0.25F*tec.facing.offsetY + 0.05625F, -0.001F);
					GL11.glNormal3f(0.0F, 1.0F, 0.0F);
					//GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
					//GL11.glRotatef(RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
					GL11.glScalef(-0.0125F, -0.0125F, 0.0125F);
					GL11.glDisable(GL11.GL_LIGHTING);
					GL11.glDepthMask(false);
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					Tessellator tessellator = Tessellator.instance;
					byte b0 = 0;

					GL11.glDisable(GL11.GL_TEXTURE_2D);
					tessellator.startDrawingQuads();
					int j = fr.getStringWidth(tec.contentString) / 2;
					tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.5F);
					tessellator.addVertex((double)(-j - 1), (double)(-1 + b0), 0.0D);
					tessellator.addVertex((double)(-j - 1), (double)(8 + b0), 0.0D);
					tessellator.addVertex((double)(j + 1), (double)(8 + b0), 0.0D);
					tessellator.addVertex((double)(j + 1), (double)(-1 + b0), 0.0D);
					tessellator.draw();
					GL11.glEnable(GL11.GL_TEXTURE_2D);
					fr.drawString(tec.contentString, -fr.getStringWidth(tec.contentString) / 2, b0, 553648127);
					GL11.glDepthMask(true);
					fr.drawString(tec.contentString, -fr.getStringWidth(tec.contentString) / 2, b0, -1);
					GL11.glEnable(GL11.GL_LIGHTING);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					GL11.glPopMatrix();

				} else {
					GL11.glPushMatrix();
					GL11.glTranslatef(0, 0.4375F, 0);
					GL11.glScalef(0.00926F, 0.00926F, 1F); //0.0625F/6.75F
					GL11.glTranslatef(0, 3.5F, 0);

					GL11.glRotatef(180, 0F, 0F, 1F);
					GL11.glTranslatef(-fr.getStringWidth(tec.contentString)/2, 0, -0.001F);
					fr.drawString(tec.contentString, 0, 0, 0);

					GL11.glPopMatrix();
				}
			}

			GL11.glPopMatrix();
		}
		GL11.glPopAttrib();

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		BlockLib.glRotateForFaceDir(tec.facing);

		render();

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();

	}

	public void render() {
		GL11.glScalef(0.0625F, 0.0625F, 0.0625F);
		bindTextureByName("/mods/Boxes/textures/models/crate.png");
		crateModel.renderGroup("Crate");
	}

}
