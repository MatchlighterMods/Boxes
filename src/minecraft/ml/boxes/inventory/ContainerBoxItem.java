package ml.boxes.inventory;

import ml.boxes.ItemIBox;
import ml.boxes.item.ItemBox;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBoxItem extends ContainerBox {

	private final ItemIBox itemIBox;
	
	public ContainerBoxItem(ItemIBox box, EntityPlayer pl) {
		super(box, pl);
		itemIBox = box;
	}

	@Override
	public ItemStack slotClick(int slotNum, int mouseBtn, int action,
			EntityPlayer par4EntityPlayer) {
		
//		Slot slot = (Slot)inventorySlots.get(slotNum);
//		if (slot.getStack() == boxStack){
//			super.slotClick(slotNum, mouseBtn, action, par4EntityPlayer);
//			this.player.closeScreen();
//			return null;
//		}
		
		return super.slotClick(slotNum, mouseBtn, action, par4EntityPlayer);
	}

}
