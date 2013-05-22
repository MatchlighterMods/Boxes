package ml.boxes.tile.safe;

import java.util.Arrays;
import java.util.List;

import ml.boxes.Boxes;
import ml.boxes.tile.TileEntitySafe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class MechCombo extends SafeMechanism {

	public static int COMBO_LENGTH = 3;
	
	static {
		SafeMechanism.registerMechansim(new MechCombo());
	}
	
	public MechCombo() {
		super();
	}
		
	@Override
	public NBTTagCompound writeNBTPacket(TileEntitySafe tes) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setIntArray("dispCombo", tes.mechTag.getIntArray("dispCombo"));
		return tag;
	}

	@Override
	public void beginUnlock(TileEntitySafe tes, EntityPlayer epl) {
		if (tes.worldObj.isRemote)
			epl.openGui(Boxes.instance, 3, tes.worldObj, tes.xCoord, tes.yCoord, tes.zCoord);
	}

	@Override
	public boolean matches(NBTTagCompound mech1, NBTTagCompound mech2) {
		return mech1.equals(mech2);
	}

	@Override
	public void addISInfo(ItemStack is, List infos) {
		
	}
}
