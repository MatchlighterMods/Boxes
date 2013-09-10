package ml.boxes.tile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;

public interface IEventedTE {

	public abstract boolean onRightClicked(EntityPlayer pl, ForgeDirection side);

	public abstract void onLeftClicked(EntityPlayer pl);

	public abstract void hostPlaced(EntityLivingBase pl, ItemStack is);

	public abstract void hostBroken();

	public abstract boolean onAttemptUpgrade(EntityPlayer pl, ItemStack is,
			int side);

	public abstract void onNeighborBlockChange();

}