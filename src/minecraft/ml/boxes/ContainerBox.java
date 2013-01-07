package ml.boxes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class ContainerBox extends Container {

	final BoxData box;
	
	public ContainerBox(BoxData box, EntityPlayer pl) {
		this.box = box;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public ItemStack slotClick(int slotNum, int mouseBtn, int action,
			EntityPlayer par4EntityPlayer) {
		saveInventory();
		return super.slotClick(slotNum, mouseBtn, action, par4EntityPlayer);
	}

	@Override
	public void onCraftGuiClosed(EntityPlayer par1EntityPlayer) {
		
		super.onCraftGuiClosed(par1EntityPlayer);
		
		saveInventory();
	}

	public void saveInventory(){
		
	}
}
