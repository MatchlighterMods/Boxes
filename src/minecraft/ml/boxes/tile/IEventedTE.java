package ml.boxes.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;

public interface IEventedTE {

	public boolean onRightClicked(EntityPlayer pl, ForgeDirection side);
	
	public void onLeftClicked(EntityPlayer pl);
	
	public void hostBroken();
	
	public boolean onAttemptUpgrade(EntityPlayer pl, ItemStack is, int side);
}
