package ml.boxes.tile;

import java.util.Arrays;

import ml.boxes.Boxes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.ForgeDirection;

public class TileEntitySafeCombo extends TileEntitySafe {

	public static int COMBO_LENGTH = 3;
	
	public int[] combination;
	public int[] dispCombination;
	
	public TileEntitySafeCombo() {
		combination = new int[COMBO_LENGTH];
	}
	
	@Override
	protected boolean canConnectWith(TileEntitySafe remoteTes) {
		return Arrays.equals(((TileEntitySafeCombo)remoteTes).combination, this.combination);
	}
	
	@Override
	public boolean onRightClicked(EntityPlayer pl, ForgeDirection side) {
		if (worldObj.isRemote) pl.openGui(Boxes.instance, 3, worldObj, xCoord, yCoord, zCoord);
		return true;
	}
}
