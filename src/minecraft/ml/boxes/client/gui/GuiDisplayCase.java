package ml.boxes.client.gui;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelCustom;
import ml.boxes.client.render.tile.DisplayCaseTESR;
import ml.boxes.inventory.ContainerDisplayCase;
import ml.core.gui.GuiContainerControl;

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
		
		this.mc.func_110434_K().func_110577_a(bgRes);
		drawTexturedModalRect(0, 0, 0, 0, xSize, ySize);
	}

}
