package ml.boxes.client;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import ml.boxes.TileEntityBox;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class BoxTERenderer extends TileEntitySpecialRenderer {

	private ModelBox boxModel = new ModelBox();
	
	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4,
			double var6, float var8) {
		
		bindTextureByName("/ml/Boxes/gfx/box.png");
		
		TileEntityBox box = (TileEntityBox)var1;
		
		float red = (box.color >> 16) & 0xFF;
		float green = (box.color >> 8) & 0xFF;
		float blue = box.color & 0xFF;
		
		GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glTranslatef((float)var2, (float)var4, (float)var6);
        GL11.glColor4f(red, green, blue, 1.0F);
        
        boxModel.flap1.rotateAngleX = (float)Math.toRadians(box.flapAngle);
        boxModel.flap2.rotateAngleX = 360F - (float)Math.toRadians(box.flapAngle);
        boxModel.flap3.rotateAngleZ = 360F - (float)Math.toRadians(box.flapAngle);
        boxModel.flap4.rotateAngleZ = (float)Math.toRadians(box.flapAngle);
        
        boxModel.renderAll();
        
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

	}

}
