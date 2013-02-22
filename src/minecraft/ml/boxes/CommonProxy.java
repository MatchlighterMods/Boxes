package ml.boxes;

import ml.boxes.data.ItemIBox;
import ml.boxes.inventory.ContainerBox;
import ml.boxes.item.ItemBox;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler {

	public void load(){}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		switch (ID) {
		case 1: //BoxIsAsTileEntity
			TileEntity te = world.getBlockTileEntity(x, y, z);
			if (te instanceof TileEntityBox){
				return new ContainerBox(((TileEntityBox)te), player);
			}
			break;
		case 2: //BoxIsAsItem
			ItemStack is = player.getCurrentEquippedItem();
			if (is != null && is.getItem() instanceof ItemBox){ 
				return new ContainerBox(new ItemIBox(is), player);
			}
			break;
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		return null;
	}

}
