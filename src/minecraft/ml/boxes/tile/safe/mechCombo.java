package ml.boxes.tile.safe;

import java.util.Arrays;
import java.util.List;

import ml.boxes.Boxes;
import ml.boxes.tile.TileEntitySafe;
import ml.boxes.tile.safe.SafeMechanism.methodAddInfo;
import ml.core.StringUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class MechCombo extends SafeMechanism {

	public static int COMBO_LENGTH = 3;
	
	public int[] combination;
	public int[] dispCombination;
	
	public MechCombo(TileEntitySafe tsafe) {
		super(tsafe);
		combination = new int[COMBO_LENGTH];
	}
	
	@methodAddInfo()
	public static void getISInfo(ItemStack is, List infos) {
		infos.add("Combination: " + StringUtils.join(is.stackTagCompound.getIntArray("combination"), "-"));
	}
	
	@Override
	public NBTTagCompound saveNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setIntArray("combination", combination);
		tag.setIntArray("dispCombo", dispCombination);
		return tag;
	}
	@Override
	public void loadNBT(NBTTagCompound mechKey) {
		combination = mechKey.getIntArray("combination");
		dispCombination = mechKey.getIntArray("dispCombo");
		
		if (combination.length != COMBO_LENGTH) combination = new int[3];
		if (dispCombination.length != COMBO_LENGTH) dispCombination = new int[3];
	}
	
	@Override
	public NBTTagCompound writeNBTPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setIntArray("dispCombo", dispCombination);
		return tag;
	}

	@Override
	public void beginUnlock(EntityPlayer epl) {
		if (safe.worldObj.isRemote)
			epl.openGui(Boxes.instance, 3, safe.worldObj, safe.xCoord, safe.yCoord, safe.zCoord);
	}

	@Override
	public boolean matches(SafeMechanism tmech) {
		return Arrays.equals(((MechCombo)tmech).combination, this.combination);
	}
}
