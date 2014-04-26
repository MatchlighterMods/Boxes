package ml.boxes.api.box;

import ml.boxes.data.Box;
import ml.boxes.data.ItemBoxContainer;
import ml.boxes.inventory.ContainerBox;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * This interface is to be implemented by Classes that contain a box. e.g. The box TileEntity.<br/>
 * This class is in charge of (dumb) reading and writing of Box NBT data to file. i.e. It should NOT process data in any way besides saving and loading. 
 * @author Matchlighter
 */
public interface IBoxContainer {

	public void saveData();
	
	public void boxOpen();
	
	public void boxClose();
	
	/**
	 * @return The {@link Box} this container houses
	 */
	public Box getBox();
	
	/**
	 * Eject an item if a bad item gets pumped in by a non-{@link IInventory}-respecting mod.
	 * @param is
	 */
	public void ejectItem(ItemStack is);
	
	/**
	 * Called by the Container when a slot is clicked.<br/>
	 * Primarily used to prevent dupe-hacks in {@link ItemBoxContainer}
	 * @return true to continue the action, false to stop it
	 */
	public boolean slotPreClick(ContainerBox cb, int slotNum, int mouseBtn, int action,
			EntityPlayer par4EntityPlayer);
	
	public boolean boxUseableByPlayer(EntityPlayer epl);
}
