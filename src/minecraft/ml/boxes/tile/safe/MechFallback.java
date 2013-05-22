package ml.boxes.tile.safe;

import java.util.List;

import ml.boxes.tile.TileEntitySafe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * A fall-back mechanism that will be loaded if a safe fails to save or load properly.
 * Shouldn't ever happen, but neither should NPEs
 * 
 * @author Matchlighter
 */
public class MechFallback extends SafeMechanism {

	public MechFallback() {
		super();
	}

	@Override
	public void beginUnlock(TileEntitySafe tes, EntityPlayer epl) {
		epl.sendChatToPlayer("\u00A77\u00A7oWarning: The safe has been corrupted and can no longer be locked properly!");
		tes.unlock();
	}

	@Override
	public boolean matches(NBTTagCompound mech1, NBTTagCompound mech2) {
		return true;
	}

	@Override
	public void addISInfo(ItemStack is, List infos) {
		// TODO Auto-generated method stub
		
	}
}
