package ml.boxes.inventory;

import ml.boxes.tile.TileEntitySafe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerSafe extends Container {

	public TileEntitySafe safe;
	
	public ContainerSafe(TileEntitySafe tes) {
		safe = tes;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return safe.unlocked;
	}

}
