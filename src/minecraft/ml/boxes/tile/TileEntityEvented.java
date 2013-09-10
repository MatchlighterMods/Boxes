package ml.boxes.tile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class TileEntityEvented extends TileEntity implements IEventedTE {

	@Override
	public boolean onRightClicked(EntityPlayer pl, ForgeDirection side) {
		return false;
	}

	@Override
	public void onLeftClicked(EntityPlayer pl) {

	}

	@Override
	public void hostPlaced(EntityLivingBase pl, ItemStack is) {

	}

	@Override
	public void hostBroken() {

	}

	@Override
	public boolean onAttemptUpgrade(EntityPlayer pl, ItemStack is, int side) {
		return false;
	}

	@Override
	public void onNeighborBlockChange() {

	}

}
