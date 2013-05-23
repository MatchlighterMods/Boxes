package ml.boxes.tile.safe;

import java.util.List;

import ml.boxes.item.ItemKey;
import ml.boxes.tile.TileEntitySafe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class MechKey extends SafeMechanism {
	
	public int keyId = 0;
	
	public MechKey(TileEntitySafe tsafe) {
		super(tsafe);
	}

	@methodAddInfo()
	public static void getISInfo(ItemStack is, List infos) {
		infos.add("KeyId: " + is.stackTagCompound.getInteger("keyId"));
	}
	
	@Override
	public NBTTagCompound saveNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("keyId", keyId);
		return tag;
	}

	@Override
	public void loadNBT(NBTTagCompound mechKey) {
		keyId = mechKey.getInteger("keyId");
	}
	
	@Override
	public void beginUnlock(EntityPlayer epl) {
		ItemStack is = epl.getHeldItem();
		if (is.getItem() instanceof ItemKey && is.getItemDamage() == keyId) {
			this.safe.unlock();
		}
	}

	@Override
	public boolean matches(SafeMechanism tmech) {
		return ((MechKey)tmech).keyId == keyId;
	}
}
