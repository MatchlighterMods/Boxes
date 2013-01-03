package ml.boxes.client;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import ml.boxes.TileEntityBox;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class BoxTERenderer extends TileEntitySpecialRenderer {

	private ModelBox boxModel = new ModelBox();
	
	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4,
			double var6, float var8) {
				
		TileEntityBox box = (TileEntityBox)var1;
			
		GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glTranslatef((float)var2, (float)var4, (float)var6);
        
        setBoxFlaps(box.flapAngle + 3, box.flapAngle+1, box.flapAngle, box.flapAngle);

        renderBox(box.color);
        
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();

	}

	public void setBoxFlaps(int a, int b, int c, int d){
		boxModel.flap1.rotateAngleX = (float)Math.toRadians(360-a);
        boxModel.flap2.rotateAngleX = (float)Math.toRadians(b);
        boxModel.flap3.rotateAngleZ = (float)Math.toRadians(c);
        boxModel.flap4.rotateAngleZ = (float)Math.toRadians(360-d);
	}
	
	public void renderBox(int color){
			
		bindTextureByName("/ml/Boxes/gfx/box.png");
		boxModel.renderAll();
		
		float red = ((color >> 16) & 0xFF)/255F;
		float green = ((color >> 8) & 0xFF)/255F;
		float blue = (color & 0xFF)/255F;
		
		GL11.glColor4f(red, green, blue, 1.0F);
		
		bindTextureByName("/ml/Boxes/gfx/boxColor.png");
		boxModel.renderAll();
		
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
	
}
