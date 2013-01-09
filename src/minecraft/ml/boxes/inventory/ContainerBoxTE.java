package ml.boxes.inventory;

import ml.boxes.BoxData;
import ml.boxes.TileEntityBox;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerBoxTE extends ContainerBox {

	private final TileEntityBox te;
	
	public ContainerBoxTE(TileEntityBox te, EntityPlayer pl) {
		super(te.data, pl);
		this.te = te;
		te.openChest();
	}

	@Override
	public void onCraftGuiClosed(EntityPlayer par1EntityPlayer) {
		super.onCraftGuiClosed(par1EntityPlayer);
		te.closeChest();
	}

}
