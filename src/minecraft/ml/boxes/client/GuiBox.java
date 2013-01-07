package ml.boxes.client;

import ml.boxes.BoxData;
import ml.boxes.ContainerBox;
import ml.boxes.TileEntityBox;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class GuiBox extends GuiContainer {

	public GuiBox(BoxData box, EntityPlayer pl) {
		super(new ContainerBox(box, pl));
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2,
			int var3) {
		// TODO Auto-generated method stub

	}

}
