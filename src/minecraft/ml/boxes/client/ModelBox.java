package ml.boxes.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelBox extends ModelBase {

	private int texX = 128;
	private int texY = 128;
	
	public ModelRenderer bottom;
	public ModelRenderer flap1; //X+
	public ModelRenderer flap2; //X-
	public ModelRenderer flap3; //Z-
	public ModelRenderer flap4; //Z+
	public ModelRenderer sidexm;
	public ModelRenderer sidexp;
	public ModelRenderer sidezm;
	public ModelRenderer sidezp;
	
	public ModelBox() {
		bottom = new ModelRenderer(this, 5, -29).setTextureSize(texX, texY);
		bottom.addBox(0F, 0F, 0F, 14, 1, 14, 0.0F);
		bottom.rotationPointX = 1F;
		bottom.rotationPointZ = 1F;

		flap1 = new ModelRenderer(this, 19, 30).setTextureSize(texX, texY);
		flap1.addBox(0F, 0F, 0F, 14, 1, 6);
		flap1.rotationPointX = 1F;
		flap1.rotationPointY = 13F;
		flap1.rotationPointZ = 2F;

		flap2 = new ModelRenderer(this, -2, 41).setTextureSize(texX, texY);
		flap2.addBox(0F, 0F, -6F, 14, 1, 6);
		flap2.rotationPointX = 1F;
		flap2.rotationPointY = 13F;
		flap2.rotationPointZ = 14F;

		flap3 = new ModelRenderer(this, 30, 16).setTextureSize(texX, texY);
		flap3.addBox(0F, 0F, 0F, 6, 1, 12);
		flap3.rotationPointX = 2F;
		flap3.rotationPointY = 13F;
		flap3.rotationPointZ = 2F;

		flap4 = new ModelRenderer(this, 33, 42).setTextureSize(texX, texY);
		flap4.addBox(-6F, 0F, 0F, 6, 1, 12);
		flap4.rotationPointX = 14F;
		flap4.rotationPointY = 13F;
		flap4.rotationPointZ = 2F;

		sidexm = new ModelRenderer(this, 37, -14).setTextureSize(texX, texY);
		sidexm.addBox(0F, 0F, 0F, 1, 13, 14);
		sidexm.rotationPointX = 1F;
		sidexm.rotationPointY = 1F;
		sidexm.rotationPointZ = 1F;

		sidexp = new ModelRenderer(this, 9, -3).setTextureSize(texX, texY);
		sidexp.addBox(0F, 0F, 0F, 1, 13, 14);
		sidexm.rotationPointX = 14F;
		sidexm.rotationPointY = 1F;
		sidexm.rotationPointZ = 1F;

		sidezm = new ModelRenderer(this, -10, -11).setTextureSize(texX, texY);
		sidezm.addBox(0F, 0F, 0F, 12, 13, 1);
		sidexm.rotationPointX = 2F;
		sidexm.rotationPointY = 1F;
		sidexm.rotationPointZ = 1F;

		sidezp = new ModelRenderer(this, -3, 50).setTextureSize(texX, texY);
		sidezp.addBox(0F, 0F, 0F, 12, 13, 1);
		sidexm.rotationPointX = 2F;
		sidexm.rotationPointY = 1F;
		sidexm.rotationPointZ = 14F;
	}
	
	public void renderAll(){
		bottom.render(0.0625F);
		flap1.render(0.0625F);
		flap2.render(0.0625F);
		flap3.render(0.0625F);
		flap4.render(0.0625F);
		sidexm.render(0.0625F);
		sidexp.render(0.0625F);
		sidezm.render(0.0625F);
		sidezp.render(0.0625F);
	}
}
