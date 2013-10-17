package ml.boxes.window;

import ml.boxes.tile.TileEntitySafe;
import ml.core.gui.controls.inventory.ControlPlayerInventory;
import ml.core.gui.core.Window;
import ml.core.vec.Vector2i;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import cpw.mods.fml.relauncher.Side;

public class WindowSafe extends Window {

	public TileEntitySafe tes;
	public IInventory sinv;
	
	public WindowSafe(EntityPlayer epl, Side side, TileEntitySafe sf) {
		super(epl, side);
		
		tes = sf;
		sinv = tes.isConnected() ? new InventoryLargeChest("", ((TileEntitySafe)tes.getConnected()).inventory, tes.inventory) : tes.inventory;
		
		setSize(178, 132 + 18*sinv.getSizeInventory()/9);
	}

	@Override
	public void initControls() {
		new ControlPlayerInventory(this, player, new Vector2i(8, getSize().y-76-8));
	}

	@Override
	public boolean canInteractWith(EntityPlayer epl) {
		return sinv.isUseableByPlayer(epl);
	}

}
