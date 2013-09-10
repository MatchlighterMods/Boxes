package ml.boxes;

import ml.boxes.data.ItemBoxContainer;
import ml.boxes.inventory.ContainerBox;
import ml.boxes.inventory.ContainerDisplayCase;
import ml.boxes.inventory.ContainerSafe;
import ml.boxes.item.ItemBox;
import ml.boxes.tile.TileEntityBox;
import ml.boxes.tile.TileEntityDisplayCase;
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
		
		int aID = ID>>4, subID = ID & 15;
		switch (aID) {
		case 0:
			TileEntity te = world.getBlockTileEntity(x, y, z);
			if (te == null) return null;
			if (te.getClass() == TileEntityBox.class){
				return new ContainerBox(((TileEntityBox)te), player);
				
			} else if (te.getClass() == TileEntitySafe.class) {
				TileEntitySafe tes = (TileEntitySafe)te;
				return new ContainerSafe(player, tes);
				
			} else if (te.getClass() == TileEntityDisplayCase.class) {
				return new ContainerDisplayCase((TileEntityDisplayCase)te, player);
			}
		case 2: //Box as item
			ItemStack is = player.getCurrentEquippedItem();
			if (is != null && is.getItem() instanceof ItemBox){ 
				return new ContainerBox(new ItemBoxContainer(is), player);
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
