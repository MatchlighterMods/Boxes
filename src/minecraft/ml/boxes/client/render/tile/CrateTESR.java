package ml.boxes.client.render.tile;

import ml.boxes.Registry;
import ml.boxes.block.BlockMeta;
import ml.boxes.tile.TileEntityCrate;
import ml.core.block.BlockUtils;
import ml.core.world.WorldRenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.common.ForgeDirection;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CrateTESR extends TileEntitySpecialRenderer {

	protected static ResourceLocation texMain = new ResourceLocation("Boxes:textures/models/crate.png");
	private IModelCustom crateModel = AdvancedModelLoader.loadModel("/assets/boxes/models/crate.obj");
	private RenderBlocks renderBlocks = new RenderBlocks();
	public static CrateTESR INSTANCE = new CrateTESR();
	
	public static boolean shouldFastRender() {
		return Registry.config.crate_RenderMode !=2 && (!Minecraft.isFancyGraphicsEnabled() || Registry.config.crate_RenderMode==1);
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double d0, double d1, double d2, float f) {

		TileEntityCrate tec = (TileEntityCrate)te;
		Tessellator tes = Tessellator.instance;

		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glTranslatef((float)d0, (float)d1, (float)d2);

		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		BlockUtils.glRotateForFaceDir(tec.facing);
		render();
		if (tec.upg_label) crateModel.renderPart("Label");
		GL11.glPopMatrix();
		
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		if (tec.cItem != null){
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			GL11.glTranslatef(tec.facing.offsetX*0.5F, tec.facing.offsetY*(shouldFastRender() ? 0.7F : 0.6F), tec.facing.offsetZ*0.51F);

			GL11.glRotatef(BlockUtils.getRotationFromDirection(tec.facing), 0, 1.0F, 0F);

			boolean isBlock = tec.cItem.getItem() instanceof ItemBlock;
			boolean upOrDwn = tec.facing == ForgeDirection.UP || tec.facing == ForgeDirection.DOWN;

			int rendMode = isBlock ? Registry.config.crate_BlockRenderMode : Registry.config.crate_ItemRenderMode;

			FontRenderer fr = getFontRenderer();
			if (upOrDwn){
				GL11.glRotatef(FMLClientHandler.instance().getClient().thePlayer.rotationYaw, 0F, -1.0F, 0F);
			}

			GL11.glPushMatrix();
			if (!upOrDwn && !shouldFastRender()) GL11.glTranslatef(0, 0, 0.0625F);
			WorldRenderUtils.renderItemIntoWorldCenteredAt(tec.cItem, !(rendMode == 0 || (rendMode == 1 && !upOrDwn)));
			GL11.glPopMatrix();

			GL11.glPushMatrix();
			if (tec.contentString.length() > 0) {
				if (upOrDwn){
					GL11.glTranslatef(0F, 0.3125F*tec.facing.offsetY + 0.05625F, -0.001F);
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
				} else {
					GL11.glTranslatef(0, 0.4375F, 0);
					GL11.glScalef(0.00926F, 0.00926F, 1F); //0.0625F/6.75F
					GL11.glTranslatef(0, 3.5F, 0);
	
					GL11.glRotatef(180, 0F, 0F, 1F);
					GL11.glTranslatef(-fr.getStringWidth(tec.contentString)/2, 0, -0.001F);
					fr.drawString(tec.contentString, 0, 0, 0);
				}
			}
			GL11.glPopMatrix();
		}
		GL11.glPopAttrib();

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

	public void render() {
		if (shouldFastRender()) {
			this.bindTexture(TextureMap.locationBlocksTexture);
			GL11.glPushMatrix();
			GL11.glTranslated(0.5F, 0.5F, 0.5F);
			renderBlocks.overrideBlockBounds(0, 0, 0, 1, 1, 1);
			BlockMeta.renderTypeOverride = 0;
			renderBlocks.renderBlockAsItem(Registry.BlockMeta, 0, 1F);
			renderBlocks.unlockBlockBounds();
			BlockMeta.resetRenderType();
			GL11.glPopMatrix();
		} else {
			GL11.glScalef(0.0625F, 0.0625F, 0.0625F);
			this.bindTexture(texMain);
			crateModel.renderPart("Border");
			crateModel.renderPart("Supports");
		}
	}

}
