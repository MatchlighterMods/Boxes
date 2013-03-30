package ml.boxes.client;

import ml.boxes.TileEntityBox;
import ml.boxes.data.BoxData;
import ml.boxes.data.ItemIBox;
import ml.boxes.inventory.ContainerBox;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class GuiBox extends GuiContainer {

	float cPitch;
	EntityPlayer epl;

	public GuiBox(ContainerBox container, EntityPlayer pl) {
		super(container);
		this.xSize = 178;
		this.ySize = 152;

		epl = pl;
		cPitch = pl.rotationPitch;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2,
			int var3) {
		this.mc.renderEngine.bindTexture("/mods/Boxes/textures/gui/boxGui.png");
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

	}

	@Override
	public void onGuiClosed() {
		epl.rotationPitch = cPitch;
		super.onGuiClosed();
	}

}
