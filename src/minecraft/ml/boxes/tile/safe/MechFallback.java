package ml.boxes.tile.safe;

import ml.boxes.tile.TileEntitySafe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * A fall-back mechanism that will be loaded if a safe fails to save or load properly.
 * Shouldn't ever happen, but neither should NPEs
 * 
 * @author Matchlighter
 */
public class MechFallback extends SafeMechanism {

	public MechFallback(TileEntitySafe tsafe) {
		super(tsafe);
	}

	@Override
	public NBTTagCompound saveNBT() {
		return new NBTTagCompound();
	}

	@Override
	public void loadNBT(NBTTagCompound mechKey) {}

	@Override
	public void beginUnlock(EntityPlayer epl) {
		epl.sendChatToPlayer("\u00A77\u00A7oWarning: The safe has been corrupted and can no longer be locked properly!");
		safe.unlock();
	}

	@Override
	public boolean matches(SafeMechanism tmech) {
		return true;
	}
}
