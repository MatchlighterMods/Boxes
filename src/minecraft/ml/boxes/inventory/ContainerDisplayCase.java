package ml.boxes.inventory;

import ml.boxes.tile.TileEntityDisplayCase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class ContainerDisplayCase extends Container {

	public ContainerDisplayCase(TileEntityDisplayCase tedc, EntityPlayer pl) {
		
		int ySize = 166;
		
		addSlotToContainer(new Slot(tedc, 0, 80, 30));
		
		for (int slt=9; slt < pl.inventory.mainInventory.length; slt++){
			int row = (int)Math.floor(slt/9) -1;
			addSlotToContainer(new Slot(pl.inventory, slt, 8 + (slt%9)*18, ySize - 82 + row*18));
		}

		for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
			addSlotToContainer(new Slot(pl.inventory, hotbarSlot, 8 + hotbarSlot * 18, ySize - 24));
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

}
