package ml.boxes;

import ml.boxes.data.ItemBoxContainer;
import ml.boxes.inventory.ContainerBox;
import ml.boxes.inventory.ContainerSafe;
import ml.boxes.item.ItemBox;
import ml.boxes.tile.TileEntityBox;
import ml.boxes.tile.TileEntitySafe;
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
		TileEntity te = world.getBlockTileEntity(x, y, z);
		switch (ID) {
		case 1: //BoxIsAsTileEntity
			if (te instanceof TileEntityBox){
				return new ContainerBox(((TileEntityBox)te), player);
			}
			break;
		case 2: //BoxIsAsItem
			ItemStack is = player.getCurrentEquippedItem();
			if (is != null && is.getItem() instanceof ItemBox){ 
				return new ContainerBox(new ItemBoxContainer(is), player);
			}
			break;
		case 3: //Safe
			if (te instanceof TileEntitySafe)
				return new ContainerSafe((TileEntitySafe)te);
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
