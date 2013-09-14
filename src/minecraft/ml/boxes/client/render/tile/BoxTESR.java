package ml.boxes.client.render.tile;

import ml.boxes.client.ModelBox;
import ml.boxes.tile.TileEntityBox;
import ml.core.block.BlockUtils;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BoxTESR extends TileEntitySpecialRenderer {

	protected static ResourceLocation texColor = new ResourceLocation("Boxes:textures/models/boxColor.png");
	protected static ResourceLocation texMain = new ResourceLocation("Boxes:textures/models/box.png");
	
	private ModelBox boxModel = new ModelBox();
	public static BoxTESR instance = new BoxTESR();

	@Override
	public void renderTileEntityAt(TileEntity te, double var2, double var4,
			double var6, float tickTime) {

		TileEntityBox box = (TileEntityBox)te;
		//int meta = box.worldObj.getBlockMetadata(box.xCoord, box.yCoord, box.zCoord);

		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glTranslatef((float)var2, (float)var4, (float)var6);
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		GL11.glRotatef(BlockUtils.getRotationFromDirection(box.facing), 0F, 1F, 0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

		float openAngleInner = box.prevAngleInner + (box.flapAngleInner-box.prevAngleInner)*tickTime;
		openAngleInner = (float)Math.sin(openAngleInner*3.14/2);
		int innerAngle = (int)(120 * openAngleInner);

		float openAngleOuter = box.prevAngleOuter + (box.flapAngleOuter-box.prevAngleOuter)*tickTime;
		openAngleOuter = (float)Math.sin(openAngleOuter*3.14/2);
		int outerAngle = (int)(120 * openAngleOuter); 

		setBoxFlaps(outerAngle + 1, outerAngle+3, innerAngle, innerAngle);

		renderBox(box.getBox().boxColor);

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

		float red = ((color >> 16) & 0xFF)/255F;
		float green = ((color >> 8) & 0xFF)/255F;
		float blue = (color & 0xFF)/255F;

		GL11.glColor4f(red, green, blue, 1.0F);

		this.bindTexture(texColor);
		boxModel.renderAll();

		GL11.glColor4f(0.7F, 0.47F, 0.3F, 1F);
		this.bindTexture(texMain);
		boxModel.renderAll();

		GL11.glColor4f(1F, 1F, 1F, 1F);
	}

}
