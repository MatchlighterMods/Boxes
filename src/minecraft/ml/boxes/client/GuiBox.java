package ml.boxes.client;

import ml.boxes.TileEntityBox;
import ml.boxes.data.BoxData;
import ml.boxes.inventory.ContainerBox;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class GuiBox extends GuiContainer {

	public GuiBox(ContainerBox container, EntityPlayer pl) {
		super(container);
		this.xSize = 178;
		this.ySize = 152;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2,
			int var3) {
		this.mc.renderEngine.bindTexture(this.mc.renderEngine.getTexture("/ml/boxes/res/boxGui.png"));
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

	}

}
