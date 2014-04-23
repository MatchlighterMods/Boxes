package ml.boxes;

import ml.boxes.data.ItemBoxContainer;
import ml.boxes.inventory.ContainerBox;
import ml.boxes.item.ItemBox;
import ml.boxes.tile.TileEntityBox;
import ml.boxes.tile.TileEntityDisplayCase;
import ml.boxes.tile.TileEntitySafe;
import ml.boxes.window.WindowCombo;
import ml.boxes.window.WindowSafe;
import ml.core.gui.MLGuiHandler;
import ml.core.gui.core.TopParentGuiElement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy extends MLGuiHandler {

	@Override
	public TopParentGuiElement getTopElement(int ID, EntityPlayer player, World world, int x, int y, int z, Side side) {
		
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (te instanceof TileEntitySafe) {
			TileEntitySafe tes = (TileEntitySafe)te;
			if (ID == 11 && te instanceof TileEntitySafe) {
				return new WindowSafe(player, side, tes);
				
			} else if (ID == 12) { //Mech: Combo
				return new WindowCombo(player, side, tes);
			}
		}
		
		return null;
	}
	
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
				
			} else if (te.getClass() == TileEntityDisplayCase.class) {
				//return new ContainerDisplayCase((TileEntityDisplayCase)te, player);
			}
		case 2: //Box as item
			ItemStack is = player.getCurrentEquippedItem();
			if (is != null && is.getItem() instanceof ItemBox){ 
				return new ContainerBox(new ItemBoxContainer(is), player);
			}
			break;
		}
		
		return super.getServerGuiElement(subID, player, world, x, y, z);
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		return super.getClientGuiElement(ID, player, world, x, y, z);
	}

}
