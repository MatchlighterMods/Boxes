package ml.boxes.tile.safe;

import java.util.List;

import ml.boxes.item.ItemKey;
import ml.boxes.tile.TileEntitySafe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class MechKey extends SafeMechanism {

	static {
		SafeMechanism.registerMechansim(new MechKey());
	}
	
	public int keyId = 0;
	
	public MechKey() {
		super();
	}
	
	@Override
	public void beginUnlock(TileEntitySafe tes, EntityPlayer epl) {
		ItemStack is = epl.getHeldItem();
		if (is.getItem() instanceof ItemKey && is.getItemDamage() == keyId) {
			tes.unlock();
		}
	}

	@Override
	public boolean matches(NBTTagCompound mech1, NBTTagCompound mech2) {
		return mech1.equals(mech2);
	}

	@Override
	public void addISInfo(ItemStack is, List infos) {
		infos.add("KeyId: " + is.stackTagCompound.getInteger("keyId"));
	}
}
