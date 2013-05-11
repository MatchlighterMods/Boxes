package ml.boxes.tile.safe;

import java.util.Arrays;

import ml.boxes.Boxes;
import ml.boxes.tile.TileEntitySafe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class mechCombo extends SafeMechanism {

	public static int COMBO_LENGTH = 3;
	
	public int[] combination;
	public int[] dispCombination;
	
	public mechCombo(TileEntitySafe tsafe) {
		super(tsafe);
		combination = new int[COMBO_LENGTH];
	}
	
	@Override
	public NBTTagCompound saveNBT() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void loadNBT(NBTTagCompound mechKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beginUnlock(EntityPlayer epl) {
		if (safe.worldObj.isRemote)
			epl.openGui(Boxes.instance, 3, safe.worldObj, safe.xCoord, safe.yCoord, safe.zCoord);
	}

	@Override
	public boolean matches(SafeMechanism tmech) {
		return Arrays.equals(((mechCombo)tmech).combination, this.combination);
	}
}
