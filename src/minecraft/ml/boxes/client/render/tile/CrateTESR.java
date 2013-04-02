package ml.boxes.client.render.tile;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import ml.boxes.client.ModelBox;
import ml.core.lib.RenderLib;
import ml.core.model.ObjModel;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;

public class CrateTESR extends TileEntitySpecialRenderer {

	public ObjModel crateModel = ObjModel.loadFromResource("/mods/Boxes/models/crate.obj");
	public static CrateTESR instance = new CrateTESR();

	private void drawBeam(Tessellator tes){

	}

	@Override
	public void renderTileEntityAt(TileEntity te, double d0, double d1,
			double d2, float f) {

		te.worldObj.theProfiler.startSection("Boxes");
		te.worldObj.theProfiler.startSection("crate");

		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glTranslatef((float)d0, (float)d1, (float)d2);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		render();

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();

		te.worldObj.theProfiler.endSection();
		te.worldObj.theProfiler.endSection();

	}
	
	public void render() {
		GL11.glScalef(0.0625F, 0.0625F, 0.0625F);
		bindTextureByName("/mods/Boxes/textures/models/crate.png");
		crateModel.renderAll();
	}

}
