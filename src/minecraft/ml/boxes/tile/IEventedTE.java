package ml.boxes.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.ForgeDirection;

public interface IEventedTE {

	public boolean onRightClicked(EntityPlayer pl, ForgeDirection side);
	
	public void onLeftClicked(EntityPlayer pl);
	
	public void hostBroken();
}
