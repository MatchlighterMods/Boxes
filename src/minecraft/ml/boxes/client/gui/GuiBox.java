package ml.boxes.client.gui;

import ml.boxes.inventory.ContainerBox;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiBox extends GuiContainer {

	protected static ResourceLocation bgRes = new ResourceLocation("Boxes:textures/gui/boxGui.png");
	
	float cPitch;
	EntityPlayer epl;

	public GuiBox(ContainerBox container, EntityPlayer pl) {
		super(container);
		this.xSize = 178;
		this.ySize = 168;

		epl = pl;
		cPitch = pl.rotationPitch;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2,
			int var3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		this.mc.getTextureManager().bindTexture(bgRes);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

	}

	@Override
	public void onGuiClosed() {
		epl.rotationPitch = cPitch;
		super.onGuiClosed();
	}

}
